package top.maplex.taboomvc

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.expansion.orm.EasyORM
import taboolib.expansion.tool.RedisCache
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.ConfigSection
import taboolib.module.database.getHost

/**
 *  和业务逻辑相关的Taboolib功能一定要注册在这里不要写道Main
 *  因为Main加载时Taboolib未加载所以会报错
 */
object MainConfig {

    @Config("settings.yml")
    lateinit var config: ConfigFile

    val json by lazy {
        val kotlinModule = KotlinModule.Builder()
            .configure(KotlinFeature.NullIsSameAsDefault, true)
            .build()
        JsonMapper.builder()
            .addModule(kotlinModule)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build()
    }

    @Awake(LifeCycle.ENABLE)
    fun init() {
        RedisCache.link(config)

        EasyORM.init(config.getHost("database.mysql"))
    }

    /**
     *  这里是Jackson
     */
    fun jsonMapper(): ObjectMapper {
        return json
    }

}
