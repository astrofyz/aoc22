import java.io.File

fun main() {

    fun parseMap(input: List<String>): Pair<MutableSet<Pair<Int, Int>>, MutableSet<Pair<Int, Int>>> {
        var groveMap = mutableSetOf<Pair<Int, Int>>()
        var blockedTiles = mutableSetOf<Pair<Int, Int>>()

        for ((row, line) in input.withIndex()){
            for ((col, elem) in line.withIndex()){
                when (elem){
                    '.' -> groveMap.add(Pair(row, col))
                    '#' -> {blockedTiles.add(Pair(row, col)); groveMap.add(Pair(row, col))}
                    else -> continue
                }
            }
        }
        return Pair(groveMap, blockedTiles)
    }

    fun part1(input: String){
        val (groveMap, blockedTiles) = parseMap(input.split("\n\n")[0].split("\n"))
        var regex = "\\d+|\\D+".toRegex()
        val path = regex.findAll(input.split("\n\n")[1]).map { it.value }.toList()

        var colMin = groveMap.filter { it.first == 0 }.map { it.second }.toList().min()
        var Pos = groveMap.filter { (it.first == 0)&&(it.second == colMin) }.take(1)[0]
        var facing = ">"

        var facingMapR = mapOf(">" to "v", "v" to "<", "<" to "^", "^" to ">")
        var facingMapL = mapOf(">" to "^", "^" to "<", "<" to "v", "v" to ">")
        var facingDir = mapOf(">" to Pair(0, 1), "v" to Pair(1, 0), "<" to Pair(0, -1), "^" to Pair(-1, 0))

        fun Pair<Int, Int>.move(step: String, facing: String): Pair<Pair<Int, Int>, String> {
//            println("move here $step $facing")
            var startPos = Pair(this.first, this.second)
            var nextPos = Pair(0, 0)
            repeat(step.toInt()) {
                nextPos = Pair(
                    startPos.first + (facingDir[facing]?.first ?: 0),
                    startPos.second + (facingDir[facing]?.second ?: 0))

                println("$nextPos, $startPos")

                if ((nextPos !in blockedTiles)&&(nextPos !in groveMap)){
                    when (facing){
                        ">" -> nextPos = Pair(this.first,
                            groveMap.filter { (it.first == this.first) }.map { it.second }.toList().min())
                        "<" -> nextPos = Pair(this.first,
                            groveMap.filter { (it.first == this.first) }.map { it.second }.toList().max())
                        "^" -> nextPos = Pair(groveMap.filter { (it.second == this.second) }.map { it.first }.toList().max(),
                            this.second)
                        "v" -> nextPos = Pair(groveMap.filter { (it.second == this.second) }.map { it.first }.toList().min(),
                            this.second).also { println("mia") }
                    }
                    println("next pos outside of field $nextPos")
                }
                if (nextPos in blockedTiles) return Pair(startPos, facing)
                else startPos = Pair(nextPos.first, nextPos.second)
            }
            return Pair(startPos, facing)
        }

        fun Pair<Int, Int>.update(step: String, facing: String): Pair<Pair<Int, Int>, String> {
//            println("update here $step, $facing")
            when (step){
                "R" -> return Pair(this, facingMapR[facing]?: ">") //.also { println("${facingMapR[facing]}") }
                "L" -> return Pair(this, facingMapL[facing]?: "<") //.also { println("${facingMapL[facing]}") }
                else -> return this.move(step, facing)
            }
        }

        for (step in path){
            println("$step, $facing")
            var curPair = Pos.update(step, facing)
            Pos = curPair.first
            facing = curPair.second
            println("$Pos, $facing")
        }

    }

    val testInput = File("src","Day22.txt").readText()
    part1(testInput)
//    for (line in testInput){
//        println(line)

}