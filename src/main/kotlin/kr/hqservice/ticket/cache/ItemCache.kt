package kr.hqservice.ticket.cache

import org.bukkit.inventory.ItemStack
import java.util.UUID

class ItemCache {

    private val playerItems = mutableMapOf<UUID, MutableList<ItemStack>>()

    fun contains(uuid: UUID) = playerItems.containsKey(uuid)

    fun get(uuid: UUID) = playerItems[uuid]

    fun add(uuid: UUID, items: MutableList<ItemStack>) {
        playerItems[uuid] = items
    }

    fun remove(uuid: UUID) {
        playerItems.remove(uuid)
    }
}