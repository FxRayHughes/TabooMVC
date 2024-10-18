package top.maplex.taboomvc.system.router

import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.library.reflex.ClassMethod
import taboolib.library.reflex.ReflexClass
import top.maplex.taboomvc.system.common.RequestType
import top.maplex.taboomvc.system.router.annotations.BaseRouter
import top.maplex.taboomvc.system.router.annotations.RouterNode
import top.maplex.taboomvc.system.router.data.RouterControllerData
import top.maplex.taboomvc.system.router.data.RouterNodeData

/**
 *  用于注册路由组件
 */
@Awake
object RouterManager : ClassVisitor(1) {

    val controllers = HashMap<String, RouterControllerData>()

    override fun getLifeCycle(): LifeCycle {
        return LifeCycle.LOAD
    }

    override fun visitStart(clazz: ReflexClass) {
        val annotationPresent = clazz.structure.isAnnotationPresent(BaseRouter::class.java)
        if (annotationPresent) {
            val annotation = clazz.structure.getAnnotation(BaseRouter::class.java)
            val baseUrl = annotation.enum<String>("value")
            clazz.name?.let {
                controllers[it] = RouterControllerData(baseUrl)
            }
        }
    }

    override fun visit(method: ClassMethod, owner: ReflexClass) {
        val controller = controllers[owner.name] ?: return
        if (method.isAnnotationPresent(RouterNode::class.java)) {
            val annotation = method.getAnnotation(RouterNode::class.java)
            val path = annotation.enum<String>("value")
            val type = annotation.enum("type", RequestType.GET)
            controller.nodes += RouterNodeData(path, type, method, owner)
        }
    }

}
