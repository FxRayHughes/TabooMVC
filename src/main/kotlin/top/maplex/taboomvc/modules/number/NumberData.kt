package top.maplex.taboomvc.modules.number

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.sql.Date

@DatabaseTable(tableName = "number_data")
data class NumberData(
    @DatabaseField(generatedId = true)
    val id: Long? = null,
    @DatabaseField
    val type: String? = null,
    @DatabaseField
    val number: Long? = null,
    @DatabaseField
    val time: Date? = null,
    @DatabaseField
    val remark: String? = null
)
