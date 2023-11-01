package kr.hqservice.ticket.config

import kr.hqservice.ticket.HQInventorySaveTicket
import kr.hqservice.ticket.extension.async
import org.bukkit.ChatColor
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

    private var mustSaveLore = mutableListOf<String>()

    private var exceptedSaveLore = mutableListOf<String>()

    fun load() {
        inventorySaveTicketItem = config.getItemStack("inventory-save-ticket")
        exceptedWorlds = config.getStringList("excepted-worlds")
        isSaveExp = config.getBoolean("save-exp")
        mustSaveLore = config.getStringList("must-save-lore")
        exceptedSaveLore = config.getStringList("excepted-save-lore")
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

    fun isMustSaveItem(itemStack: ItemStack): Boolean {
        return itemStack.getStripColorLore()?.containsAll(mustSaveLore) == true
    }

    fun isExceptedSaveItem(itemStack: ItemStack): Boolean {
        return itemStack.getStripColorLore()?.containsAll(exceptedSaveLore) == true
    }

    private fun ItemStack.getStripColorLore(): List<String>? {
        return itemMeta?.lore?.map { ChatColor.stripColor(it) }
    }
}