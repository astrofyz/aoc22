fun main() {
    fun part1(input: List<String>): Int {
        return input.map {elves -> elves.replace(',', '-').split('-').map { it.toInt() } }
            .count{edges -> (((edges[2] >= edges[0]) and (edges[1] >= edges[3] ))
                    or ((edges[0] >= edges[2]) and (edges[3] >= edges[1] )))}  // learned about .count in stream
//            .sumOf { edges ->
//                (((edges[2] >= edges[0]) and (edges[1] >= edges[3] ))
//                        or ((edges[0] >= edges[2]) and (edges[3] >= edges[1] ))).compareTo(false) }
    }

    fun part2(input: List<String>): Int {
        return input.map {elves -> elves.replace(',', '-').split('-').map { it.toInt() } }
            .count { edges -> ((edges[2] <= edges[1]) and (edges[0] <= edges[3]))}
    // learned short way to compare, felt a bit ashamed and amazed

//                (((edges[2] >= edges[0]) and (edges[3] >= edges[1] ) and (edges[1] >= edges[2]))
//                        or ((edges[0] >= edges[2]) and (edges[1] >= edges[3]) and (edges[3] >= edges[0]))
//                        or ((edges[2] >= edges[0]) and (edges[1] >= edges[3] ))
//                        or ((edges[0] >= edges[2]) and (edges[3] >= edges[1] )))
//                    .compareTo(false) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
