package kr.hqservice.ticket.extension

import org.bukkit.entity.Player

fun Player.sendMessages(vararg message: String?) {
    message.filterNotNull().forEach(::sendMessage)
}