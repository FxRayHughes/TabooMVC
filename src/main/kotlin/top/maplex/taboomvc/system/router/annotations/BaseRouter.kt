package top.maplex.taboomvc.system.router.annotations

/**
 * 用于标记路由控制器
 * @param value 控制器路径
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseRouter(
    val value: String = "",
//    val requestType: RequestType = RequestType.GET
)
