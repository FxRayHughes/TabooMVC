package top.maplex.taboomvc.system.eventbus

class HandlerList {


    private val handlerSlots = ArrayList<BaseListener>()

    fun register(listener: BaseListener) {
        handlerSlots.add(listener)
        handlerSlots.sortBy { it.priority }
    }

    fun unregister(listener: BaseListener) {
        handlerSlots.remove(listener)
    }

    fun getRegisteredListeners(): List<BaseListener> {
        return handlerSlots
    }

}
