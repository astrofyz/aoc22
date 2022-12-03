fun main() {
    fun Char.priority(): Int {
        return when (this.isUpperCase()){
            true -> this - 'A' + 27
            else -> this - 'a' + 1
        }
    }

    fun part1(input: List<String>): Int {
        val res = input.flatMap { rucksack ->
            rucksack.take(rucksack.length / 2).toSet() intersect rucksack.takeLast(rucksack.length / 2).toSet()
        }.sumOf { it.priority() }
        return res
    }

    fun part2(input: List<String>): Int {
        val res = input.chunked(3).flatMap { rucksack -> rucksack[0]
            .toSet()
            .intersect(rucksack[1].toSet())
            .intersect(rucksack[2].toSet()) }.sumOf { it.priority() }
        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
