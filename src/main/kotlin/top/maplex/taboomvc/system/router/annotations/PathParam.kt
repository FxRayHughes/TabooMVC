package top.maplex.taboomvc.system.router.annotations

import top.maplex.taboomvc.system.router.data.ParamType

/**
 * 用于标记路由参数
 * @param value 参数名
 * @param type 参数类型 默认是从路径获取
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class PathParam(
    val value: String = "",
    val type: ParamType = ParamType.QUERY
)
