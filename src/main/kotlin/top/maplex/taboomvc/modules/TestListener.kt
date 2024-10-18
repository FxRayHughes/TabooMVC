package top.maplex.taboomvc.modules

import taboolib.common.platform.event.SubscribeEvent
import top.maplex.taboomvc.system.common.event.HTTPRequestInterceptionEvent
import top.maplex.taboomvc.system.router.event.RouterProcessingEvent
import top.maplex.taboomvc.utils.info

object TestListener {

    @SubscribeEvent
    fun test(event: HTTPRequestInterceptionEvent) {
        info("监听到： ${event.context.path()}")

    }

    @SubscribeEvent(level = 20)
    fun test(event: RouterProcessingEvent) {
        info("B-监听到： ${event.routerNodeData.type} = > ${event.routerNodeData.path}")

    }

}
