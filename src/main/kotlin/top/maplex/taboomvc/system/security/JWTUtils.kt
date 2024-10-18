package top.maplex.taboomvc.system.security

import io.javalin.http.Context
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import taboolib.expansion.tool.RedisCache
import top.maplex.taboomvc.MainConfig
import top.maplex.taboomvc.system.permission.LoginUserData
import java.util.*
import java.util.concurrent.TimeUnit


object JWTUtils {

    private const val MILLIS_SECOND: Long = 1000
    private const val MILLIS_MINUTE = 60 * MILLIS_SECOND
    private const val MILLIS_MINUTE_TEN = 20 * 60 * 1000L

    /**
     * 令牌前缀
     */
    const val LOGIN_USER_KEY = "login_user_key"

    // 令牌自定义标识
    val header: String
        get() = MainConfig.config.getString("token.header")!!

    // 令牌秘钥
    val secret: String
        get() = MainConfig.config.getString("token.secret")!!

    // 令牌有效期（默认30分钟）
    val expireTime: Long
        get() = MainConfig.config.getLong("token.expireTime", 30 * MILLIS_MINUTE)


    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    fun getLoginUser(request: Context): LoginUserData? {
        // 获取请求携带的令牌
        val token = getToken(request)
        if (token.isNotEmpty()) {
            val claims = parseToken(token)
            // 解析对应的权限以及用户信息
            val uuid = claims[LOGIN_USER_KEY] as String?
            val userKey = getTokenKey(uuid!!)
            return RedisCache.getObject<LoginUserData>(userKey)
        }
        return null
    }

    /**
     * 设置用户身份信息
     */
    fun setLoginUser(loginUser: LoginUserData) {
        if (loginUser.token.isNotEmpty()) {
            refreshToken(loginUser)
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    fun refreshToken(loginUser: LoginUserData) {
        loginUser.loginTime = System.currentTimeMillis()
        loginUser.expireTime = loginUser.loginTime + expireTime * MILLIS_MINUTE
        // 根据uuid将loginUser缓存
        val userKey = getTokenKey(loginUser.token)
        RedisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES)
    }


    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private fun getToken(request: Context): String {
        var token = request.header(header) ?: error("获取 token - Header 失败")
        if (token.isNotEmpty() && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "")
        }
        return token
    }

    /**
     * 从令牌中获取数据声明
     *
     *
     * @param token 令牌
     * @return 数据声明
     */
    private fun parseToken(token: String): Claims {
        return Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).body
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    fun getUsernameFromToken(token: String?): String {
        val claims = parseToken(token!!)
        return claims.subject
    }

    /**
     * 删除用户身份信息
     */
    fun delLoginUser(token: String) {
        if (token.isNotEmpty()) {
            val userKey = getTokenKey(token)
            RedisCache.del(userKey)
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    fun createToken(loginUser: LoginUserData): String {
        val token = UUID.randomUUID().toString()
        loginUser.token = token
        refreshToken(loginUser)
        val claims: MutableMap<String, Any> = HashMap()
        claims[LOGIN_USER_KEY] = token
        return createToken(claims)
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private fun createToken(claims: Map<String, Any?>): String {
        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, secret).compact()
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    fun verifyToken(loginUser: LoginUserData) {
        val expireTime: Long = loginUser.expireTime
        val currentTime = System.currentTimeMillis()
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser)
        }
    }

    private fun getTokenKey(uuid: String): String {
        return "login_tokens:$uuid"
    }

}
