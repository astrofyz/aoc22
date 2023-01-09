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

    fun part2(input: String){
        var size = 50

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
            var startPos = Pair(this.first, this.second)
            var nextPos = Pair(0, 0)
            var newFacing = facing
            repeat(step.toInt()) {
                nextPos = Pair(
                    startPos.first + (facingDir[facing]?.first ?: 0),
                    startPos.second + (facingDir[facing]?.second ?: 0))

                if ((nextPos !in blockedTiles)&&(nextPos !in groveMap)){
//                    println("original nextPos: $nextPos $facing")
                    when (facing){
                        ">" -> {
                            if (startPos.first in 0 .. (size-1) && startPos.second == (size*3-1)) {  // 6 -> 3
                                newFacing = "<"
                                nextPos = Pair((size*3-1) - startPos.first, (size*2-1))
                            }
                            if (startPos.first in size .. (size*2-1) && startPos.second == (size*2-1)) {  // 4 -> 6
                                newFacing = "^"
                                nextPos = Pair((size*1-1), startPos.first + size)
                            }
                            if (startPos.first in (size*2-1) .. (size*3-1) && startPos.second == (size*2-1)) {  // 3 -> 6
                                newFacing = "<"
                                nextPos = Pair((size*3-1) - startPos.first, (size*3-1))
                            }
                            if (startPos.first in (size*3)..(size*4-1) && startPos.second == (size*1-1)) {  // 1 -> 3
                                newFacing = "^"
                                nextPos = Pair((size*3-1), startPos.first - (size*2))
                            }
                        }
                        "<" -> {
                            if (startPos.first in 0 .. (size*1-1) && startPos.second == size) {  // 5 -> 2
                                newFacing = ">"
                                nextPos = Pair((size*3-1) - startPos.first, 0)
                            }
                            if (startPos.first in size .. (size*2-1) && startPos.second == size) { // 4 -> 2
                                newFacing = "v"
                                nextPos = Pair((size*3), startPos.first - size)
                            }
                            if (startPos.first in size*2 .. (size*3-1) && startPos.second == 0) {  // 2 -> 5
                                newFacing = ">"
                                nextPos = Pair((size*3-1) - startPos.first, size)
                            }
                            if (startPos.first in size*3..(size*4-1) && startPos.second == 0) {  // 1 -> 5
                                newFacing = "v"
                                nextPos = Pair(0, startPos.first - size*2)
                            }
                        }
                        "^" -> {
                            if (startPos.first == 0 && startPos.second in size .. (size*2-1)) {  // 5 -> 1
                                newFacing = ">"
                                nextPos = Pair(startPos.second + size*2, 0)
                            }
                            if (startPos.first == 0 && startPos.second in (size*2) .. (size*3-1)) { // 6 -> 1
                                newFacing = "^"
                                nextPos = Pair((size*4-1), startPos.second - size*2)
                            }
                            if (startPos.first == size*2 && startPos.second in 0 .. size-1) {  // 2 -> 4
                                newFacing = ">"
                                nextPos = Pair(startPos.second + size, size)
                            }
                        }
                        "v" -> {
                            if (startPos.first == size-1 && startPos.second in (size*2) .. (size*3-1)) {  // 6 -> 4
                                newFacing = "<"
                                nextPos = Pair(startPos.second - size, (size*2-1))
                            }
                            if (startPos.first == (size*3-1) && startPos.second in size .. (size*2-1)) { // 3 -> 1
                                newFacing = "<"
                                nextPos = Pair(startPos.second + size*2, size-1)
                            }
                            if (startPos.first == (size*4-1) && startPos.second in 0 .. size-1) {  // 1 -> 6
                                newFacing = "v"
                                nextPos = Pair(0, size*2 + startPos.second)
                            }
                        }
                    }
//                    println("recomputed nextPos: $nextPos $newFacing")

//                    println("next pos outside of field $nextPos")
                }
                if (nextPos in blockedTiles) return Pair(startPos, newFacing)
                else startPos = Pair(nextPos.first, nextPos.second)
            }
            return Pair(startPos, newFacing)
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
//            println("step $step, $facing")
            var curPair = Pos.update(step, facing)
            Pos = curPair.first
            facing = curPair.second
//            println("updated Pos $Pos, $facing")
        }
        println((Pos.first+1)*1000 + (Pos.second+1)*4)

    }

    val testInput = File("src","Day22.txt").readText()
//    part1(testInput)
    part2(testInput)
//    for (line in testInput){
//        println(line)

}