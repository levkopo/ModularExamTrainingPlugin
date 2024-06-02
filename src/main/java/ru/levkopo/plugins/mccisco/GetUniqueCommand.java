package ru.levkopo.plugins.mccisco;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.levkopo.plugins.mccisco.tasks.result.GiveItemResult;

import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class GetUniqueCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
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

            player.sendMessage(Component.text("Что-то неведомое дало тебе уникальный предмет", GRAY).decorate(ITALIC));
        } else {
            sender.sendMessage("You must be a player!");
            return false;
        }

        return false;
    }
}