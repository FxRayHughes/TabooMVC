package top.maplex.taboomvc

import taboolib.platform.App
import top.maplex.taboomvc.utils.info

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("org.apache.logging.log4j.simplelog.StatusLogger.level", "OFF");
        App.env().group("top.maplex.taboomvc").version("6.2.0-beta20")
        Runtime.getRuntime().addShutdownHook(Thread {
            info("&c服务器即将关闭")
            App.shutdown()
        })
        info("开始准备初始化...")
        App.init()
        info("&3TabooMVC - 枫溪 - &f1.0.0 注：仅供参考")
    }


}
