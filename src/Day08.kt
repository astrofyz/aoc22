fun main() {
    infix fun Int.taller(trees: List<Int>): Boolean{
        return trees.all{ it < this}
    }

    infix fun Int.visibleTrees(trees: List<Int>): Int{
        if (this taller trees) return trees.size
        return trees.indexOfFirst{ it >= this} + 1
    }

    fun part1(input: List<String>): Int {
        val sideSize = input.size
        val forest = input.map { it.toList().map { tree -> tree.digitToInt() } }

        fun Int.ifVisible(row:Int, col:Int): Int{
            if ((row == 0)||(col == 0)||(row == sideSize-1)||(col == sideSize-1)) {
                return 1
            }
            else if ((this taller forest[row].slice(0..col-1))
                ||(this taller forest[row].slice(col+1..sideSize-1))
                ||(this taller forest.map { it[col] }.slice(0..row-1))
                ||(this taller forest.map { it[col] }.slice(row+1..sideSize-1))){
                return 1
            }
            return 0
        }
        var count = 0
        forest.mapIndexed { rowId, row -> row.mapIndexed { colId, tree -> count += tree.ifVisible(rowId, colId) }}
        return count
    }

    fun part2(input: List<String>): Int {
        val sideSize = input.size
        val forest = input.map { it.toList().map { tree -> tree.digitToInt() } }

        fun Int.scenicScore(row:Int, col:Int): Int{
            if ((row == 0)||(col == 0)||(row == sideSize-1)||(col == sideSize-1)) {
                return 0
            }
            val left = this visibleTrees forest[row].slice(0..col-1).reversed()
            val right = this visibleTrees forest[row].slice(col+1..sideSize-1)
            val up = this visibleTrees forest.map { it[col] }.slice(0..row-1).reversed()
            val down = this visibleTrees forest.map { it[col] }.slice(row+1..sideSize-1)

            return left*right*up*down
        }
        val res = forest.mapIndexed { rowId, row -> row.mapIndexed { colId, tree -> tree.scenicScore(rowId, colId) }.max()}.max()
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
