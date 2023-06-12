package kr.hqservice.ticket.listener

import kr.hqservice.ticket.HQInventorySaveTicket
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack

class InventorySaveListener(
    plugin: HQInventorySaveTicket
) : Listener {

    private val itemRepository = plugin.itemRepository
    private val inventorySaveTicket get() = itemRepository.getInventorySaveTicket()

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity

        if (itemRepository.isExceptedWorld(player.world.name)) return
        if (inventorySaveTicket == null) return

        val inventorySaveTickets = event.drops.toList().filterNotNull().filter { it.isSimilar(inventorySaveTicket) }
        if (inventorySaveTickets.isEmpty()) return

        event.keepInventory = true
        event.drops.clear()

        inventorySaveTickets.subtractAmount()
    }

    private fun List<ItemStack>.subtractAmount() {
        first().amount -= 1
    }
}