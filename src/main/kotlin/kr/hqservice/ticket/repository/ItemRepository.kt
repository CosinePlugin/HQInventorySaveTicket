package kr.hqservice.ticket.repository

import kr.hqservice.ticket.HQInventorySaveTicket
import kr.hqservice.ticket.extension.async
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File

class ItemRepository(
    plugin: HQInventorySaveTicket
) {

    private companion object {
        const val path = "config.yml"
    }

    private var file: File
    private var config: YamlConfiguration

    init {
        val newFile = File(plugin.dataFolder, path)
        if (!newFile.exists() && plugin.getResource(path) != null) {
            plugin.saveResource(path, false)
        }
        file = newFile
        config = YamlConfiguration.loadConfiguration(file)
    }

    private var exceptedWorlds = mutableListOf<String>()
    private var inventorySaveTicketItem: ItemStack? = null

    fun load() {
        inventorySaveTicketItem = config.getItemStack("inventory-save-ticket")
        exceptedWorlds = config.getStringList("excepted-worlds")
    }

    fun save() {
        async {
            config.set("inventory-save-ticket", inventorySaveTicketItem)
            config.save(file)
        }
    }

    fun reload() {
        config.load(file)
        exceptedWorlds.clear()
        load()
    }

    fun isExceptedWorld(worldName: String) = exceptedWorlds.contains(worldName)

    fun getInventorySaveTicket() = inventorySaveTicketItem

    fun setInventorySaveTicket(item: ItemStack) {
        inventorySaveTicketItem = item
    }
}