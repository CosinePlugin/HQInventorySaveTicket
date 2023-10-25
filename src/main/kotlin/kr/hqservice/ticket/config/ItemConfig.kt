package kr.hqservice.ticket.config

import kr.hqservice.ticket.HQInventorySaveTicket
import kr.hqservice.ticket.extension.async
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.io.File

class ItemConfig(
    private val plugin: HQInventorySaveTicket
) {

    private val path = "config.yml"

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

    private var inventorySaveTicketItem: ItemStack? = null

    private var exceptedWorlds = mutableListOf<String>()

    var isSaveExp = true
        private set

    fun load() {
        inventorySaveTicketItem = config.getItemStack("inventory-save-ticket")
        exceptedWorlds = config.getStringList("excepted-worlds")
        isSaveExp = config.getBoolean("save-exp")
    }

    fun save() {
        plugin.async {
            config.set("inventory-save-ticket", inventorySaveTicketItem)
            config.save(file)
        }
    }

    fun reload() {
        config.load(file)
        exceptedWorlds.clear()
        load()
    }

    fun isExceptedWorld(worldName: String): Boolean = exceptedWorlds.contains(worldName)

    fun getInventorySaveTicket(): ItemStack? = inventorySaveTicketItem?.clone()

    fun setInventorySaveTicket(itemStack: ItemStack) {
        inventorySaveTicketItem = itemStack
    }
}