package kr.hqservice.ticket.inventory

import kr.hqservice.ticket.HQInventorySaveTicket.Companion.prefix
import kr.hqservice.ticket.repository.ItemRepository
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

class ExceptedItemSettingInventory(
    private val itemRepository: ItemRepository
) : InventoryHolder("제외될 아이템 설정", 6) {

    override fun init(inventory: Inventory) {
        itemRepository.getExceptedItems().forEachIndexed { index, item ->
            inventory.setItem(index, item)
        }
    }

    override fun onClose(event: InventoryCloseEvent) {
        val player = event.player as Player
        val contents = event.inventory.contents

        itemRepository.setExceptedItems(contents)
        itemRepository.save()

        player.sendMessage("$prefix 인벤토리 세이브에서 제외될 아이템이 설정되었습니다.")
    }
}