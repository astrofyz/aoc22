fun main() {
    fun part1(input: List<String>): Int {
        var mutInput: List<String> = input
        var maxCalorie = 0
        var elfSnacks: List<Int>
        var curCalorie: Int
        while (mutInput.isNotEmpty()){
            elfSnacks = mutInput.takeWhile { !it.isEmpty() }.map { it.toInt() }
            curCalorie = elfSnacks.sum()
            mutInput = mutInput.dropWhile { !it.isEmpty() }.dropWhile { it.isEmpty() }
            maxCalorie = listOf<Int>(maxCalorie, curCalorie).max()
        }
        return maxCalorie
    }

    fun part2(input: List<String>): Int {
        var mutInput: List<String> = input
        var elfSnacks: List<Int>
        val curCalorie = mutableListOf<Int>()
        while (mutInput.isNotEmpty()) {
            elfSnacks = mutInput.takeWhile { !it.isEmpty() }.map { it.toInt() }
            curCalorie.add(elfSnacks.sum())
            mutInput = mutInput.dropWhile { !it.isEmpty() }.dropWhile { it.isEmpty() }
        }
        curCalorie.sortDescending()
        return curCalorie.slice(0..2).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
