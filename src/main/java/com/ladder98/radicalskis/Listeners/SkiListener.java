package com.ladder98.radicalskis.Listeners;

import com.ladder98.radicalskis.Items.Poles;
import com.ladder98.radicalskis.Items.Skis;
import com.ladder98.radicalskis.RadicalSkis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class SkiListener implements Listener {

    private ArrayList<String> cooldown = new ArrayList<String>();

    @EventHandler
    public void onUseListener(PlayerInteractEvent event) {
        EquipmentSlot hand = event.getHand();
        Player player = event.getPlayer();
        ItemStack poles = Poles.getPole();
        ItemStack skis = Skis.getSkis();

        boolean handsAreNotNull = (player.getInventory().getItem(EquipmentSlot.HAND) != null
            && player.getInventory().getItem(EquipmentSlot.OFF_HAND) != null);
        boolean isMainHand = (EquipmentSlot.HAND == hand);


        if (handsAreNotNull) {
            ItemStack mainHandItem = player.getInventory().getItem(EquipmentSlot.HAND);
            ItemStack offHandItem = player.getInventory().getItem(EquipmentSlot.OFF_HAND);
            boolean hasPoles = (mainHandItem.equals(poles) && offHandItem.equals(poles));
            boolean hasSkis = player.getInventory().getItem(EquipmentSlot.FEET).equals(skis);
            Block block = player.getLocation().clone().subtract(0,1,0).getBlock();
            boolean isOnSnow = (block.getType().equals(Material.SNOW_BLOCK)
                    || block.getType().equals(Material.ICE));

            if (hasPoles && hasSkis && isOnSnow) {
                if (cooldown.contains(player.getName())) {
                    event.setCancelled(true);
                } else {
                    Vector playerDirection = player.getLocation().getDirection();
                    Vector newPlayerDirection = playerDirection.clone().multiply(1.5);
                    player.setVelocity(newPlayerDirection.setY(.1));
                    cooldown.add(player.getName());
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RadicalSkis.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            cooldown.remove(player.getName());
                        }
                    }, 10L);
                }
            }
        }
    }

}
