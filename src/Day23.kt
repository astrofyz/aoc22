import kotlin.math.min

fun main() {

    class Elf(var pos: Pair<Int, Int>, var direction: String){}

    fun Elf.print(){
        println("pos = ${this.pos}; direction = ${this.direction}")
    }

    fun List<Elf>.printFull(){
        val minRow = this.map { it.pos.first }.min()
        val maxRow = this.map { it.pos.first }.max()
        val minCol = this.map { it.pos.second }.min()
        val maxCol = this.map { it.pos.second }.max()

        val positions = this.map { it.pos }.toList()

        for (i in minRow .. maxRow){
            var row = ""
            for (j in minCol..maxCol){
                when (Pair(i, j)){
                    in positions -> row+="#"
                    else -> row+="."
                }
            }
            println(row)
        }
        println((maxRow-minRow+1)*(maxCol-minCol+1)-this.size)
    }

    val nextDir = mapOf("N" to "S", "S" to "W", "W" to "E", "E" to "N")
    val step = mapOf("N" to Pair(-1, 0), "S" to Pair(1, 0), "E" to Pair(0, 1), "W" to Pair(0, -1),
        "NW" to Pair(-1, -1), "NE" to Pair(-1, 1), "SW" to Pair(1, -1), "SE" to Pair(1, 1))

    fun List<Elf>.update():Boolean{
        var proposedPositions = mutableListOf<Pair<Int, Int>>()
        var notMoved = 0

        for (Elf in this){
            var dir = Elf.direction
            for (i in 0..3){
                // check if there are Elves in adjacent positions:
                var allAdjacent = step.map { Pair(Elf.pos.first + it.value.first,
                    Elf.pos.second + it.value.second) }
                if (allAdjacent.toSet().intersect(this.map { it.pos }.toSet()).isEmpty()){
                    proposedPositions.add(Pair(Elf.pos.first, Elf.pos.second))
                    notMoved += 1
                    break
                }

                // find all possible neighbours in selected direction
                var neighbours = step.keys.filter { it.contains(dir) }
                    .map { dir -> Pair(Elf.pos.first + step.getOrDefault(dir, Pair(0, 0)).first,
                        Elf.pos.second + step.getOrDefault(dir, Pair(0, 0)).second) }
//                println("neighbours for ${Elf.pos} in $dir direction: $neighbours")
                // check if there are elves in proposed direction
//                println(neighbours.toSet())
//                println(this.map { it.pos }.toSet())
                if (neighbours.toSet().intersect(this.map { it.pos }.toSet()).isEmpty()){
                    // if proposed tiles are empty, add proposed position to list
                    proposedPositions.add(Pair(Elf.pos.first + step.getOrDefault(dir, Pair(0, 0)).first,
                        Elf.pos.second + step.getOrDefault(dir, Pair(0, 0)).second))
                    // change first-considered direction
                    break // not checking next proposal
                }
                else { // if there are elves on neighbouring tiles
                    dir = nextDir.getOrDefault(dir, "N")  // change direction
                    if (i == 3){proposedPositions.add(Pair(Elf.pos.first, Elf.pos.second))}
                }
//                println("here")
//                proposedPositions.add(Pair(Elf.pos.first, Elf.pos.second))
            }
//        println(proposedPositions)
            // finished first stage
        }
        for ((idElf, propPos) in proposedPositions.withIndex()){
            if (proposedPositions.count { it == propPos } == 1){
                this[idElf].pos = Pair(propPos.first, propPos.second)
            }
        }
        for (Elf in this) {Elf.direction = nextDir.getOrDefault(Elf.direction, "N")}
        return notMoved == this.size
    }

    fun part1(input: List<String>){
        var Elves = mutableListOf<Elf>()

        for ((row, line) in input.withIndex()){
            for ((col, elem) in line.withIndex() ){
                if (elem == '#') Elves.add(Elf(pos = Pair(row, col), direction = "N"))
            }
        }
        repeat(10){
            Elves.update()
            Elves.printFull()
        }
    }

    fun part2(input: List<String>){
        var Elves = mutableListOf<Elf>()

        for ((row, line) in input.withIndex()){
            for ((col, elem) in line.withIndex() ){
                if (elem == '#') Elves.add(Elf(pos = Pair(row, col), direction = "N"))
            }
        }

        var iter = 1
        while (true){
            if (Elves.update()){
                println(iter)
                break
            }
            println(iter)
            iter++
        }
    }

    val testInput = readInput("Day23")

    part2(testInput)
}