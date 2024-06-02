package ru.levkopo.plugins.mccisco.tasks;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.levkopo.plugins.mccisco.UniqueItems;
import ru.levkopo.plugins.mccisco.tasks.result.DowngradeWorldBorder;
import ru.levkopo.plugins.mccisco.tasks.result.GiveItemResult;
import ru.levkopo.plugins.mccisco.tasks.result.TimeUpgradeWorldBorder;
import ru.levkopo.plugins.mccisco.tasks.result.UpgradeWorldBorder;

import java.security.SecureRandom;
import java.util.Objects;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class TasksProvider {
    private static final SecureRandom random = new SecureRandom();
    protected final static Task[] tasks = new Task[]{
            new QuestionTask(
                    "Какой уровень модели OSI реализует простокол TCP?",
                    "транспортный",
                    new DowngradeWorldBorder(30)
            ),
            new QuestionTask(
                    "Какой уровень модели OSI реализует простокол UDP?",
                    "транспортный",
                    new UpgradeWorldBorder(80)
            ),
            new QuestionTask(
                    "Какой уровень модели OSI реализует простокол SNMP?",
                    "прикладной",
                    new DowngradeWorldBorder(60)
            ),
            new QuestionTask(
                    "Как выглядит маска для ip адреса 192.168.0.1/24?",
                    "255.255.255.0",
                    new UpgradeWorldBorder(12)
            ),
            new QuestionTask(
                    "Как выглядит маска для ip адреса 192.168.0.1/27?",
                    "255.255.255.224",
                    new UpgradeWorldBorder(40)
            ),
            new QuestionTask(
                    "Как выглядит маска для ip адреса 192.168.0.1/20?",
                    "255.255.240.0",
                    new DowngradeWorldBorder(15)
            ),
            new QuestionTask(
                    "Как выглядит маска для ip адреса 192.168.0.1/16?",
                    "255.255.0.0",
                    new DowngradeWorldBorder(25)
            ),
            new QuestionTask(
                    "Как выглядит маска для ip адреса 192.168.0.1/10?",
                    "255.192.0.0",
                    new DowngradeWorldBorder(50)
            ),
            new QuestionTask(
                    "Как выглядит маска для ip адреса 192.168.0.1/8?",
                    "255.0.0.0",
                    new UpgradeWorldBorder(25)
            ),
            new QuestionTask(
                    "Существует ли протокол RTCP?",
                    "да",
                    new DowngradeWorldBorder(35)
            ),
            new QuestionTask(
                    "Существует ли протокол SIP?",
                    "да",
                    new DowngradeWorldBorder(12)
            ),
            new QuestionTask(
                    "Существует ли протокол TRRR?",
                    "нет",
                    new UpgradeWorldBorder(10)
            ),
            new QuestionTask(
                    "Где хранится сохраненная конфигурация сетевого устройства?",
                    "nvram",
                    new GiveItemResult(UniqueItems.SIMPLE_ITEMS)
            ),
            new QuestionTask(
                    "К какому типу сети относится сеть стандарта IEEE 802.11?",
                    "wlan",
                    new GiveItemResult(UniqueItems.SIMPLE_ITEMS)
            ),
            new QuestionTask(
                    "Набор правил, определяющий принципы взаимодействия устройств в сети. Что это?",
                    "протокол",
                    new GiveItemResult(UniqueItems.SIMPLE_ITEMS)
            ),
            new QuestionTask(
                    "Какой адрес используется для настройки определенной топологии в сети Wi-Fi?",
                    "физический",
                    new GiveItemResult(UniqueItems.SIMPLE_ITEMS)
            ),
            new QuestionTask(
                    "4-й уровень сетевой модели OSI, предназначен для доставки данных. Как он называется?",
                    "транспортный",
                    new GiveItemResult(UniqueItems.SIMPLE_ITEMS)
            ),
            new QuestionTask(
                    "3-й уровень сетевой модели OSI, предназначается для определения пути передачи данных. Как он называется?",
                    "сетевой",
                    new GiveItemResult(UniqueItems.SIMPLE_ITEMS)
            ),
            new QuestionTask(
                    "Сколько бит содержит адрес IPv4?",
                    "32",
                    new UpgradeWorldBorder(25)
            ),
            new QuestionTask(
                    "Сколько бит содержит адрес IPv6?",
                    "128",
                    new UpgradeWorldBorder(25)
            ),
            new QuestionTask(
                    "Какое устройство ПЭВМ хранит операционную систему после ее загрузки?",
                    "озу",
                    new DowngradeWorldBorder(50)
            ),
            new CiscoInterfaceTask(
                    new TimeUpgradeWorldBorder(1000, 1),
                    "Switch",
                    CiscoInterfaceTask.Context.NONE,
                    "Настройте на коммутаторе имя устройства HQ-RTR",
                    (data) -> data.hostname.equals("HQ-RTR")
            ),
            new CiscoInterfaceTask(
                    new TimeUpgradeWorldBorder(250, 3),
                    "Router",
                    CiscoInterfaceTask.Context.NONE,
                    "Настройте доменное имя cisco.com на роутере",
                    (data) -> Objects.equals(data.domainName, "cisco.com")
            ),
            new CiscoInterfaceTask(
                    new TimeUpgradeWorldBorder(400, 2),
                    "Switch",
                    CiscoInterfaceTask.Context.NONE,
                    "Настройте telnet на коммутаторе",
                    (data) -> data.lines.get("vty").stream().filter(it -> !Objects.equals(it.transportInput, "telnet")).toArray().length == 0
            ),
            new CiscoInterfaceTask(
                    new GiveItemResult(UniqueItems.SIMPLE_ITEMS),
                    "Router",
                    CiscoInterfaceTask.Context.NONE,
                    "Настройте ip адрес 1.2.2.1/16 на интерфейсе GigabitEthernet1/0. Также включите этот интерфейс",
                    (data) -> data.verifyInterface("GigabitEthernet", 1, 0,
                            inf -> inf.enabled && Objects.equals(inf.ipAddress, "1.2.2.1") && Objects.equals(inf.mask, "255.255.0.0"))
            ),
            new CiscoInterfaceTask(
                    new GiveItemResult(UniqueItems.SIMPLE_ITEMS),
                    "Firewall",
                    CiscoInterfaceTask.Context.NONE,
                    "Настройте ip адрес 75.53.123.53/16 на интерфейсе GigabitEthernet2/0. Также включите этот интерфейс",
                    (data) -> data.verifyInterface("GigabitEthernet", 2, 0,
                            inf -> inf.enabled && Objects.equals(inf.ipAddress, "75.53.123.53") && Objects.equals(inf.mask, "255.255.0.0"))
            ),
            new CiscoInterfaceTask(
                    new GiveItemResult(UniqueItems.MEDIUM_ITEMS),
                    "SW1",
                    CiscoInterfaceTask.Context.NONE,
                    "Настройте агрегацию по протоколу LACP на интерфейсах GigabitEtherne1/0, GigabitEtherne2/0. Гарантируется что на другом устройстве настроен passive режим по 2 группе каналов",
                    (data) -> data.verifyInterface("GigabitEthernet", 1, 0,
                            inf -> inf.channelGroup != null && inf.channelGroup.number == 2 && Objects.equals(inf.channelGroup.mode, "active")) &&
                            data.verifyInterface("GigabitEthernet", 2, 0,
                                    inf -> inf.channelGroup != null && inf.channelGroup.number == 2 && Objects.equals(inf.channelGroup.mode, "active"))
            ),
            new CiscoInterfaceTask(
                    new GiveItemResult(UniqueItems.MEDIUM_ITEMS),
                    "SW1",
                    CiscoInterfaceTask.Context.NONE,
                    "Настройте агрегацию по протоколу LACP на интерфейсах GigabitEtherne1/0, GigabitEtherne2/0. Гарантируется что на другом устройстве настроен active режим по 4 группе каналов",
                    (data) -> data.verifyInterface("GigabitEthernet", 1, 0,
                            inf -> inf.channelGroup != null && inf.channelGroup.number == 4 && (Objects.equals(inf.channelGroup.mode, "active") || Objects.equals(inf.channelGroup.mode, "passive"))) &&
                            data.verifyInterface("GigabitEthernet", 2, 0,
                                    inf -> inf.channelGroup != null && inf.channelGroup.number == 4 && (Objects.equals(inf.channelGroup.mode, "active") || Objects.equals(inf.channelGroup.mode, "passive")))
            ),
            new CiscoInterfaceTask(
                    new GiveItemResult(UniqueItems.MEDIUM_ITEMS),
                    "SW2",
                    CiscoInterfaceTask.Context.NONE,
                    "Настройте агрегацию по протоколу PAgP на интерфейсах GigabitEtherne1/0, GigabitEtherne2/0. Гарантируется что на другом устройстве настроен auto режим по 4 группе каналов",
                    (data) -> data.verifyInterface("GigabitEthernet", 1, 0,
                            inf -> inf.channelGroup != null && inf.channelGroup.number == 2 && Objects.equals(inf.channelGroup.mode, "desirable")) &&
                            data.verifyInterface("GigabitEthernet", 2, 0,
                                    inf -> inf.channelGroup != null && inf.channelGroup.number == 2 && Objects.equals(inf.channelGroup.mode, "desirable"))
            ),
            new CiscoInterfaceTask(
                    new GiveItemResult(UniqueItems.MEDIUM_ITEMS),
                    "Router",
                    CiscoInterfaceTask.Context.NONE,
                    "Настройте ip адрес 34.4.65.1/24 на интерфейсе GigabitEthernet2/1. " +
                            "Также включите этот интерфейс и защитите консольный порт, создав пользователя sshuser с паролем P@ssw0rd. Дайте пользователю привелегию 12",
                    (data) -> data.verifyInterface("GigabitEthernet", 2, 1,
                            inf -> inf.enabled && Objects.equals(inf.ipAddress, "34.4.65.1") && Objects.equals(inf.mask, "255.255.255.0"))
                            && data.auth("sshuser", "P@ssw0rd", 12) && data.lines.get("console").getFirst().login == CiscoInterfaceTask.Line.Login.LOCAL
            ),
            new CiscoInterfaceTask(
                    new GiveItemResult(UniqueItems.MEDIUM_ITEMS),
                    "SW",
                    CiscoInterfaceTask.Context.NONE,
                    "Настройте ip адрес 5.4.65.1/8 на интерфейсе GigabitEtherne2/0. " +
                            "Также включите этот интерфейс и защитите консольный порт, создав пользователя Sc0rp1o с паролем pasSword. Дайте пользователю привелегию 15",
                    (data) -> data.verifyInterface("GigabitEthernet", 2, 0,
                            inf -> inf.enabled && Objects.equals(inf.ipAddress, "5.4.65.1") && Objects.equals(inf.mask, "255.0.0.0"))
                            && data.auth("Sc0rp1o", "pasSword", 15) && data.lines.get("console").getFirst().login == CiscoInterfaceTask.Line.Login.LOCAL
            ),
            new CiscoInterfaceTask(
                    new GiveItemResult(UniqueItems.ULTIMATE_ITEMS),
                    "lk-server",
                    CiscoInterfaceTask.Context.NONE,
                    "Настройте ip адрес 5.4.65.1/8 на интерфейсе GigabitEtherne2/0. Настройте доменное имя lk.ru. " +
                            "Включите этот интерфейс и настройте удаленный доступ по ssh v2, создав пользователя levkopo с паролем r3212. Дайте пользователю привелегию 6. О",
                    (data) -> data.verifyInterface("GigabitEthernet", 2, 0,
                            inf -> inf.enabled && Objects.equals(inf.ipAddress, "5.4.65.1") && Objects.equals(inf.mask, "255.0.0.0"))
                            && data.auth("levkopo", "r3212", 6)
                            && data.sshVersion == 2
                            && data.verifyLines("vty", line -> line.login == CiscoInterfaceTask.Line.Login.LOCAL && Objects.equals(line.transportInput, "ssh"))
            ),
            new CiscoInterfaceTask(
                    new GiveItemResult(UniqueItems.ULTIMATE_ITEMS),
                    "vs-server",
                    CiscoInterfaceTask.Context.NONE,
                    "Настройте ip адрес 5.4.65.2/8 на интерфейсе GigabitEtherne2/0. Настройте доменное имя vc.ru. " +
                            "Включите этот интерфейс и настройте удаленный доступ по telnet, создав пользователя vc с паролем r3212. Дайте пользователю привелегию 2. О",
                    (data) -> data.verifyInterface("GigabitEthernet", 2, 0,
                            inf -> inf.enabled && Objects.equals(inf.ipAddress, "5.4.65.2") && Objects.equals(inf.mask, "255.0.0.0"))
                            && data.auth("v", "r3212", 2)
                            && data.verifyLines("vty", line -> line.login == CiscoInterfaceTask.Line.Login.LOCAL && Objects.equals(line.transportInput, "telnet"))
            ),
    };

    private static @Nullable Task task = null;

    public static boolean handleChatEvent(AsyncChatEvent event) {
        if (task != null) {
            @NotNull Player player = event.getPlayer();
            if (!task.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(text("БЕЗ ПОДСКАЗОК", RED));
            } else {
                event.setCancelled(true);
                task.renderMessage(event.getPlayer(), event.message());
            }

            return true;
        }

        return false;
    }

    public static void stopTask() {
        TasksProvider.task = null;
    }

    public static void startTask(Player player, Task task) {
        if(TasksProvider.task != null) {
            return;
        }

        TasksProvider.task = task;
        task.onStart(player);
    }

    public static void startRandomTask(Player player) {
        startTask(player, tasks[random.nextInt(tasks.length)]);
    }
}
