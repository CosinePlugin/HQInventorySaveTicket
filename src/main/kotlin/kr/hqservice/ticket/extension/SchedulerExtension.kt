package kr.hqservice.ticket.extension

import org.bukkit.plugin.Plugin

internal fun Plugin.async(actionFunction: () -> Unit) {
    server.scheduler.runTaskAsynchronously(this, actionFunction)
}

internal fun Plugin.runTaskLater(delay: Int = 1, actionFunction: () -> Unit = {}) {
    server.scheduler.runTaskLater(this, actionFunction, delay.toLong())
}