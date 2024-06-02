package ru.levkopo.plugins.mccisco.tasks.result;

import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UpgradeWorldBorder extends TaskResult{
    public final double size;

    public UpgradeWorldBorder(double size) {
        super(
                false,
                "границы будут расширены на "+size+" блоков"
        );
        this.size = size;
    }

    @Override
    public String getResultDescription(boolean negative) {
        return !negative ? "Границы расширены на "+size+" блоков благодаря ": "Границы не расширелись благодаря";
    }

    @Override
    public void run(Player player) {
        @NotNull WorldBorder worldBorder = player.getWorld().getWorldBorder();
        worldBorder.setSize(worldBorder.getSize() + this.size, 20);
    }
}
