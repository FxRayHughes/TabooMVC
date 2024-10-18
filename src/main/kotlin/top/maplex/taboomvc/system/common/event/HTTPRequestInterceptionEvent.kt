package top.maplex.taboomvc.system.common.event

import io.javalin.http.Context
import top.maplex.taboomvc.system.eventbus.ProxyEvent

data class HTTPRequestInterceptionEvent(
    val context: Context,
    val executionTimeMs: Float
) : ProxyEvent()
