package kr.hqservice.ticket

import kr.hqservice.ticket.command.AdminCommand
import kr.hqservice.ticket.listener.InventorySaveListener
import kr.hqservice.ticket.config.ItemConfig
import org.bukkit.plugin.java.JavaPlugin

class HQInventorySaveTicket : JavaPlugin() {

    internal companion object {
        const val prefix = "§c[ HQInventorySaveTicket ]§f"
    }

    override fun onEnable() {
        val itemConfig = ItemConfig(this)
        itemConfig.load()

        server.pluginManager.registerEvents(InventorySaveListener(this, itemConfig), this)
        getCommand("inventorysaveticket")?.setExecutor(AdminCommand(itemConfig))
    }

    override fun onDisable() {

    }
}