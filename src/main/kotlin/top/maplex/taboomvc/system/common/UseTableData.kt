package top.maplex.taboomvc.system.common

data class UseTableData(
    val records: List<Any>,
    val current: Int,
    val size: Int,
    val total: Int
) {

    companion object {

        fun build(list: List<Any>, current: Int, size: Int): UseTableData {
            return UseTableData(list.page(current, size), current, size, list.size)
        }

        fun <T> List<T>.page(pageNumber: Int, pageSize: Int): List<T> {
            val overSize = if (this.size < pageSize) this.size else pageSize
            val startIndex = maxOf((pageNumber - 1) * overSize, 0)
            val endIndex = minOf(startIndex + overSize, size)
            return subList(startIndex, endIndex)
        }
    }
}
