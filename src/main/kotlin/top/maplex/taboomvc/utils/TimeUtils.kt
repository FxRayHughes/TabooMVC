package top.maplex.taboomvc.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    fun getNowTimeStr(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentTime = Date()
        return sdf.format(currentTime)
    }

}
