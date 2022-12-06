import java.io.File
fun main() {
    fun part1(input: String, window: Int): Int {
        return input.windowed(window).indexOfFirst { it.toSet().size == window } + window
    }

    // test if implementation meets criteria from the description, like:
    val testInput = File("src", "Day06_test.txt").readLines()
    testInput.forEach{println(part1(it, 4))}
    testInput.forEach{println(part1(it, 14))}

    val input = File("src", "Day06.txt").readText()
    println(part1(input, 4))
    println(part1(input, 14))
}
