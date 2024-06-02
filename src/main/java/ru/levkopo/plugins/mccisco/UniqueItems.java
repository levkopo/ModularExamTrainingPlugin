package ru.levkopo.plugins.mccisco;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import ru.levkopo.plugins.mccisco.tasks.result.GiveItemResult;

import java.util.Map;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class UniqueItems {
    public static final GiveItemResult.UniqueItem OLD_SWORD = new GiveItemResult.UniqueItem(
            Material.STONE_SWORD,
            "Мечь древнего добытчика",
            RED,
            Map.ofEntries(
                    Map.entry(Enchantment.LOOTING, 2),
                    Map.entry(Enchantment.PROTECTION, 2),
                    Map.entry(Enchantment.VANISHING_CURSE, 1)
            )
    );

    public static final GiveItemResult.UniqueItem DEFENDER_SWORD = new GiveItemResult.UniqueItem(
            Material.STONE_SWORD,
            "Мечь защитника Диплома",
            RED,
            Map.ofEntries(
                    Map.entry(Enchantment.SHARPNESS, 2),
                    Map.entry(Enchantment.FLAME, 1),
                    Map.entry(Enchantment.PROTECTION, 2),
                    Map.entry(Enchantment.VANISHING_CURSE, 1)
            )
    );

    public static final GiveItemResult.UniqueItem UNBREAKING_SHOVEL = new GiveItemResult.UniqueItem(
            Material.STONE_SHOVEL,
            "Прочная лопата из УрФУ",
            BLUE,
            Map.ofEntries(
                    Map.entry(Enchantment.UNBREAKING, 5),
                    Map.entry(Enchantment.VANISHING_CURSE, 1)
            )
    );

    public static final GiveItemResult.UniqueItem UNBREAKING_PICKAXE = new GiveItemResult.UniqueItem(
            Material.STONE_PICKAXE,
            "Прочная кирка из УрФУ",
            BLUE,
            Map.ofEntries(
                    Map.entry(Enchantment.UNBREAKING, 5),
                    Map.entry(Enchantment.VANISHING_CURSE, 1)
            )
    );

    public static final GiveItemResult.UniqueItem UNBREAKING_AXE = new GiveItemResult.UniqueItem(
            Material.IRON_SWORD,
            "Железный дровосек",
            DARK_GREEN,
            Map.ofEntries(
                    Map.entry(Enchantment.UNBREAKING, 2),
                    Map.entry(Enchantment.FORTUNE, 4),
                    Map.entry(Enchantment.VANISHING_CURSE, 1)
            )
    );

    public static final GiveItemResult.UniqueItem URTIBUR = new GiveItemResult.UniqueItem(
            Material.DIAMOND_PICKAXE,
            "УрТИБур",
            GOLD,
            Map.ofEntries(
                    Map.entry(Enchantment.FORTUNE, 4),
                    Map.entry(Enchantment.UNBREAKING, 8),
                    Map.entry(Enchantment.VANISHING_CURSE, 1)
            )
    );

    public static final GiveItemResult.UniqueItem URTIAXE = new GiveItemResult.UniqueItem(
            Material.DIAMOND_AXE,
            "УрТИНСИС",
            GOLD,
            Map.ofEntries(
                    Map.entry(Enchantment.FORTUNE, 4),
                    Map.entry(Enchantment.UNBREAKING, 8),
                    Map.entry(Enchantment.VANISHING_CURSE, 1)
            )
    );

    public static final GiveItemResult.UniqueItem STEPAN_SHOVEL = new GiveItemResult.UniqueItem(
            Material.DIAMOND_SHOVEL,
            "Лопата Степана для трупов",
            RED,
            Map.ofEntries(
                    Map.entry(Enchantment.EFFICIENCY, 5),
                    Map.entry(Enchantment.UNBREAKING, 3),
                    Map.entry(Enchantment.VANISHING_CURSE, 1)
            )
    );

    public static final GiveItemResult.UniqueItem STARTER_BOOK = new GiveItemResult.UniqueItem(
            Material.ENCHANTED_BOOK,
            "Гайд для первокурсника",
            BLUE,
            Map.ofEntries(
                    Map.entry(Enchantment.FORTUNE, 2),
                    Map.entry(Enchantment.UNBREAKING, 1)
            )
    );

    public static final GiveItemResult.UniqueItem STEPAN_JOURNAL = new GiveItemResult.UniqueItem(
            Material.ENCHANTED_BOOK,
            "Журнал 123 группы",
            BLUE,
            Map.ofEntries(
                    Map.entry(Enchantment.POWER, 4),
                    Map.entry(Enchantment.FLAME, 1)
            )
    );

    public static final GiveItemResult.UniqueItem CISCO_FIREWALL = new GiveItemResult.UniqueItem(
            Material.ENCHANTED_BOOK,
            "Firewall Cisco",
            BLUE,
            Map.ofEntries(
                    Map.entry(Enchantment.THORNS, 1),
                    Map.entry(Enchantment.BLAST_PROTECTION, 2)
            )
    );

    public static final GiveItemResult.UniqueItem MENDING_UNBREAKING_BOOK = new GiveItemResult.UniqueItem(
            Material.ENCHANTED_BOOK,
            "Прочная починка",
            GOLD,
            Map.ofEntries(
                    Map.entry(Enchantment.MENDING, 1),
                    Map.entry(Enchantment.UNBREAKING, 2)
            )
    );

    public static final GiveItemResult.UniqueItem EFFECTIVE_UNBREAKING_BOOK = new GiveItemResult.UniqueItem(
            Material.ENCHANTED_BOOK,
            "Эффективная прочность",
            GOLD,
            Map.ofEntries(
                    Map.entry(Enchantment.EFFICIENCY, 3),
                    Map.entry(Enchantment.UNBREAKING, 2)
            )
    );

    public static final GiveItemResult.UniqueItem GOLDEN_APPLE = new GiveItemResult.UniqueItem(
            Material.GOLDEN_APPLE,
            "Небесное яблоко",
            GOLD,
            Map.ofEntries()
    );

    public static final GiveItemResult.UniqueItem GOLDEN_CARROT = new GiveItemResult.UniqueItem(
            Material.GOLDEN_CARROT,
            "Небесная морковь",
            GOLD,
            Map.ofEntries()
    );

    public static final GiveItemResult.UniqueItem[] SIMPLE_ITEMS = new GiveItemResult.UniqueItem[]{
            OLD_SWORD, DEFENDER_SWORD, STEPAN_JOURNAL, STARTER_BOOK, CISCO_FIREWALL
    };

    public static final GiveItemResult.UniqueItem[] MEDIUM_ITEMS = new GiveItemResult.UniqueItem[]{
            OLD_SWORD, DEFENDER_SWORD, STEPAN_JOURNAL, STARTER_BOOK, CISCO_FIREWALL,
            UNBREAKING_AXE, UNBREAKING_PICKAXE, EFFECTIVE_UNBREAKING_BOOK, UNBREAKING_SHOVEL
    };


    public static final GiveItemResult.UniqueItem[] ULTIMATE_ITEMS = new GiveItemResult.UniqueItem[]{
            UNBREAKING_AXE, UNBREAKING_PICKAXE, EFFECTIVE_UNBREAKING_BOOK, UNBREAKING_SHOVEL,
            URTIAXE, URTIBUR, GOLDEN_APPLE, GOLDEN_CARROT, STEPAN_SHOVEL, MENDING_UNBREAKING_BOOK
    };
}
