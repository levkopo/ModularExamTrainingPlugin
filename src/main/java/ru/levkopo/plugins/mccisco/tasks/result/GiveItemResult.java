package ru.levkopo.plugins.mccisco.tasks.result;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.security.SecureRandom;
import java.util.Map;

public class GiveItemResult extends TaskResult {
    public static final SecureRandom random = new SecureRandom();
    private final UniqueItem[] items;

    public GiveItemResult(UniqueItem[] items) {
        super(
                false,
                "игрок получит уникальный предмет"
        );
        this.items = items;
    }


    public static class UniqueItem {
        private final Material material;
        private final String name;
        private final TextColor color;
        private final Map<Enchantment, Integer> enchantments;

        public UniqueItem(
                Material material,
                String name,
                TextColor color,
                Map<Enchantment, Integer> enchantments
        ) {
            this.material = material;
            this.name = name;
            this.color = color;
            this.enchantments = enchantments;
        }
    }

    @Override
    public String getResultDescription(boolean negative) {
        return negative ? "Уникальный предмет не достоин " : "Границы не уменьшились благодаря ";
    }

    @Override
    public void run(Player player) {
        UniqueItem uniqueItem = items[random.nextInt(items.length)];
        ItemStack itemStack = new ItemStack(uniqueItem.material);
        ItemMeta meta = itemStack.getItemMeta();
        for (Map.Entry<Enchantment, Integer> entry : uniqueItem.enchantments.entrySet()) {
            meta.addEnchant(entry.getKey(), entry.getValue(), true);
        }

        meta.displayName(Component.text(uniqueItem.name, uniqueItem.color));
        itemStack.setItemMeta(meta);

        player.getInventory().addItem(itemStack);
    }
}
