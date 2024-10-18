package top.maplex.taboomvc.system.common

import io.javalin.Javalin
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import top.maplex.taboomvc.MainConfig
import top.maplex.taboomvc.system.common.RequestType.*
import top.maplex.taboomvc.system.common.event.HTTPRequestInterceptionEvent
import top.maplex.taboomvc.system.common.event.WebsocketRequestInterceptionEvent
import top.maplex.taboomvc.system.router.RouterManager
import top.maplex.taboomvc.system.router.event.RouterProcessingEvent
import top.maplex.taboomvc.utils.info

@Awake
object HttpServer {

    private val server: Javalin by lazy {
        Javalin.create {
            it.showJavalinBanner = false
            it.requestLogger.http { ctx, executionTimeMs ->
                info("&a${ctx.method()} &7- &8${ctx.path()} &7- &8${ctx.status()} &7- &8${executionTimeMs}ms")
                HTTPRequestInterceptionEvent(ctx, executionTimeMs).call()
            }
            it.requestLogger.ws { ws ->
                ws.onMessage { context ->
                    WebsocketRequestInterceptionEvent(context, ws).call()
                }
            }
        }
    }

    private val SERVER_PORT
        get() = MainConfig.config.getInt("server.port", 8080)

    @Awake(LifeCycle.ENABLE)
    fun init() {
        RouterManager.controllers.forEach { (_, controller) ->
            controller.nodes.forEach { node ->
                when (node.type) {
                    GET -> {
                        server.get("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }

                    POST -> {
                        server.post("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }

                    PUT -> {
                        server.put("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }

                    PATCH -> {
                        server.patch("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }

                    DELETE -> {
                        server.delete("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }

                    HEAD -> {
                        server.head("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }

                    OPTIONS -> {
                        server.options("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }
                }
                info("&a路由注册 &7- &8${controller.baseUrl}${node.path}")
            }
        }

        server.start(SERVER_PORT)
        info("&aHTTP 服务已启动 &7- &8${SERVER_PORT}")
    }


}
