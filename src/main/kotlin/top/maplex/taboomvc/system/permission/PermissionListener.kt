package top.maplex.taboomvc.system.permission

import taboolib.common.platform.event.SubscribeEvent
import top.maplex.taboomvc.system.router.event.RouterProcessingEvent
import top.maplex.taboomvc.system.security.JWTUtils

object PermissionListener {

    @SubscribeEvent(level = 0)
    fun onNode(event: RouterProcessingEvent) {
        val method = event.routerNodeData.method
        val annotation = method.getAnnotationIfPresent(RouterNeedLogin::class.java)
        if (annotation != null) {
            val user = kotlin.runCatching { JWTUtils.getLoginUser(event.context) }.getOrNull()
            if (user == null) {
                event.context.result("需要登录")
                event.context.status(8888)
                event.setCancelled(true)
                return
            }
        }
//        判断权限 - 我写的太烂了，建议重写
//        val permission = method.getAnnotationIfPresent(RouterPermission::class.java)
//        if (permission != null) {
//            val user = JWTUtils.getLoginUser(event.context)
//            if (user == null) {
//                event.context.result("需要登录")
//                event.context.status(401)
//                event.setCancelled(true)
//                return
//            }
//            // 上文已经判断了是否登录
//            val permissionValue = permission.enum("value", "none")
//            val hasPermission = PermissionService.checkPermission(user.userId, permissionValue)
//            if (!hasPermission) {
//                event.context.result("没有权限 - $permissionValue")
//                event.context.status(403)
//                event.setCancelled(true)
//                return
//            }
//        }
        println("PermissionListener - ${event.url}")
    }
}
