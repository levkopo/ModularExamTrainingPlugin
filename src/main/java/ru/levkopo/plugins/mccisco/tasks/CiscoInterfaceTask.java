package ru.levkopo.plugins.mccisco.tasks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.levkopo.plugins.mccisco.tasks.result.TaskResult;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class CiscoInterfaceTask extends Task {
    private final String initialHostname;
    private final Context initialContext;
    protected @NotNull Context context;

    public enum Context {
        NONE,
        ENABLED,
        CONFIGURE,
        CONFIGURE_INTERFACE,
        CONFIGURE_LINE,
        INPUT,
    }

    public interface TerminalFunction extends Function<ArrayList<String>, Object> {
    }

    public interface VerifyFunction extends Function<CiscoInterfaceTask, Boolean> {
    }

    public static class NetInterface {
        public @Nullable String ipAddress;
        public @Nullable String mask;
        public boolean enabled = false;
        public @Nullable ChannelGroup channelGroup = null;

        public final @NotNull String type;
        public final int networkCard;
        public final int number;

        public static class ChannelGroup {
            public final @NotNull int number;
            public final @NotNull String mode;

            public ChannelGroup(int number, String mode) {
                this.number = number;
                this.mode = mode;
            }
        }

        public NetInterface(String type, int networkCard, int number) {
            this.type = type;
            this.networkCard = networkCard;
            this.number = number;
        }
    }

    public static class UserData {
        public final String password;
        public final int privilege;

        public UserData(String password, int privilege) {
            this.password = password;
            this.privilege = privilege;
        }
    }

    public static class Line {
        public enum Login {
            NONE,
            LOCAL,
            AUTHENTICATION
        }

        public Login login;
        public String transportInput;
    }

    public @NotNull String hostname;
    public @Nullable String domainName = null;
    public @Nullable String enablePassword = null;
    public @Nullable Integer sshVersion = null;
    public @Nullable Integer cryptoKey = null;
    public @Nullable NetInterface configureInterface = null;
    public @Nullable List<Line> configureLines = null;
    public @Nullable Consumer<String> inputConsumer = null;
    public final String taskDescription;
    public final VerifyFunction verifyFunction;

    public List<NetInterface> interfaces = new ArrayList<>();
    public Map<String, List<Line>> lines = new HashMap<>();
    public Map<String, UserData> users = new HashMap<>();

    private void enableCtx(int attempt) {
        readInput(password -> {
            if (Objects.equals(password, enablePassword)) {
                context = Context.ENABLED;
            } else if (attempt == 3) {
                error("Invalid password. Tey again later");
            } else {
                error("Invalid password");
                getPlayer().sendMessage(text("Password: ", WHITE));
                enableCtx(attempt + 1);
            }
        });
    }

    public Map<Context, Map<String, Object>> commands = Map.ofEntries(
            Map.entry(Context.NONE, Map.ofEntries(
                    Map.entry("enable", (TerminalFunction) (data) -> {
                        if (enablePassword != null) {
                            enableCtx(0);
                            return true;
                        }

                        context = Context.ENABLED;
                        return true;
                    })
            )),
            Map.entry(Context.ENABLED, Map.ofEntries(
                    Map.entry("exit", (TerminalFunction) (data) -> {
                        context = Context.NONE;
                        return true;
                    }),
                    Map.entry("configure", Map.ofEntries(
                            Map.entry("terminal", (TerminalFunction) (data) -> {
                                context = Context.CONFIGURE;
                                return true;
                            })
                    ))
            )),
            Map.entry(Context.CONFIGURE, Map.ofEntries(
                    Map.entry("exit", (TerminalFunction) (data) -> {
                        context = Context.ENABLED;
                        return true;
                    }),
                    Map.entry("username", (TerminalFunction) (usernameData) -> {
                        String username = usernameData.removeFirst();

                        if (usernameData.size() > 1) {
                            AtomicInteger privilegeAtomic = new AtomicInteger(0);
                            Map<String, Object> passwordSetCommands = Map.ofEntries(
                                    Map.entry("password", (TerminalFunction) (passwordData) -> {
                                        registerUser(username, passwordData.removeFirst(), privilegeAtomic.get());
                                        return true;
                                    }),
                                    Map.entry("secret", (TerminalFunction) (passwordData) -> {
                                        registerUser(username, passwordData.removeFirst(), privilegeAtomic.get());
                                        return true;
                                    })
                            );

                            HashMap<String, Object> privilegeSetCommands = new HashMap<>(Map.ofEntries(
                                    Map.entry("privilege", (TerminalFunction) (privilegeData) -> {
                                        privilegeAtomic.set(Integer.parseInt(privilegeData.removeFirst()));
                                        if (privilegeData.size() > 1) {
                                            return passwordSetCommands;
                                        } else {
                                            registerUser(username, "", 0);
                                            return true;
                                        }
                                    })
                            ));
                            privilegeSetCommands.putAll(passwordSetCommands);

                            return privilegeSetCommands;
                        }

                        registerUser(username, "", 0);
                        return true;
                    }),
                    Map.entry("line", (TerminalFunction) (data) -> lines.keySet().stream().map(type ->
                            Map.entry(type, (TerminalFunction) (typeData) -> {
                                int from = Integer.parseInt(typeData.removeFirst());
                                int to = typeData.size() == 1 ? from : Integer.parseInt(typeData.removeFirst());
                                configureLines = lines.get(type).subList(from, to);

                                context = Context.CONFIGURE_LINE;
                                return type;
                            })
                    )),
                    Map.entry("interface", (TerminalFunction) (data) -> {
                        String interfaceName = String.join("", data).toLowerCase();
                        StringBuilder interfaceType = new StringBuilder();
                        for (int i = 0; i < interfaceName.length() - 1; i++) {
                            char c = interfaceName.charAt(i);
                            if (String.valueOf(c).matches("[0-9]+")) {
                                break;
                            }

                            interfaceType.append(c);
                        }

                        String[] numData = interfaceName.substring(interfaceType.length()).split("/");
                        int cardNum = Integer.parseInt(numData[0]);
                        int interfaceNum = Integer.parseInt(numData[1]);

                        NetInterface candidate = null;
                        for (NetInterface netInterface : interfaces) {
                            if (netInterface.type.toLowerCase()
                                    .startsWith(interfaceType.toString()) && netInterface.networkCard == cardNum && netInterface.number == interfaceNum) {
                                if (candidate != null) {
                                    return false;
                                }

                                candidate = netInterface;
                            }
                        }

                        if (candidate == null) {
                            return false;
                        }

                        context = Context.CONFIGURE_INTERFACE;
                        configureInterface = candidate;
                        return true;
                    }),
                    Map.entry("hostname", (TerminalFunction) (data) -> {
                        hostname = data.getFirst();
                        return true;
                    }),
                    Map.entry("ip", Map.ofEntries(
                            Map.entry("domain-name", (TerminalFunction) (data) -> {
                                domainName = data.getFirst();
                                return true;
                            })
                    )),
                    Map.entry("enable", Map.ofEntries(
                            Map.entry("password", (TerminalFunction) (data) -> {
                                enablePassword = data.getFirst();
                                return true;
                            }),
                            Map.entry("secret", (TerminalFunction) (data) -> {
                                enablePassword = data.getFirst();
                                return true;
                            })
                    ))
            )),
            Map.entry(Context.CONFIGURE_INTERFACE, Map.ofEntries(
                    Map.entry("exit", (TerminalFunction) (data) -> {
                        context = Context.CONFIGURE;
                        return true;
                    }),
                    Map.entry("end", (TerminalFunction) (data) -> {
                        context = Context.ENABLED;
                        return true;
                    }),
                    Map.entry("no", Map.ofEntries(
                            Map.entry("shutdown", (TerminalFunction) (data) -> {
                                configureInterface.enabled = true;
                                return true;
                            })
                    )),
                    Map.entry("crypto", Map.ofEntries(
                            Map.entry("key", Map.ofEntries(
                                    Map.entry("generate", Map.ofEntries(
                                            Map.entry("rsa", (TerminalFunction) (data) -> {
                                                if (domainName == null) {
                                                    error("Please set domain name first");
                                                    return true;
                                                }

                                                getPlayer().sendMessage("""
                                                    The name for the keys will be: kras831-10.80.176.254.domainnamehere.com
                                                    Choose the size of the key modulus in the range of 360 to 2048 for your
                                                    General Purpose Keys. Choosing a key modulus greater than 512 may take
                                                    a few minutes.
                                                    How many bits in the modulus:\s
                                                """);

                                                readInput(sizeRaw -> {
                                                    int size = Integer.parseInt(sizeRaw);
                                                    if (size < 512 || size > 2048) {
                                                        error("Invalid key size");
                                                    }

                                                    getPlayer().sendMessage("% Generating "+size+" bit RSA keys, keys will be non-exportable...[OK]");
                                                    cryptoKey = size;
                                                });
                                                return true;
                                            })
                                    ))
                            ))
                    )),
                    Map.entry("shutdown", (TerminalFunction) (data) -> {
                        configureInterface.enabled = false;
                        return true;
                    }),
                    Map.entry("channel-group", (TerminalFunction) (numberData) -> {
                        int number = Integer.parseInt(numberData.removeFirst());
                        return Map.ofEntries(
                                Map.entry("mode", (TerminalFunction) (modeData) -> {
                                    configureInterface.channelGroup = new NetInterface.ChannelGroup(number, modeData.removeFirst());
                                    return true;
                                })
                        );
                    }),
                    Map.entry("ip", Map.ofEntries(
                            Map.entry("address", (TerminalFunction) (data) -> {
                                configureInterface.ipAddress = data.getFirst();
                                configureInterface.mask = data.get(1);
                                return true;
                            }),
                            Map.entry("ssh", Map.ofEntries(
                                    Map.entry("version", (TerminalFunction) (data) -> {
                                        sshVersion = Integer.parseInt(data.getFirst());
                                        return true;
                                    })
                            ))
                    ))
            )),
            Map.entry(Context.CONFIGURE_LINE, Map.ofEntries(
                    Map.entry("exit", (TerminalFunction) (data) -> {
                        context = Context.CONFIGURE;
                        return true;
                    }),
                    Map.entry("end", (TerminalFunction) (data) -> {
                        context = Context.ENABLED;
                        return true;
                    }),
                    Map.entry("transport", Map.ofEntries(
                            Map.entry("input", (TerminalFunction) (data) -> {
                                String type = data.removeFirst();
                                if (Objects.equals(type, "ssh")) {
                                    if (cryptoKey == null) {
                                        error("Crypto key not generated");
                                        return true;
                                    }
                                }

                                applyToLines(line -> line.transportInput = data.getFirst());
                                return true;
                            })
                    )),
                    Map.entry("no", Map.ofEntries(
                            Map.entry("login", (TerminalFunction) (data) -> {
                                applyToLines(line -> line.login = Line.Login.NONE);
                                return true;
                            })
                    )),
                    Map.entry("login", Map.ofEntries(
                            Map.entry("local", (TerminalFunction) (data) -> {
                                applyToLines(line -> line.login = Line.Login.LOCAL);
                                return true;
                            })
                    ))
            ))
    );

    public void applyToLines(Consumer<Line> consumer) {
        if (configureLines != null) {
            for (Line line : configureLines) {
                consumer.accept(line);
            }
        }
    }

    public boolean verifyInterface(String type, int networkCard, int number, Predicate<NetInterface> consumer) {
        NetInterface candidate = null;
        for (NetInterface netInterface : interfaces) {
            if (netInterface.type.equals(type) && netInterface.networkCard == networkCard && netInterface.number == number) {
                candidate = netInterface;
                break;
            }
        }

        return consumer.test(candidate);
    }

    public boolean verifyLines(String type, Predicate<Line> consumer) {
        for (Line line : lines.get(type)) {
            if (!consumer.test(line)) {
                return false;
            }
        }

        return true;
    }

    public boolean auth(String username, String password, int minPrivilege) {
        if (users.containsKey(username)) {
            UserData userData = users.get(username);
            return userData.password.equals(password) && userData.privilege >= minPrivilege;
        }

        return false;
    }

    public void registerUser(String name, String password, int privilege) {
        users.put(name, new UserData(password, privilege));
    }

    public void reload() {
        interfaces = List.of(
                new NetInterface("GigabitEthernet", 1, 0),
                new NetInterface("GigabitEthernet", 1, 1),
                new NetInterface("GigabitEthernet", 2, 0),
                new NetInterface("GigabitEthernet", 2, 1),
                new NetInterface("GigabitEthernet", 2, 2),
                new NetInterface("GigabitEthernet", 3, 0)
        );

        lines = Map.ofEntries(
                Map.entry("vty", List.of(
                        new Line(), new Line(), new Line(),
                        new Line(), new Line(), new Line(),
                        new Line(), new Line(), new Line(),
                        new Line(), new Line(), new Line(),
                        new Line(), new Line(), new Line()
                )),
                Map.entry("console", List.of(
                        new Line()
                ))
        );

        hostname = initialHostname;
        context = initialContext;
        enablePassword = null;
        cryptoKey = null;
        sshVersion = null;
    }

    public CiscoInterfaceTask(TaskResult result, @NotNull String initialHostname, @NotNull Context initialContext, String taskDescription, VerifyFunction verifyFunction) {
        super(result);
        this.initialHostname = initialHostname;
        this.initialContext = initialContext;
        this.taskDescription = taskDescription;
        this.verifyFunction = verifyFunction;

        hostname = initialHostname;
        context = initialContext;
    }

    public void readInput(Consumer<String> consumer) {
        final Context prevContext = context;
        inputConsumer = data -> {
            inputConsumer = null;
            context = prevContext;
            consumer.accept(data);
        };

        context = Context.INPUT;
    }

    public void error(String taskDescription) {
        getPlayer().sendMessage(text(taskDescription, RED));
    }

    public Component renderCiscoLine(String text) {
        return text(hostname)
                .append(text(context == Context.CONFIGURE ? "(config)" : (context == Context.CONFIGURE_INTERFACE ? "(config-if)" : "")))
                .append(text(context == Context.NONE ? ">" : "#"))
                .appendSpace()
                .append(text(text));
    }

    public void buildHelp(Map<String, Object> commands, String prefix) {
        Component helpText = Component.empty();
        for (String subcommand : commands.keySet()) {
            if (subcommand.startsWith(prefix)) {
                helpText = helpText
                        .append(text(subcommand))
                        .appendNewline();
            }
        }

        getPlayer().sendMessage(helpText);
    }

    public boolean executeCiscoCommand(Map<String, Object> commands, ArrayList<String> data) {
        String command = data.removeFirst();
        if (command.endsWith("?")) {
            buildHelp(commands, command.substring(0, command.length() - 1));
            return true;
        }

        Object variant = null;
        for (Map.Entry<String, Object> entry : commands.entrySet()) {
            if (entry.getKey().startsWith(command)) {
                if (variant != null) {
                    error("Invalid command");
                    return false;
                }

                variant = entry.getValue();
            }
        }

        if (variant instanceof TerminalFunction) {
            variant = ((TerminalFunction) variant).apply(data);
        }

        if (variant instanceof Map<?, ?>) {
            Map<String, Object> subcommands = (Map<String, Object>) variant;
            return executeCiscoCommand(subcommands, data);
        }

        if (variant instanceof Boolean && (boolean) variant) {
            return true;
        }

        error("Command not found");
        return false;
    }

    public void renderMessage(@NotNull Player player, @NotNull Component message) {
        TextComponent component = (TextComponent) message;
        String text = component.content();

        try {
            if (context == Context.INPUT) {
                if (inputConsumer != null) {
                    inputConsumer.accept(text);
                } else {
                    error("Input error. Exit.");
                    context = Context.NONE;
                }
            } else {
                player.sendMessage(renderCiscoLine(text));
                Map<String, Object> commandsForContext = commands.get(context);
                executeCiscoCommand(commandsForContext, new ArrayList<>(List.of(text.split(" "))));
            }
        } catch (Exception e) {
            error("Invalid syntax");
            System.err.println(e.getMessage());
        }

        if (verifyFunction.apply(this)) {
            done(false);
            return;
        }

        player.sendMessage(renderCiscoLine(""));
    }

    @Override
    public void onStart(Player player) {
        super.onStart(player);
        reload();

        player.getServer().broadcast(
                text("[ЗАДАЧА]:", RED)
                        .appendSpace()
                        .append(text(taskDescription, WHITE))
                        .appendNewline()
                        .append(text("Если игрок " + player.getName() + " сможет решить", WHITE))
                        .appendSpace()
                        .append(text((result.negative ? "неверно" : "верно"), WHITE))
                        .appendSpace()
                        .append(text("то,", WHITE))
                        .appendSpace()
                        .append(text(result.description, WHITE))
        );
    }
}
