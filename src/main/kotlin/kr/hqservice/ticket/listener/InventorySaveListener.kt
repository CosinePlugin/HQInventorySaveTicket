package kr.hqservice.ticket.listener

import kr.hqservice.ticket.HQInventorySaveTicket
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent

class InventorySaveListener(
    plugin: HQInventorySaveTicket
) : Listener {

    private val itemRepository = plugin.itemRepository
    private val itemCache = plugin.itemCache

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        val uuid = player.uniqueId

        if (!itemCache.contains(uuid)) return

        val items = itemCache.get(uuid) ?: return
        val playerInventory = player.inventory

        items.forEach(playerInventory::addItem)
        itemCache.remove(uuid)
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity as Player
        val playerInventory = player.inventory

        val inventorySaveTicket = itemRepository.getInventorySaveTicket()
        if (!playerInventory.containsAtLeast(inventorySaveTicket, 1)) return

        playerInventory.removeItem(inventorySaveTicket)

        val drops = event.drops

        val exceptedItems = itemRepository.getExceptedItems()
        val filterDrops = drops.filter { it in exceptedItems }
        val saveFilter = drops.filterNot { it in exceptedItems }

        itemCache.add(player.uniqueId, saveFilter.toMutableList())

        drops.clear()
        drops.addAll(filterDrops)
    }
}