package top.maplex.taboomvc.system.router.event

import io.javalin.http.Context
import top.maplex.taboomvc.system.eventbus.ProxyEvent
import top.maplex.taboomvc.system.router.data.RouterControllerData
import top.maplex.taboomvc.system.router.data.RouterNodeData

data class RouterProcessingEvent(
    val controllerData: RouterControllerData,
    var routerNodeData: RouterNodeData,
    var context: Context
) : ProxyEvent() {

    val url: String
        get() = context.url()

}
