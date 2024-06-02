package ru.levkopo.plugins.mccisco.tasks.result;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DowngradeWorldBorder extends TaskResult {
    public final double size;

    public DowngradeWorldBorder(double size) {
        super(
                true,
                "границы будут сужены на "+size+" блоков"
        );
        this.size = size;
    }

    @Override
    public String getResultDescription(boolean negative) {
        return negative ? "Границы сужены на "+size+" блоков благодаря ": "Границы не уменьшились благодаря ";
    }

    @Override
    public void run(Player player) {
        @NotNull WorldBorder worldBorder = player.getWorld().getWorldBorder();
        worldBorder.setSize(Math.max(worldBorder.getSize() - this.size, 1), 20);
    }
}
