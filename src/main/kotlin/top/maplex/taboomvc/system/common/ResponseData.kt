package top.maplex.taboomvc.system.common

data class ResponseData(
    val code: String,
    val msg: String,
    val data: Any? = null
)

fun responseSuccess(data: Any? = null) = ResponseData("0000", "success", data)

fun responseSuccess(msg: String, data: Any? = null) = ResponseData("200", msg, data)

fun responseError(code: String, msg: String) = ResponseData(code, msg)

fun responseError(msg: String) = ResponseData("500", msg)
