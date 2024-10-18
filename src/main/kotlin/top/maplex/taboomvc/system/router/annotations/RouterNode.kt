package top.maplex.taboomvc.system.router.annotations

import top.maplex.taboomvc.system.common.RequestType

/**
 * 用于标记路由节点
 * @param value 节点路径
 * @param type 请求类型
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class RouterNode(
    val value: String = "",
    val type: RequestType = RequestType.GET
)
