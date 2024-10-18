package top.maplex.taboomvc.system.eventbus

import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.reflex.ClassMethod
import taboolib.library.reflex.ReflexClass
import top.maplex.taboomvc.utils.info
import java.util.concurrent.ConcurrentHashMap

@Awake
object EventManager : ClassVisitor(1) {

    private val handlers = ConcurrentHashMap<String, HandlerList>()

    fun getHandlers(event: String): HandlerList {
        return handlers.getOrPut(event) { HandlerList() }
    }

    fun callEvent(event: ProxyEvent) {
        val listeners = handlers[event.javaClass.name]
        listeners?.getRegisteredListeners()?.forEach {
            if (it.ignoreCancelled || !event.isCancelled()) {
                it.callEvent(event)
            }
        }
    }

    override fun getLifeCycle(): LifeCycle {
        return LifeCycle.ENABLE
    }

    override fun visit(method: ClassMethod, owner: ReflexClass) {
        if (method.isAnnotationPresent(SubscribeEvent::class.java)) {
            val annotation = method.getAnnotation(SubscribeEvent::class.java)
            method.parameter.firstOrNull()?.let { parameter ->
                try {
                    parameter.instance
                    val handlerList = getHandlers(parameter.name)
                    val findInstance = findInstance(owner)
                    if (findInstance != null) {
                        val level = annotation.enum("level", -1)
                        val ignoreCancelled = annotation.enum("ignoreCancelled", false)
                        handlerList.register(BaseListener(findInstance, method, level, ignoreCancelled))
                    }
                } catch (_: Exception) {

                }
            }

        }
    }


}
