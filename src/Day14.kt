import java.io.File
fun main() {
    fun createWall(input: List<String>, xMax: Int, xMin: Int, yMax: Int, yMin: Int): MutableList<MutableList<Int>> {
        var wall = MutableList(yMax-yMin+1, { MutableList(xMax-xMin+1, {0}) })

        fun MutableList<MutableList<Int>>.paintRock(crd: Pair<String, String>): MutableList<MutableList<Int>> {
            val startPoint = crd.first.split(',').map { it.toInt() }.toMutableList()
            val endPoint = crd.second.split(',').map { it.toInt() }
            val dx = listOf<Int>((endPoint[0]-startPoint[0]).coerceIn(-1 .. 1), (endPoint[1]-startPoint[1]).coerceIn(-1 .. 1))
            while (startPoint != endPoint){
                this[startPoint[1]-yMin][startPoint[0]-xMin] = 1
                startPoint[0] += dx[0]
                startPoint[1] += dx[1]
            }
            this[endPoint[1]-yMin][endPoint[0]-xMin] = 1
            return this
        }

        for (rock in input){
            var vertices = rock.split(" -> ")
            vertices.zipWithNext().forEach {
                wall = wall.paintRock(it)
            }
        }
        return wall
    }

    fun part1(testInput: String): Int {
        val regex = "[0-9]+".toRegex()
        val matches = regex.findAll(testInput)
        val coords = matches.map { it.value }.toList()
        val xCoords = coords.filterIndexed { index, _ -> index % 2 == 0 }.toList()
        val yCoords = coords.filterIndexed { index, _ -> index % 2 == 1 }.toList()

        val xMax = maxOf(xCoords.maxOf { it.toInt() }, 500)
        val xMin = xCoords.minOf { it.toInt() }
        val yMax = yCoords.maxOf { it.toInt() }
        val yMin = minOf(yCoords.minOf { it.toInt() }, 0)

        var startX = 500 - xMin

        val wall = createWall(testInput.split("\n"), xMax, xMin, yMax, yMin)

        var countSand = 0

        var startPoint = Pair(startX, wall.map { it[startX] }.toList().indexOfFirst{it != 0} - 1)
        var point = startPoint
        println(startPoint)
        var flag = "fall" // fall, rest, abyss

        fun Pair<Int, Int>.makeStep(): Pair<Pair<Int, Int>, String>{
            val down = wall.getOrNull(this.second+1)?.getOrNull(this.first)
            when (down) {
                0 -> return Pair(Pair(this.first, this.second + 1), "fall")
                null -> return Pair(Pair(this.first, this.second), "abyss")
                1, 2 -> {
                    val left = wall.getOrNull(this.second + 1)?.getOrNull(this.first - 1)
                    val right = wall.getOrNull(this.second + 1)?.getOrNull(this.first + 1)
                    when (left) {
                        1, 2 -> {
                            when (right) {
                                1, 2 -> return Pair(this, "rest")
                                null -> return Pair(this, "abyss")
                                0 -> return Pair(Pair(this.first + 1, this.second + 1), "fall")
                            }
                        }

                        null -> return Pair(this, "abyss")
                        0 -> return Pair(Pair(this.first - 1, this.second + 1), "fall")
                    }
                }
            }
            return Pair(Pair(this.first, this.second), "fall")
        }


        while (flag != "abyss") {
            var (newPoint, flag) = point.makeStep()
            when (flag){
                "fall" -> {point = newPoint}
                "rest" -> {wall[point.second][point.first] = 2;
                    startPoint = Pair(startX, wall.map { it[startX] }.toList().indexOfFirst{it != 0} - 1)
                    point = startPoint
                    countSand++
                }
                "abyss" -> break
            }
        }

        for (row in wall){
            println(row.joinToString("")
                .replace('1', '#')
                .replace('2', 'o')
                .replace('0', '.'))
        }

        println(countSand)
        return 0
    }

    fun part2(input: List<String>): Int {
        var blockedPoints = mutableSetOf<Pair<Int, Int>>()

        fun MutableSet<Pair<Int, Int>>.paintRock(crd: Pair<String, String>): MutableSet<Pair<Int, Int>> {
            var startPoint = crd.first.split(',').map { it.toInt() }.toMutableList()
            var endPoint = crd.second.split(',').map { it.toInt() }
            var dx = listOf((endPoint[0]-startPoint[0]).coerceIn(-1 .. 1), (endPoint[1]-startPoint[1]).coerceIn(-1 .. 1))
            while (startPoint != endPoint){
                this.add(Pair(startPoint[0], startPoint[1]))
                startPoint[0] += dx[0]
                startPoint[1] += dx[1]
            }
            this.add(Pair(endPoint[0], endPoint[1]))
            return this
        }

        for (rock in input){
            var vertices = rock.split(" -> ")
            vertices.zipWithNext().forEach {
                blockedPoints = blockedPoints.paintRock(it)
            }
        }


        var yFloor = blockedPoints.sortedBy { it.second }.takeLast(1)[0].second + 2
        var startPoint = Pair(500, blockedPoints.filter{it.first == 500}.sortedBy { it.second }.take(1)[0].second-1)
        var point = startPoint
        var flag = "fall" // fall, rest

        fun Pair<Int, Int>.makeStep(): Pair<Pair<Int, Int>, String>{
            val down = Pair(this.first, this.second+1)
            val right = Pair(this.first+1, this.second+1)
            val left = Pair(this.first-1, this.second+1)

            for (direction in listOf(down, right, left)){
                when (direction) {
                    !in blockedPoints -> {
                        if (direction.second == yFloor) return Pair(this, "rest")
                        else return Pair(direction, "fall")
                    }
                    in blockedPoints -> continue
                }
            }
            return Pair(Pair(this.first, this.second), "rest")
        }

        var countSand = 0
        while (true) {
            var (newPoint, flag) = point.makeStep()
            when (flag){
                "fall" -> {point = newPoint}
                "rest" -> {
                    blockedPoints.add(newPoint)
                    startPoint = Pair(500, blockedPoints.filter{it.first == 500}.sortedBy { it.second }.take(1)[0].second-1)
                    point = startPoint
                    countSand++
                    if (newPoint == Pair(500, 0)) break
                }
            }
        }
    println(countSand)
    return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = File("src", "Day14_test.txt").readText()
    part1(testInput)
    part2(testInput.split("\n"))

    val input = File("src", "Day14.txt").readText()
    part1(input)
    part2(input.split("\n"))
}
