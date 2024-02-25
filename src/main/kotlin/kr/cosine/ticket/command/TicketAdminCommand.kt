package kr.cosine.ticket.command

import kr.cosine.ticket.config.ItemConfig
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(label = "인벤세이브권관리", isOp = true)
class TicketAdminCommand(
    private val itemConfig: ItemConfig
) {

    @CommandExecutor("설정", "손에 든 아이템을 인벤세이브권으로 설정합니다.", priority = 1)
    fun setInventorySaveTicket(player: Player) {
        val item = player.inventory.itemInMainHand.clone().apply { amount = 1 }
        if (item.type == Material.AIR) {
            player.sendMessage("§c손에 아이템을 들어주세요.")
            return
        }
        itemConfig.setInventorySaveTicket(item)
        itemConfig.save()
        player.sendMessage("§a인벤토리 세이브권을 설정하였습니다.")
    }

    @CommandExecutor("지급", "인벤세이브권을 지급받습니다.", priority = 2)
    fun giveInventorySaveTicket(player: Player) {
        val ticket = itemConfig.getInventorySaveTicket() ?: run {
            player.sendMessage("§c인벤세이브권이 설정되어 있지 않습니다.")
            return
        }
        player.inventory.addItem(ticket)
        player.sendMessage("§a인벤세이브권이 지급되었습니다.")
    }

    @CommandExecutor("리로드", "config.yml을 리로드합니다.", priority = 3)
    fun reload(sender: CommandSender) {
        itemConfig.reload()
        sender.sendMessage("§aconfig.yml을 리로드하였습니다.")
    }
}