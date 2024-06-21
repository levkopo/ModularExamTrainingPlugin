package ru.levkopo.plugins.mccisco.tasks;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.levkopo.plugins.mccisco.UniqueItems;
import ru.levkopo.plugins.mccisco.tasks.dynamic.*;
import ru.levkopo.plugins.mccisco.tasks.result.DowngradeWorldBorder;
import ru.levkopo.plugins.mccisco.tasks.result.GiveItemResult;
import ru.levkopo.plugins.mccisco.tasks.result.TimeUpgradeWorldBorder;
import ru.levkopo.plugins.mccisco.tasks.result.UpgradeWorldBorder;
import ru.levkopo.plugins.mccisco.utils.RandomUtil;

import java.util.Objects;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class TasksProvider {
    public final static Task[] tasks = new Task[]{
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
            new WhatIsCableTypeTask(),
            new ShortcutMaskToFullTask(),
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
            new HostnameSetupTask(),
            new DomainNameSetupTask(),
            new CableLabelQuestionsSetupTask(),
            new IPAddressSetupTask(),
            new LACPSetupTask(),
            new PAgPSetupTask(),
            new ConsoleSetupTask(),
            new SSHSetupTask(),
            new TelnetSetupTask(),
    };

    public static @Nullable Task task = null;

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
        startTask(player, RandomUtil.fromArray(tasks));
    }
}
