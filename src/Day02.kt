fun main() {
    fun part1(input: List<String>): Int {
        val scoreMap = mapOf("X" to 1, "Y" to 2, "Z" to 3)
        val gameMap = mapOf("A Y" to 6, "B Z" to 6, "C X" to 6, "A X" to 3, "B Y" to 3, "C Z" to 3)

        var totalSum = 0
        for (game in input){
            totalSum += gameMap.getOrDefault(game, 0) + scoreMap.getOrDefault(game.split(" ")[1], 0)
        }
        return totalSum
    }

    fun part2(input: List<String>): Int {
        val scoreMap = mapOf("A" to 1, "B" to 2, "C" to 3)
        val scoreGameMap = mapOf("X" to 0, "Y" to 3, "Z" to 6)

        infix fun Int.modulo(modulus: Int): Int {
            val remainder = this % modulus
            return if (remainder >= 0) remainder else remainder + modulus
        }

        val gameRule = listOf("A", "B", "C")
        val dirMap = mapOf("X" to -1, "Y" to 0, "Z" to 1)
        var totalSum = 0

        for (game in input){
            val (elfTurn, turnRes) = game.split(" ")
            val idxNew = gameRule.indexOf(elfTurn)+dirMap.getOrDefault(turnRes, 0)
            val myTurn = gameRule[idxNew modulo gameRule.size]
            totalSum += scoreMap.getOrDefault(myTurn, 0) + scoreGameMap.getOrDefault(turnRes, 0)
        }
    return totalSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
