package kr.hqservice.ticket.command

import kr.hqservice.ticket.HQInventorySaveTicket.Companion.prefix
import kr.hqservice.ticket.extension.sendMessages
import kr.hqservice.ticket.config.ItemConfig
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class AdminCommand(
    private val itemConfig: ItemConfig
) : CommandExecutor, TabCompleter {

    private val commandTabList = listOf("ticket", "give", "reload")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("$prefix 콘솔에서 실행할 수 없는 명령어입니다.")
            return true
        }
        val player: Player = sender
        if (args.isEmpty()) {
            printHelp(player, label)
            return true
        }
        checker(player, label, args)
        return true
    }

    private fun printHelp(player: Player, label: String) {
        player.sendMessages(
            "$prefix 인벤토리 세이브권 명령어 도움말",
            "",
            "$prefix /$label ticket : 인벤토리 세이브권을 설정합니다.",
            "$prefix /$label give : 인벤토리 세이브권을 지급받습니다.",
            "$prefix /$label reload : config.yml을 리로드합니다."
        )
    }

    private fun checker(player: Player, label: String, args: Array<out String>) {
        when (args[0]) {
            "ticket" -> setInventorySaveTicket(player)

            "give" -> getInventorySaveTicket(player)

            "reload" -> reload(player)

            else -> printHelp(player, label)
        }
    }

    private fun setInventorySaveTicket(player: Player) {
        val item = player.inventory.itemInMainHand.clone().apply { amount = 1 }
        if (item.type == Material.AIR) {
            player.sendMessage("$prefix 손에 아이템을 들어주세요.")
            return
        }
        itemConfig.setInventorySaveTicket(item)
        itemConfig.save()
        player.sendMessage("$prefix 인벤토리 세이브권을 설정하였습니다.")
    }

    private fun getInventorySaveTicket(player: Player) {
        val ticket = itemConfig.getInventorySaveTicket()
        player.inventory.addItem(ticket)
    }

    private fun reload(player: Player) {
        itemConfig.reload()
        player.sendMessage("$prefix config.yml을 리로드하였습니다.")
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String> {
        if (args.size <= 1) {
            return commandTabList
        }
        return emptyList()
    }
}