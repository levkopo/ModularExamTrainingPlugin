package ru.levkopo.plugins.mccisco.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.levkopo.plugins.mccisco.tasks.TasksProvider;

import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class CancelTaskCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Server || sender.isOp()) {
            if (TasksProvider.task != null) {
                TasksProvider.task.getPlayer().sendMessage(Component.text("Что-то неведомое дало отменило твою задачу", GRAY).decorate(ITALIC));
                TasksProvider.task.done(false);
                TasksProvider.stopTask();
                sender.sendMessage("ok");
            }

            return true;
        }

        return false;
    }
}