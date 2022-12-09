fun main() {
    fun abs(a: Int, b: Int): Int{
        return maxOf(a, b) - minOf(a, b)
    }

    fun sign(a: Int): Int{
        if (a > 0) return 1
        else if (a < 0) return -1
        return 0
    }

    fun Pair<Int, Int>.move(moveDir: String):Pair<Int, Int>{
        return when (moveDir){
            "R" -> Pair(this.first, this.second+1)
            "L" -> Pair(this.first, this.second-1)
            "U" -> Pair(this.first+1, this.second)
            "D" -> Pair(this.first-1, this.second)
            else -> this
        }
    }

    fun Pair<Int, Int>.adjustTail(head: Pair<Int, Int>): Pair<Int, Int>{
        return when(Pair(abs(head.first, this.first), abs(head.second, this.second))){
            Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(0, 0) -> this
            else -> Pair(this.first+sign(head.first-this.first),
                this.second+sign(head.second - this.second))
        }
    }

    fun part1(input: List<String>): Int {
        var headPos = Pair(0, 0)
        var tailPos = Pair(0, 0)
        val tailVisited = mutableSetOf(tailPos)

        for (step in input){
            repeat(step.split(" ")[1].toInt()){
                headPos = headPos.move(step.split(" ")[0])
                tailPos = tailPos.adjustTail(headPos)
                tailVisited.add(tailPos)
            }
        }
        return tailVisited.size
    }

    fun part2(input: List<String>): Int {
        val knotsPos = MutableList(10) { Pair(0, 0) }
        val tailVisited = mutableSetOf(knotsPos[8])

        for (step in input) {
            val (directionOfStep, numberOfRepetition) = step.split(" ")
            repeat(numberOfRepetition.toInt()) {
                knotsPos[0] = knotsPos[0].move(directionOfStep)
                for (i in 1 .. 9){
                    knotsPos[i] = knotsPos[i].adjustTail(knotsPos[i-1])
                }
                tailVisited.add(knotsPos[9])
            }
        }
        return tailVisited.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)

    val input = readInput("Day09")
    println(part1(input))
    part2(testInput)

    val testInput2 = readInput("Day09_test2")
    check(part2(testInput2) == 36)
    println(part2(input))
}