package ru.levkopo.plugins.mccisco;

import io.papermc.lib.PaperLib;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.levkopo.plugins.mccisco.tasks.CiscoInterfaceTask;
import ru.levkopo.plugins.mccisco.tasks.Task;
import ru.levkopo.plugins.mccisco.tasks.TasksProvider;
import ru.levkopo.plugins.mccisco.tasks.result.TimeUpgradeWorldBorder;
import ru.levkopo.plugins.mccisco.tasks.result.UpgradeWorldBorder;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;
import static net.kyori.adventure.text.format.TextColor.color;
import static ru.levkopo.plugins.mccisco.tasks.TasksProvider.startRandomTask;
import static ru.levkopo.plugins.mccisco.tasks.TasksProvider.startTask;


/**
 * Created by Levi Muniz on 7/29/20.
 *
 * @author Copyright (c) Levi Muniz. All Rights Reserved.
 */
public class McCiscoPlugin extends JavaPlugin implements Listener, ChatRenderer {
    public static McCiscoPlugin instance;
    private static final SecureRandom random = new SecureRandom();

    @Override
    public void onEnable() {
        instance = this;
        PaperLib.suggestPaper(this);
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            List<Player> players = new ArrayList<>(getServer().getOnlinePlayers());
            if(players.isEmpty()) {
                return;
            }

            TasksProvider.startRandomTask(players.get(random.nextInt(players.size())));
        }, 0L, (long) (24000 * 1.5));

        Objects.requireNonNull(getCommand("get_unique")).setExecutor(new GetUniqueCommand());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(
                text("Добро пожаловать на ", RED)
                        .append(text("Cisco MC Server!", BLUE))
        );
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncChatEvent event) {
        if (TasksProvider.handleChatEvent(event)) {
            return;
        }

        event.renderer(this);
    }

    @Override
    public @NotNull Component render(@NotNull Player player, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        return sourceDisplayName.color(RED)
                .append(Component.text(": "))
                .append(message.color(WHITE));
    }
}
