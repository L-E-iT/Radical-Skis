package com.ladder98.radicalskis.Items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class Poles extends ItemStack{

    public static ItemStack getPole(){
        ItemStack pole = new ItemStack(Material.STICK);

        // Setting some MetaData flags to make these ~Radical 8)
        ItemMeta poleMeta = pole.getItemMeta();
        poleMeta.displayName(Component.text("Radical Ski Pole").color(NamedTextColor.AQUA));
        poleMeta.lore(Collections.singletonList(Component.text("You should probably have two of these?").color(NamedTextColor.GREEN)));
        poleMeta.addEnchant(Enchantment.DIG_SPEED,0,true);
        poleMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Sneaky fake enchantments

        pole.setItemMeta(poleMeta);

        return pole;
    }

}
