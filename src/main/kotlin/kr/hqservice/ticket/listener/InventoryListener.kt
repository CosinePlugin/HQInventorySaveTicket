package kr.hqservice.ticket.listener

import kr.hqservice.ticket.inventory.InventoryHolder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class InventoryListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        event.inventory.holder?.let {
            if (it is InventoryHolder) {
                it.onInventoryClick(event)
            }
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        event.inventory.holder?.let {
            if (it is InventoryHolder) {
                it.onInventoryClose(event)
            }
        }
    }
}