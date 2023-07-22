package xyz.acrylicstyle.overdose;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OverdosePlugin extends JavaPlugin implements Listener {
    private static final Set<UUID> POTION = new HashSet<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        if (e.getItem().getType() != Material.POTION) return;
        PotionMeta meta = ((PotionMeta) e.getItem().getItemMeta());
        assert meta != null;
        PotionType type = meta.getBasePotionData().getType();
        if (type.getEffectType() == null) return;
        if (!POTION.contains(e.getPlayer().getUniqueId()) && !e.getPlayer().hasPotionEffect(PotionEffectType.POISON)) {
            POTION.add(e.getPlayer().getUniqueId());
            Bukkit.getScheduler().runTaskLater(this, () -> POTION.remove(e.getPlayer().getUniqueId()), 100);
            return;
        }
        if (e.getPlayer().hasPotionEffect(type.getEffectType())) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*10, 1));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 20*30, 1));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HARM, 0, 0));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20*20, 0));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*20, 1));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*20, 1));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*20, 1));
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20*30, 1));
        }
    }
}
