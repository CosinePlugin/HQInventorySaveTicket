package kr.cosine.ticket

import kr.cosine.ticket.config.ItemConfig
import kr.hqservice.framework.global.core.component.Component
import kr.hqservice.framework.global.core.component.HQModule

@Component
class HQInventorySaveTicketModule(
    private val itemConfig: ItemConfig
) : HQModule {

    override fun onEnable() {
        itemConfig.load()
    }
}