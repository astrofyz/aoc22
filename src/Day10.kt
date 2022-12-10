fun main() {
    fun Int.isInteresting():Int{
        if (this in listOf(20, 60, 100, 140, 180, 220)){
            return 1
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        var cycle = 0
        var X = 1
        var res = 0
        for (line in input){
            if (line.startsWith("noop")){
                cycle += 1
                res += cycle.isInteresting()*cycle*X
                }
            else{
                repeat(2){
                    cycle += 1
                    res += cycle.isInteresting()*cycle*X
                }
                X += line.split(" ")[1].toInt()
            }
        }
        return res
    }

    fun part2(input: List<String>): Int {
        val CRT = MutableList(6, { MutableList(40, {0}) })
        var row: Int
        var col: Int
        var cycle = 0
        var X = 1
        for (line in input){
            if (line.startsWith("noop")){
                row = cycle / 40
                col = cycle % 40
                cycle += 1
                if (kotlin.math.abs(X-col) <= 1){
                    CRT[row][col] += 1
                }
            }
            else{
                repeat(2) {
                    row = cycle / 40
                    col = cycle % 40
                    cycle += 1
                    if (kotlin.math.abs(X - col) <= 1) {
                        CRT[row][col] += 1
                    }
                }
                X += line.split(" ")[1].toInt()
            }
        }
        for (line in CRT){
            println(line.map { when(it){
                0 -> "."
                else -> "#"
            } }.joinToString(separator = ""))

        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val input = readInput("Day10")
    check(part1(testInput) == 13140)
    println(part1(input))
    part2(input)

}