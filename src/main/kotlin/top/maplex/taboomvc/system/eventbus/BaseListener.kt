package top.maplex.taboomvc.system.eventbus

import taboolib.common.platform.event.ProxyListener
import taboolib.library.reflex.ClassMethod
import java.lang.reflect.Method

data class BaseListener(val instance: Any, val method: ClassMethod, val priority: Int, val ignoreCancelled: Boolean) : ProxyListener {

    fun callEvent(event: ProxyEvent) {
        if (!event.isCancelled()) {
            method.invoke(instance, event)
        }
    }

}
