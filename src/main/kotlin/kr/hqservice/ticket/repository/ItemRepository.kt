package kr.hqservice.ticket.repository

import kr.hqservice.ticket.HQInventorySaveTicket
import kr.hqservice.ticket.extension.async
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.io.File

class ItemRepository(
    plugin: HQInventorySaveTicket
) {

    private companion object {
        const val path = "config.yml"
    }

    private val file = File(plugin.dataFolder, path)
    private val config = YamlConfiguration.loadConfiguration(file)

    private var inventorySaveTicketItem: ItemStack? = null

    private var exceptedItems = mutableListOf<ItemStack>()

    @Suppress("unchecked_cast")
    fun load() {
        inventorySaveTicketItem = config.getItemStack("inventory-save-ticket")

        if (config.contains("excepted-items")) {
            exceptedItems = config.getList("excepted-items") as MutableList<ItemStack>
        }
    }

    fun save() {
        async {
            config.set("inventory-save-ticket", inventorySaveTicketItem)
            config.set("excepted-items", exceptedItems)
            config.save(file)
        }
    }

    fun getInventorySaveTicket() = inventorySaveTicketItem

    fun setInventorySaveTicket(item: ItemStack) {
        inventorySaveTicketItem = item
    }

    fun getExceptedItems() = exceptedItems

    fun setExceptedItems(contents: Array<out ItemStack?>) {
        exceptedItems.clear()
        contents.filterNotNull().map { it.apply { amount = 1 } }.apply(exceptedItems::addAll)
    }
}