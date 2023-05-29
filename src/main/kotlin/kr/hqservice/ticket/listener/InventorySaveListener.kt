package kr.hqservice.ticket.listener

import kr.hqservice.ticket.HQInventorySaveTicket
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

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

        /*val inventorySaveTicket = itemRepository.getInventorySaveTicket() ?: return
        playerInventory.removeItem(inventorySaveTicket)*/
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity as Player
        val playerInventory = player.inventory

        val inventorySaveTicket = itemRepository.getInventorySaveTicket() ?: return
        if (!playerInventory.containsAtLeast(inventorySaveTicket, 1)) return

        val drops = event.drops

        // playerInventory.removeItem(inventorySaveTicket)
        val exceptedItems = itemRepository.getExceptedItems()
        val filterDrops = drops.filterNotNull().filter { it in exceptedItems }
        val saveFilter = drops.filterNotNull().filterNot { it in exceptedItems && !it.isSimilar(inventorySaveTicket) }

        val inventorySaveTickets = drops.filterNotNull().filter { it.isSimilar(inventorySaveTicket) }
        if (inventorySaveTickets.isNotEmpty()) {
            inventorySaveTickets.first().amount -= 1
            drops.clear()
        }

        itemCache.add(player.uniqueId, saveFilter.toMutableList())

        drops.addAll(filterDrops)
    }

    private fun Inventory.getAmount(item: ItemStack): Int {
        var amount = 0
        contents.filter { it != null && it.isSimilar(item) }.forEach { amount += it.amount }
        return amount
    }
}