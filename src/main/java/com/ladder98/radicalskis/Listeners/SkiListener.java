package com.ladder98.radicalskis.Listeners;

import com.ladder98.radicalskis.Items.Poles;
import com.ladder98.radicalskis.Items.Skis;
import com.ladder98.radicalskis.RadicalSkis;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.ladder98.radicalskis.RadicalSkis.SKI_ENABLE;
import static com.ladder98.radicalskis.RadicalSkis.getPlugin;

public class SkiListener implements Listener {

    private Map<String, Double> activeSkiers = new HashMap<String, Double>();
    private Map<String, Long> lastSkied = new HashMap<String, Long>();

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
            boolean hasPoles = (Objects.equals(mainHandItem, poles)) && (Objects.equals(offHandItem, poles));
            boolean hasSkis = (Objects.equals(player.getInventory().getItem(EquipmentSlot.FEET), skis));
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            boolean isOnSnow = (block.getType().equals(Material.SNOW_BLOCK)
                    || block.getType().equals(Material.SNOW));
            boolean isCrouched = player.isSneaking();
            boolean canSki = queryRegionFlag(player);
            LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
            boolean canBypass = WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, localPlayer.getWorld());


            if (hasPoles && hasSkis && isOnSnow && player.isOnGround() && (canSki || canBypass)) {
                double baseSpeed = .25;
                double maxSpeed = .75;
                double altitudeGain = 0;

                if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)
                        || event.getAction().equals(Action.LEFT_CLICK_AIR)
                        || event.getAction().equals(Action.PHYSICAL)) {
                    event.setCancelled(true);
                } else if (activeSkiers.containsKey(player.getName())) {
                    lastSkied.put(player.getName(), System.currentTimeMillis());
                    Double currentSkierSpeed = activeSkiers.get(player.getName());
                    if (currentSkierSpeed < maxSpeed) {
                        currentSkierSpeed += .025;
                        Vector playerDirection = player.getLocation().getDirection();
                        Vector newPlayerDirection = playerDirection.clone().multiply(currentSkierSpeed);
                        player.setVelocity(newPlayerDirection.setY(altitudeGain));
                        player.setGravity(false);
                    } else {
                        Vector playerDirection = player.getLocation().getDirection();
                        Vector newPlayerDirection = playerDirection.clone().multiply(currentSkierSpeed);
                        player.setVelocity(newPlayerDirection.setY(altitudeGain));
                        player.setGravity(false);
                    }
                    activeSkiers.put(player.getName(), currentSkierSpeed);
                    createRunner(player);
                } else {
                    Vector playerDirection = player.getLocation().getDirection();
                    Vector newPlayerDirection = playerDirection.clone().multiply(baseSpeed);
                    player.setVelocity(newPlayerDirection.setY(altitudeGain));
                    player.setGravity(false);
                    activeSkiers.put(player.getName(), baseSpeed);
                    createRunner(player);
                }
            }

        }
    }

    private boolean queryRegionFlag(Player player) {

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        return query.testState(localPlayer.getLocation(), localPlayer, SKI_ENABLE);
    }

    private void createRunner(Player checkPlayer) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RadicalSkis.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Player player = Bukkit.getServer().getPlayer(checkPlayer.getName());
                if (player == null) {
                    activeSkiers.remove(checkPlayer.getName());
                } else if (player.isSneaking()) {
                    activeSkiers.remove(checkPlayer.getName());
                    player.setGravity(true);
                } else {
                    Block playerBlock = player.getLocation().getBlock();
                    if (playerBlock.getRelative(BlockFace.DOWN).getType() != Material.SNOW_BLOCK
                        && playerBlock.getRelative(BlockFace.DOWN).getType() != Material.SNOW
                            && playerBlock.getType() != Material.SNOW
                    ); {
                        player.setGravity(true);
                    }

                }
            }
        }, 2L);
    }

    private void stopSkiingRunner(Player checkplayer) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RadicalSkis.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (lastSkied.get(checkplayer.getName()) < System.currentTimeMillis() - 500) {
                    activeSkiers.remove(checkplayer.getName());
                    checkplayer.setGravity(true);
                }
            }
        }, 20L);
    }

}
