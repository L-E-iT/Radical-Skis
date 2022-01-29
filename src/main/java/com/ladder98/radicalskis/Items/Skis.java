package com.ladder98.radicalskis.Items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class Skis extends ItemStack {

    public static ItemStack getSkis(){
        ItemStack skis = new ItemStack(Material.LEATHER_BOOTS);

        // Setting some MetaData flags to make these ~Radical 8)
        ItemMeta skisMeta = skis.getItemMeta();
        skisMeta.displayName(Component.text("Radical Skis").color(NamedTextColor.AQUA));
        skisMeta.lore(Collections.singletonList(Component.text("Wicked!").color(NamedTextColor.GREEN)));
        skisMeta.addEnchant(Enchantment.DIG_SPEED,0,true);
        skisMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Sneaky fake enchantments

        skis.setItemMeta(skisMeta);

        return skis;
    }


}
