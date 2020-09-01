package xyz.acrylicstyle.overdose

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.collections.HashSet

class OverdosePlugin : JavaPlugin(), Listener {
    private val potion = HashSet<UUID>()

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler
    fun onPlayerItemConsume(e: PlayerItemConsumeEvent) {
        if (e.player.gameMode != GameMode.SURVIVAL) return
        if (e.item.type != Material.POTION) return
        val meta = e.item.itemMeta as PotionMeta
        val type = meta.basePotionData.type
        if (type.effectType == null) return
        if (!potion.contains(e.player.uniqueId) && !e.player.hasPotionEffect(PotionEffectType.POISON)) {
            potion.add(e.player.uniqueId)
            object: BukkitRunnable() {
                override fun run() {
                    potion.remove(e.player.uniqueId)
                }
            }.runTaskLater(this, 100L)
            return
        }
        if (e.player.hasPotionEffect(type.effectType!!)) {
            if (type == PotionType.SPEED) {
                e.player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20*10, 1))
            } else if (type == PotionType.JUMP) {
                e.player.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 20*10, 1))
            }
            e.player.addPotionEffect(PotionEffect(PotionEffectType.UNLUCK, 20*30, 0))
            e.player.addPotionEffect(PotionEffect(PotionEffectType.HARM, 0, 0))
            e.player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 20*20, 0))
            e.player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 20*10, 1))
            e.player.addPotionEffect(PotionEffect(PotionEffectType.POISON, 20*30, 1))
        }
    }
}