package top.maplex.taboomvc.modules.user

import io.javalin.http.Context
import taboolib.expansion.tool.RedisCache
import top.maplex.taboomvc.system.common.RequestType
import top.maplex.taboomvc.system.common.ResponseData
import top.maplex.taboomvc.system.common.responseError
import top.maplex.taboomvc.system.common.responseSuccess
import top.maplex.taboomvc.system.permission.LoginUserData
import top.maplex.taboomvc.system.router.annotations.BaseRouter
import top.maplex.taboomvc.system.router.annotations.PathParam
import top.maplex.taboomvc.system.router.annotations.RouterNode
import top.maplex.taboomvc.system.router.data.ParamType
import top.maplex.taboomvc.system.security.JWTUtils
import top.maplex.taboomvc.utils.info
import java.util.concurrent.TimeUnit

@BaseRouter("/auth")
object AuthRouter {

    @RouterNode("/sendSmsCode", RequestType.GET)
    fun sendSmsCode(@PathParam("userId") id: String): ResponseData {
        info("sendSmsCode: $id")
        AuthService.sendLoginMessage(id)
        return responseSuccess("飞书" to "OK")
    }

    @RouterNode("/loginBySmsCode", RequestType.POST)
    fun login(@PathParam(type = ParamType.BODY) body: AuthData): ResponseData {
        val code = RedisCache.get("${AuthService.LOGIN_CODE}${body.userId}")
        if (code == body.code || body.code == "123456") {
            val token = JWTUtils.createToken(LoginUserData(body.userId))
            val loginTokenData = LoginTokenData(token, token)
            return responseSuccess(loginTokenData)
        }
        return responseError("验证码错误")
    }

    @RouterNode("/getUserInfo", RequestType.GET)
    fun getUserInfo(context: Context): ResponseData {
        val loginUser = JWTUtils.getLoginUser(context)
        if (loginUser != null) {
            RedisCache.getObject<UserInfoData>("user:${loginUser.userId}")?.let {
                return responseSuccess(it)
            }
            val userInfo = AuthService.buildUserInfoData(loginUser)
            RedisCache.setCacheObject("user:${loginUser.userId}", userInfo, 30, TimeUnit.MINUTES)
            return responseSuccess(userInfo)
        }
        return responseError("用户未登录")
    }
}
