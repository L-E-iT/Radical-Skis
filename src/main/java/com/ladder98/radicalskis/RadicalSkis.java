package com.ladder98.radicalskis;

import com.ladder98.radicalskis.Items.Poles;
import com.ladder98.radicalskis.Items.Skis;
import com.ladder98.radicalskis.Listeners.SkiListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public final class RadicalSkis extends JavaPlugin {

    ShapedRecipe skiRecipe = skiRecipe();
    ShapedRecipe poleRecipe = poleRecipe();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Enabled RadicalSkis ⛷");
        Bukkit.addRecipe(skiRecipe);
        Bukkit.addRecipe(poleRecipe);

        getServer().getPluginManager().registerEvents(new SkiListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling RadicalSkis ⛷");
    }

    public static RadicalSkis getPlugin() {
        return RadicalSkis.getPlugin(RadicalSkis.class);
    }

    // Make our Recipes
    public ShapedRecipe skiRecipe() {
        ItemStack skis = Skis.getSkis();
        NamespacedKey skiNSK = new NamespacedKey(this, "skis");
        ShapedRecipe skiRecipe = new ShapedRecipe(skiNSK, skis);

        // s = Stick, b = leather boot
        skiRecipe.shape("   ", " b ", "sss");
        skiRecipe.setIngredient('s', Material.STICK);
        skiRecipe.setIngredient('b', Material.LEATHER_BOOTS);

        return skiRecipe;
    }

    public ShapedRecipe poleRecipe() {
        ItemStack skis = Poles.getPole();
        NamespacedKey skiNSK = new NamespacedKey(this, "poles");
        ShapedRecipe skiRecipe = new ShapedRecipe(skiNSK, skis);

        // s = Stick, i = Iron Ingot
        skiRecipe.shape(" s ", " s ", " i ");
        skiRecipe.setIngredient('s', Material.STICK);
        skiRecipe.setIngredient('i', Material.IRON_INGOT);

        return skiRecipe;
    }

}
