package top.maplex.taboomvc.system.permission

data class LoginUserData(
    var userId: String ,
    var token: String = "",
    var loginTime: Long = 0L,
    var expireTime: Long = 0L,
    var ipaddr: String = "",
    var loginLocation: String = "",
    var browser: String = "",
    var os: String = "",
    val permissions: MutableList<String> = mutableListOf(),
    var enable: Boolean = true
)
