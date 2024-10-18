package top.maplex.taboomvc.modules.number

import taboolib.expansion.orm.DaoGetter
import top.maplex.taboomvc.system.common.ResponseData
import top.maplex.taboomvc.system.common.responseSuccess
import top.maplex.taboomvc.system.permission.RouterNeedLogin
import top.maplex.taboomvc.system.router.annotations.BaseRouter
import top.maplex.taboomvc.system.router.annotations.PathParam
import top.maplex.taboomvc.system.router.annotations.RouterNode
import top.maplex.taboomvc.system.router.data.ParamType

@BaseRouter("/number")
object NumberRouter {

    private val numberService by DaoGetter(NumberData::class.java, Long::class.java)

    @RouterNeedLogin
    @RouterNode("/getNumber")
    fun getNumber(@PathParam id: Long): ResponseData {
        return responseSuccess(numberService.queryForId(id))
    }

    @RouterNeedLogin
    @RouterNode("/addNumber")
    fun addNumber(@PathParam("number", ParamType.BODY) number: NumberData): ResponseData {
        return responseSuccess(numberService.create(number))
    }

    @RouterNeedLogin
    @RouterNode("/updateNumber")
    fun updateNumber(@PathParam("number", ParamType.BODY) number: NumberData): ResponseData {
        return responseSuccess(numberService.update(number))
    }

    @RouterNeedLogin
    @RouterNode("/deleteNumber")
    fun deleteNumber(@PathParam id: Long): ResponseData {
        return responseSuccess(numberService.deleteById(id))
    }
}
