package kr.cosine.ticket.listener

import kr.cosine.ticket.config.ItemConfig
import kr.hqservice.framework.bukkit.core.listener.Listener
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.util.UUID

@Listener
class InventorySaveListener(
    private val plugin: Plugin,
    private val itemConfig: ItemConfig
) {

    private val server = plugin.server

    private val inventoryMap = mutableMapOf<UUID, List<ItemStack>>()
    private val exp = mutableMapOf<UUID, Int>()

    private val inventorySaveTicket get() = itemConfig.getInventorySaveTicket()

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity
        val playerUniqueId = player.uniqueId

        if (itemConfig.isExceptedWorld(player.world.name)) return
        if (inventorySaveTicket == null) return

        val inventorySaveTickets = event.drops.filter { it?.isSimilar(inventorySaveTicket) == true }
        if (inventorySaveTickets.isEmpty()) {
            player.setInventoryMap {
                !itemConfig.isMustSaveItem(it).apply {
                    if (this) {
                        event.drops.remove(it)
                    }
                }
            }
            return
        }

        inventorySaveTickets.subtractAmount()
        player.setInventoryMap {
            itemConfig.isExceptedSaveItem(it).apply {
                if (!this) {
                    event.drops.remove(it)
                }
            }
        }
        // event.keepInventory = true

        if (itemConfig.isSaveExp) {
            event.droppedExp = 0
            exp[playerUniqueId] = player.totalExperience
        }
    }

    private fun Player.setInventoryMap(toAirFunction: (ItemStack) -> Boolean) {
        val clonedInventory = inventory.map {
            if (it == null || toAirFunction(it)) {
                ItemStack(Material.AIR)
            } else {
                it.clone()
            }
        }
        inventoryMap[uniqueId] = clonedInventory
    }

    private fun List<ItemStack>.subtractAmount() {
        first().amount -= 1
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        val inventory = inventoryMap.remove(player.uniqueId)
        val exp = exp.remove(player.uniqueId)
        server.scheduler.runTaskLater(plugin, Runnable {
            inventory?.forEachIndexed { slot, itemStack ->
                player.inventory.setItem(slot, itemStack)
            }
            if (exp != null) {
                player.totalExperience = 0
                player.level = 0
                player.exp = 0f
                player.giveExp(exp)
            }
        }, 1)
    }
}