package top.maplex.taboomvc.system.router.data

import io.javalin.http.Context
import io.javalin.http.UploadedFile
import io.javalin.http.util.CookieStore
import taboolib.common.inject.ClassVisitor
import taboolib.library.reflex.ClassMethod
import taboolib.library.reflex.LazyAnnotatedClass
import taboolib.library.reflex.ReflexClass
import top.maplex.taboomvc.MainConfig
import top.maplex.taboomvc.system.common.RequestType
import top.maplex.taboomvc.system.permission.LoginUserData
import top.maplex.taboomvc.system.router.annotations.PathParam
import top.maplex.taboomvc.system.router.data.ParamType.*
import top.maplex.taboomvc.system.security.JWTUtils

data class RouterControllerData(
    val baseUrl: String = "/",
    val nodes: ArrayList<RouterNodeData> = arrayListOf()
)

data class RouterNodeData(
    val path: String = "",
    val type: RequestType,
    val method: ClassMethod,
    val owner: ReflexClass
) {

    // 这里记录参数的名称与方法里的名称映射 保证顺序正常
    private val params = ArrayList<PathParamData>()

    init {
        method.parameter.forEach {
            if (it.name == Context::class.java.name) {
                params += PathParamData("context", CONTEXT, it)
                return@forEach
            }
            if (it.name == LoginUserData::class.java.name) {
                params += PathParamData("loginUserData", LOGIN_USER_DATA, it)
                return@forEach
            }
            if (it.isAnnotationPresent(PathParam::class.java)) {
                val annotation = it.getAnnotation(PathParam::class.java)
                val value = annotation.enum("value", "")
                val type = annotation.enum("type", QUERY)
                params += PathParamData(value, type, it)
                return@forEach
            }
            params += PathParamData(it.name, QUERY, it)
        }
    }

    fun invoke(context: Context) {
        val values = mutableListOf<Any?>()
        params.forEach { (paramId, type, clazz) ->
            when (type) {
                PATH -> {
                    values.add(context.pathParam(paramId))
                }

                CONTEXT -> {
                    values.add(context)
                }

                QUERY -> {
                    // 判断参数是否为list类型
                    val get = clazz.getter.get()
                    if (get != null && get.isAssignableFrom(Collection::class.java)) {
                        values.add(context.queryParams(paramId))
                        return@forEach
                    }
                    if (get != null) {
                        if (get == String::class.java) {
                            values.add(context.queryParam(paramId))
                            return@forEach
                        }
                        if (get == Int::class.java) {
                            values.add(context.queryParam(paramId)?.toIntOrNull())
                            return@forEach
                        }
                        if (get == Long::class.java) {
                            values.add(context.queryParam(paramId)?.toLongOrNull())
                            return@forEach
                        }
                        if (get == Double::class.java) {
                            values.add(context.queryParam(paramId)?.toDoubleOrNull())
                            return@forEach
                        }
                        if (get == Boolean::class.java) {
                            values.add(context.queryParam(paramId)?.toBoolean())
                            return@forEach
                        }
                        return@forEach
                    }
                    values.add(context.queryParam(paramId))
                }

                BODY -> {
                    val get = clazz.getter.get()
                    if (get == ByteArray::class.java) {
                        values.add(context.bodyAsBytes())
                        return@forEach
                    }
                    if (get == String::class.java) {
                        values.add(context.body())
                        return@forEach
                    }
                    if (get != null) {
                        values.add(context.bodyAsClass(get))
                        return@forEach
                    }
                    error("Unsupported body type: $get")
                }

                FILE -> {
                    val get = clazz.getter.get() ?: return@forEach
                    if (get == UploadedFile::class.java && paramId.isNotEmpty()) {
                        values.add(context.uploadedFile(paramId))
                        return@forEach
                    }
                    if (get.isAssignableFrom(Collection::class.java) && paramId.isNotEmpty()) {
                        values.add(context.uploadedFiles(paramId))
                        return@forEach
                    }
                    if (get.isAssignableFrom(Collection::class.java) && paramId.isEmpty()) {
                        values.add(context.uploadedFiles())
                        return@forEach
                    }
                    //Map
                    if (get.isAssignableFrom(Map::class.java)) {
                        values.add(context.uploadedFileMap())
                        return@forEach
                    }
                    error("Unsupported file type: $get")
                }

                COOKIE -> {
                    val get = clazz.getter.get() ?: return@forEach
                    if (get == String::class.java) {
                        values.add(context.cookie(paramId))
                        return@forEach
                    }
                    if (get.isAssignableFrom(Map::class.java)) {
                        values.add(context.cookieMap())
                        return@forEach
                    }
                    if (get == CookieStore::class.java) {
                        values.add(context.cookieStore())
                        return@forEach
                    }
                }

                LOGIN_USER_DATA -> {
                    val loginUser = JWTUtils.getLoginUser(context)
                    if (loginUser != null) {
                        values.add(loginUser)
                    } else {
                        values.add(LoginUserData("null"))
                    }
                }
            }
        }
        val instance = ClassVisitor.findInstance(owner)
        val back = if (instance != null) {
            method.invoke(instance, *values.toTypedArray())
        } else {
            method.invokeStatic(*values.toTypedArray())
        }
        if (back != null && back !is Unit) {
            val toJsonString = MainConfig.jsonMapper().writeValueAsString(back)
            
            context.result(toJsonString)
        }
    }

}

data class PathParamData(
    val paramId: String,
    val type: ParamType,
    val clazz: LazyAnnotatedClass
)

enum class ParamType {

    /**
     * 上下文
     */
    CONTEXT,

    /**
     * 路径参数
     */
    PATH,

    /**
     * Get类型参数 (默认)
     */
    QUERY,

    /**
     * Post类型参数 - JSON
     */
    BODY,

    /**
     * Upload类型参数 - File
     */
    FILE,

    /**
     * Cookie类型参数
     */
    COOKIE,

    /**
     * LoginUserData类型参数
     */
    LOGIN_USER_DATA
}
