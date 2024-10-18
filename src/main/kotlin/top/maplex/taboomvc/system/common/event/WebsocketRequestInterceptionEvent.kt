package top.maplex.taboomvc.system.common.event

import io.javalin.websocket.WsConfig
import io.javalin.websocket.WsMessageContext
import top.maplex.taboomvc.system.eventbus.ProxyEvent

data class WebsocketRequestInterceptionEvent(
    val context: WsMessageContext,
    val wsConfig: WsConfig
) : ProxyEvent()
