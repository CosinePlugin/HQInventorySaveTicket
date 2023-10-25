package kr.hqservice.ticket.listener

import kr.hqservice.ticket.config.ItemConfig
import kr.hqservice.ticket.extension.runTaskLater
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.util.UUID

class InventorySaveListener(
    private val plugin: Plugin,
    private val itemConfig: ItemConfig
) : Listener {

    private val exp = mutableMapOf<UUID, Int>()

    private val inventorySaveTicket get() = itemConfig.getInventorySaveTicket()

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity

        if (itemConfig.isExceptedWorld(player.world.name)) return
        if (inventorySaveTicket == null) return

        val inventorySaveTickets = event.drops.toList().filterNotNull().filter { it.isSimilar(inventorySaveTicket) }
        if (inventorySaveTickets.isEmpty()) return

        event.keepInventory = true
        event.drops.clear()

        if (itemConfig.isSaveExp) {
            event.droppedExp = 0
            exp[player.uniqueId] = player.totalExperience
        }
        inventorySaveTickets.subtractAmount()
    }

    private fun List<ItemStack>.subtractAmount() {
        first().amount -= 1
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        val exp = exp.remove(player.uniqueId) ?: return
        plugin.runTaskLater {
            player.level = 0
            player.exp = 0f
            player.giveExp(exp)
        }
    }
}