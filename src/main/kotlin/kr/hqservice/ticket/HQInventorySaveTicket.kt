package kr.hqservice.ticket

import kr.hqservice.ticket.command.AdminCommand
import kr.hqservice.ticket.listener.InventoryListener
import kr.hqservice.ticket.listener.InventorySaveListener
import kr.hqservice.ticket.repository.ItemRepository
import org.bukkit.plugin.java.JavaPlugin

class HQInventorySaveTicket : JavaPlugin() {

    companion object {
        lateinit var plugin: HQInventorySaveTicket
            private set

        internal const val prefix = "§c[ HQInventorySaveTicket ]§f"
    }

    override fun onLoad() {
        plugin = this
    }

    lateinit var itemRepository: ItemRepository
        private set

    override fun onEnable() {
        itemRepository = ItemRepository(this)
        itemRepository.load()

        server.pluginManager.let {
            it.registerEvents(InventoryListener(), this)
            it.registerEvents(InventorySaveListener(this), this)
        }
        getCommand("inventorysaveticket")?.setExecutor(AdminCommand(itemRepository))
    }

    override fun onDisable() {

    }
}