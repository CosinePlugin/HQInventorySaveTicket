package kr.hqservice.ticket.listener

import kr.hqservice.ticket.HQInventorySaveTicket
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class InventorySaveListener(
    plugin: HQInventorySaveTicket
) : Listener {

    private val itemRepository = plugin.itemRepository
    private val exceptedItems get() = itemRepository.getExceptedItems()
    private val inventorySaveTicket get() = itemRepository.getInventorySaveTicket()

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity as Player
        val playerLocation = player.location
        val playerInventory = player.inventory

        if (inventorySaveTicket == null) return

        val drops = event.drops
        val inventorySaveTickets = drops.filterNotNull().filter { it.isSimilar(inventorySaveTicket) }

        if (inventorySaveTickets.isEmpty()) return

        event.keepInventory = true

        inventorySaveTickets.subtractAmount()
        playerLocation.dropExceptedItem(drops)

        drops.clear()

        playerInventory.removeExceptedItem()
    }

    private fun List<ItemStack>.subtractAmount() {
        first().amount -= 1
    }

    private fun Inventory.removeExceptedItem() {
        contents.filterNotNull().forEach { if (it.setAmountClone() in exceptedItems) { it.amount = 0 } }
    }

    private fun Location.dropExceptedItem(drops: List<ItemStack?>) {
        drops.filterNotNull().filter { it.setAmountClone() in exceptedItems }.forEach {
            world.dropItemNaturally(this, it)
        }
    }

    private fun ItemStack.setAmountClone(): ItemStack {
        return clone().apply { amount = 1 }
    }
}