package work.fodor.life.domain.model


class World(val numberOfColumns: Int, val numberOfRows: Int) {
    var age = 0
    val cells: Array<BooleanArray> = Array(numberOfColumns) { BooleanArray(numberOfRows) }

    init {
        for (x in 0 until numberOfColumns) {
            for (y in 0 until numberOfRows) {
                cells[x][y]
            }
        }
    }

    override fun toString(): String {
        var retVal = ""
        for (x in 0 until numberOfColumns) {
            for (y in 0 until numberOfRows) {
                retVal += if (cells[x][y]) "■" else "□"
            }
            retVal += System.getProperty("line.separator")
        }
        return retVal
    }
}




