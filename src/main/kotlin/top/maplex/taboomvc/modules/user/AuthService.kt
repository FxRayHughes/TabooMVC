package top.maplex.taboomvc.modules.user

import com.lark.oapi.service.contact.v3.model.GetUserReq
import com.lark.oapi.service.im.v1.model.CreateMessageReq
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody
import taboolib.expansion.tool.RedisCache
import top.maplex.taboomvc.system.permission.LoginUserData
import top.maplex.taboomvc.system.security.SecurityUtils
import top.maplex.taboomvc.utils.TimeUtils
import top.maplex.taboomvc.utils.feishu.FeiShu
import top.maplex.taboomvc.utils.info
import java.util.concurrent.TimeUnit

object AuthService {

    const val LOGIN_CODE = "login_code_"

    /**
     *  发送登录验证码
     */
    fun sendLoginMessage(userId: String) {
        // 生成一个验证码
        val code = SecurityUtils.generateRandomCode(6)
        val version = "1.0.2"
        RedisCache.setEx("${LOGIN_CODE}$userId", code, 5L, TimeUnit.MINUTES)
        info("sendLoginMessage: ${userId} - $code")

        val time = TimeUtils.getNowTimeStr()
        val req = CreateMessageReq.newBuilder()
            .receiveIdType("user_id")
            .createMessageReqBody(
                CreateMessageReqBody.newBuilder()
                    .receiveId(userId)
                    .msgType("interactive")
                    .content("{\"data\":{\"template_id\":\"AAq7RGuwYIzLp\",\"template_variable\":{\"code\":\"${code}\",\"sendtime\":\"${time}\"},\"template_version_name\":\"${version}\"},\"type\":\"template\"}")
                    .build()
            )
            .build()
        FeiShu.client.im().message().create(req)
    }

    /**
     * 构造 UserInfoData
     */
    fun buildUserInfoData(loginUserData: LoginUserData): UserInfoData {
        val req: GetUserReq = GetUserReq.newBuilder()
            .userId(loginUserData.userId)
            .userIdType("user_id")
            .departmentIdType("open_department_id")
            .build()
        val resp = FeiShu.client.contact().user().get(req).data.user

        return UserInfoData(loginUserData.userId).apply {
            userName = resp.name
            avatarData = resp.avatar
            departmentIds = resp.departmentIds.toMutableList()
        }
    }
}
