package ru.levkopo.plugins.mccisco.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.levkopo.plugins.mccisco.UniqueItems;
import ru.levkopo.plugins.mccisco.tasks.result.GiveItemResult;

import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class GiveUniqueItemCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Server || sender.isOp()) {
            Player player = sender.getServer().getPlayer(args[0]);
            switch (args[1]) {
                case "simple":
                    new GiveItemResult(UniqueItems.SIMPLE_ITEMS).run(player);
                    break;

                case "medium":
                    new GiveItemResult(UniqueItems.MEDIUM_ITEMS).run(player);
                    break;

                case "ultimate":
                    new GiveItemResult(UniqueItems.ULTIMATE_ITEMS).run(player);
                    break;
            }

            sender.sendMessage("ok");
            player.sendMessage(Component.text("Что-то неведомое дало тебе уникальный предмет", GRAY).decorate(ITALIC));
            return true;
        }

        return false;
    }
}