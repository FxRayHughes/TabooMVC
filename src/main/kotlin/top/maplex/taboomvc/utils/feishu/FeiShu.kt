package top.maplex.taboomvc.utils.feishu

import com.lark.oapi.Client
import top.maplex.taboomvc.MainConfig

val client by lazy {
    FeiShu.client
}

/**
 *  飞书的SDK
 */
object FeiShu {

    val appId: String
        get() = MainConfig.config.getString("feishu.app_id")!!
    val appSecret: String
        get() = MainConfig.config.getString("feishu.app_secret")!!

    val client: Client by lazy {
        Client.newBuilder(appId, appSecret).build()
    }


}
