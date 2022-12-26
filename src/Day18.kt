fun main(){

    fun List<Int>.isAdjacent(other: List<Int>):Boolean{
        val diff = this.mapIndexed { index, i -> kotlin.math.abs(this[index] - other[index]) }.toList()
        return (diff.count{it==0} == 2)&&(diff.count{it==1} == 1)
    }


    fun part1(input: List<String>){
        val cubes = input.map { it.split(',').map { elem -> elem.toInt() } }
        val result = mutableListOf<Boolean>()

        for (i in 0 until cubes.size){
            for (j in i until cubes.size){
                result.add(cubes[i].isAdjacent(cubes[j]))
            }
        }
        println("part1 : ${input.size * 6 - 2*result.count { it }}")

    }

    fun part2(input: List<String>) {
        val cubes = input.map { it.split(',').map { elem -> elem.toInt() } }

        var allNeighbours = mutableSetOf<List<Int>>()

        for (cube in cubes){
            for (dir in listOf<Int>(-1, 1)){
                allNeighbours.add(listOf(cube[0]+dir, cube[1], cube[2]))
                allNeighbours.add(listOf(cube[0], cube[1]+dir, cube[2]))
                allNeighbours.add(listOf(cube[0], cube[1], cube[2]+dir))
            }
        }

        allNeighbours = allNeighbours.subtract(cubes) as MutableSet<List<Int>>

        val minList = IntRange(0, 2).map { k -> cubes.map { it[k] }.toList().min()}.toList()
        val maxList = IntRange(0, 2).map { k -> cubes.map { it[k] }.toList().max()}.toList()


        fun List<Int>.ifInside(): Boolean{
            val inside = mutableListOf<Int>()
            inside.add(cubes.map { (it[0] in (this[0] .. maxList[0]+3))&&(it[1] == this[1])&&(it[2] == this[2]) }.count { it })
            inside.add(cubes.map { (it[0] in (minList[0]-3 .. this[0]))&&(it[1] == this[1])&&(it[2] == this[2]) }.count { it })
            inside.add(cubes.map { (it[1] in (this[1] .. maxList[1]+3))&&(it[0] == this[0])&&(it[2] == this[2]) }.count { it })
            inside.add(cubes.map { (it[1] in (minList[1]-3 .. this[1]))&&(it[0] == this[0])&&(it[2] == this[2]) }.count { it })
            inside.add(cubes.map { (it[2] in (this[2] .. maxList[2]+3))&&(it[1] == this[1])&&(it[0] == this[0]) }.count { it })
            inside.add(cubes.map { (it[2] in (minList[2]-3 .. this[2]))&&(it[1] == this[1])&&(it[0] == this[0]) }.count { it })
            return inside.all { it > 0 }
        }

        val cubesInside = allNeighbours.filter { !it.ifInside() }

        var adjSides = 0
        for (cubeAir in cubesInside){
            adjSides += cubes.map { it.isAdjacent(cubeAir) }.toList().count { it == true }
        }

        println("part2 (wrong) $adjSides")

    }

    fun part2BFS(input: List<String>){
        val cubes = input.map { it.split(',').map { elem -> elem.toInt() } }

        val minList = IntRange(0, 2).map { k -> cubes.map { it[k] }.toList().min()}.toList()
        val maxList = IntRange(0, 2).map { k -> cubes.map { it[k] }.toList().max()}.toList()

        fun List<Int>.neighbours(): List<List<Int>> {
            return listOf(
                listOf(this[0]-1, this[1], this[2]),
                listOf(this[0]+1, this[1], this[2]),
                listOf(this[0], this[1]+1, this[2]),
                listOf(this[0], this[1]-1, this[2]),
                listOf(this[0], this[1], this[2]+1),
                listOf(this[0], this[1], this[2]-1)
            )
        }

        val queue = ArrayDeque(listOf(listOf(minList[0]-1, minList[1]-1, minList[2]-1)))
        val outside = mutableSetOf<List<Int>>()

        while (queue.isNotEmpty()){
            for (air in queue.removeFirst().neighbours().filter{(it !in cubes)}){
                if ((air[0] in minList[0]-1 .. maxList[0]+1) &&
                    (air[1] in minList[1]-1 .. maxList[1]+1) &&
                    (air[2] in minList[2]-1 .. maxList[2]+1) &&
                    (air !in outside)){
                    queue.add(air)
                    outside.add(air)
                }
            }
        }


        var adjSides = 0
        for (cubeAir in outside){
            adjSides += cubes.map { it.isAdjacent(cubeAir) }.toList().count { it == true }
        }
        println("part2: $adjSides")

    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18")
    part1(testInput)
    part2(testInput)
    part2BFS(testInput)
}