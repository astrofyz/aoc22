fun main() {
    class Blizzard(var pos: Pair<Int, Int>, var step: Pair<Int, Int>){}

    fun parseBlizzards(input: List<String>): MutableList<Blizzard> {
        var Blizzards = mutableListOf<Blizzard>()

        for ((row, line) in input.withIndex()){
            for ((col, elem) in line.withIndex()){
                when (elem){
                    '>' -> Blizzards.add(Blizzard(Pair(row, col), Pair(0, 1)))
                    '<' -> Blizzards.add(Blizzard(Pair(row, col), Pair(0, -1)))
                    '^' -> Blizzards.add(Blizzard(Pair(row, col), Pair(-1, 0)))
                    'v' -> Blizzards.add(Blizzard(Pair(row, col), Pair(1, 0)))
                    else -> continue
                }
            }
        }
        return Blizzards
    }

    infix fun Int.cycle(size: Int): Int {
        return when (this) {
            0 -> size-1
            size -> 1
            else -> this
        }
    }


    fun part1(input: List<String>){
        val maxRow = input.size
        val maxCol = input[0].length
        println(maxCol)

        var Blizzards = parseBlizzards(input)

        var startPos = Pair(0, 1)
        var endPos = Pair(maxRow-2, maxCol-2)

        fun MutableList<Blizzard>.update(){
            for (bliz in this){
//                println("updating ${bliz.pos}")
                bliz.pos = Pair((bliz.pos.first + bliz.step.first) cycle (maxRow - 1),
                    (bliz.pos.second + bliz.step.second) cycle (maxCol - 1))
//                println("updated to ${bliz.pos}")
            }
        }

        println("starting blizzards points ${Blizzards.map { it.pos }.toList().groupingBy { it }.eachCount()}")

        var time = 0
        var possibleMoves = mutableListOf<Pair<Int, Int>>()
        possibleMoves.add(startPos)

        while (true){
            Blizzards.update()
//            println("Blizzards: ${Blizzards.map { it.pos }.toList().groupingBy { it }.eachCount()}")
            var closedPos = Blizzards.map { it.pos }.toSet()
            var currentMoves = possibleMoves.map { it }.toMutableList()

            for (pos in currentMoves){
                for (step in listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))){
                    if ((pos.first+step.first in 1..maxRow-2)&&(pos.second+step.second in 1..maxCol-2)){
                        possibleMoves.add(Pair(pos.first+step.first, pos.second+step.second))
                    }
                }
            }
            possibleMoves = possibleMoves.subtract(closedPos).toMutableList()
//            println(possibleMoves)
            if (endPos in possibleMoves) {println("found $time"); break}
            time ++
        }
        time += 2
        println("time to End first $time")

        time = 0
        startPos = Pair(maxRow-1, maxCol-2)
        endPos = Pair(1, 1)

        possibleMoves = mutableListOf<Pair<Int, Int>>()
        possibleMoves.add(startPos)

        while (true){
            Blizzards.update()
//            println("Blizzards: ${Blizzards.map { it.pos }.toList().groupingBy { it }.eachCount()}")
            var closedPos = Blizzards.map { it.pos }.toSet()
            var currentMoves = possibleMoves.map { it }.toMutableList()

            for (pos in currentMoves){
                for (step in listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))){
                    if ((pos.first+step.first in 1..maxRow-2)&&(pos.second+step.second in 1..maxCol-2)){
                        possibleMoves.add(Pair(pos.first+step.first, pos.second+step.second))
                    }
                }
            }
            possibleMoves = possibleMoves.subtract(closedPos).toMutableList()
//            println(possibleMoves)
            if (endPos in possibleMoves) {println("found $time"); break}
            time ++
        }
        time += 2
        println("time to start first $time")

        time = 0
        startPos = Pair(0, 1)
        endPos = Pair(maxRow - 2, maxCol - 2)

        possibleMoves = mutableListOf<Pair<Int, Int>>()
        possibleMoves.add(startPos)

        while (true){
            Blizzards.update()
//            println("Blizzards: ${Blizzards.map { it.pos }.toList().groupingBy { it }.eachCount()}")
            var closedPos = Blizzards.map { it.pos }.toSet()
            var currentMoves = possibleMoves.map { it }.toMutableList()

            for (pos in currentMoves){
                for (step in listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))){
                    if ((pos.first+step.first in 1..maxRow-2)&&(pos.second+step.second in 1..maxCol-2)){
                        possibleMoves.add(Pair(pos.first+step.first, pos.second+step.second))
                    }
                }
            }
            possibleMoves = possibleMoves.subtract(closedPos).toMutableList()
//            println(possibleMoves)
            if (endPos in possibleMoves) {println("found $time"); break}
            time ++
        }
        time += 2
        println("time to end second $time")



    }

    val testInput = readInput("Day24")
    part1(testInput)
}