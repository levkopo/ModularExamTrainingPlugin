package ru.levkopo.plugins.mccisco.tasks.result;

import net.kyori.adventure.text.Component;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import ru.levkopo.plugins.mccisco.ModularExamTrainingPlugin;

import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class TimeUpgradeWorldBorder extends TaskResult {
    public final double size;
    public final int days;

    public TimeUpgradeWorldBorder(double size, int days) {
        super(
                false,
                "границы будут расширены на " + size + " блоков"
        );
        this.size = size;
        this.days = days;
    }

    @Override
    public String getResultDescription(boolean negative) {
        return !negative ? "Границы расширены на " + size + " блоков благодаря " : "Границы не были расширены на " + size + " блоков благодаря ";
    }

    @Override
    public void run(Player player) {
        BukkitScheduler scheduler = player.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(ModularExamTrainingPlugin.instance, () -> {
            player.getServer().broadcast(Component.text("Ой, кажется мир сужается", RED));

            @NotNull WorldBorder worldBorder = player.getWorld().getWorldBorder();
            worldBorder.setSize(Math.max(worldBorder.getSize() - this.size, 1), 20);
        }, 24000L * days);

        player.getServer().broadcast(Component.text("Увы, расширение не вечное и мир вернется к предыдущему размеру через " + days + " игровых дня", YELLOW));
        @NotNull WorldBorder worldBorder = player.getWorld().getWorldBorder();
        worldBorder.setSize(Math.max(worldBorder.getSize() + this.size, 1), 20);
    }
}
