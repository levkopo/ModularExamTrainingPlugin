package ru.levkopo.plugins.mccisco.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.levkopo.plugins.mccisco.tasks.TasksProvider;

public class StartTaskCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Server || sender.isOp()) {
            Player player = sender.getServer().getPlayer(args[0]);
            if(args.length > 1) {
                int taskNumber = Integer.parseInt(args[1]);
                if(TasksProvider.tasks.length > taskNumber) {
                    TasksProvider.startTask(player, TasksProvider.tasks[taskNumber]);
                    sender.sendMessage("ok");
                }else{
                    sender.sendMessage("invalid task id");
                }
            }else{
                TasksProvider.startRandomTask(player);
                sender.sendMessage("ok");
            }

            return true;
        }

        return false;
    }
}