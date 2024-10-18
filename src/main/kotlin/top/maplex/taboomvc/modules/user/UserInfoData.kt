package top.maplex.taboomvc.modules.user

data class UserInfoData(
    val userId: String,
    var userName: String = "",
    var roles: MutableList<String> = mutableListOf(),
    var buttons: MutableList<String> = mutableListOf(),
    var avatarData: Any? = null,
    var departmentIds: MutableList<Any> = mutableListOf(),
)
