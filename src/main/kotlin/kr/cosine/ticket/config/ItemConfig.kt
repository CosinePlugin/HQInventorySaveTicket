package kr.cosine.ticket.config

import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import java.io.File

@Bean
class ItemConfig(
    private val plugin: Plugin
) {

    private val server = plugin.server

    private var file: File
    private var config: YamlConfiguration

    init {
        val path = "config.yml"
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
        server.scheduler.runTaskAsynchronously(plugin, Runnable {
            config.set("inventory-save-ticket", inventorySaveTicketItem?.clone())
            config.save(file)
        })
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
        return itemMeta?.lore?.map { ChatColor.stripColor(it) ?: it }
    }
}