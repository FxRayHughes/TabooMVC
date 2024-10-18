package top.maplex.taboomvc.system.eventbus

open class ProxyEvent {

    private var isCancelled = false

    var allowCancelled: Boolean = true

    fun getHandlers(): HandlerList {
        return EventManager.getHandlers(this::class.java.name)
    }

    fun isCancelled(): Boolean {
        return isCancelled
    }

    fun setCancelled(value: Boolean) {
        if (allowCancelled) {
            isCancelled = value
        } else {
            error("Event cannot be cancelled.")
        }
    }

    fun call(): Boolean {
        EventManager.callEvent(this)
        return isCancelled
    }

}
