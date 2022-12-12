import kotlin.math.max

fun main() {

    fun parseInput(input: List<String>): Pair<MutableList<MutableList<Char>>, Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        var startPos = Pair(-1, -1)
        var endPos = Pair(-1, -1)
        var maze = input.mapIndexed{row, itRow -> itRow.toMutableList()
            .also { if ('S' in it) {startPos = Pair(row, it.indexOf('S'))}
                    if ('E' in it){endPos = Pair(row, it.indexOf('E'))}
            }
        }.toMutableList()
        maze[endPos.first][endPos.second] = 'z'
        return Pair(maze, Pair(startPos, endPos))
    }

    fun part1(input: List<String>){
        var (maze, startEnd) = parseInput(input)
        var (startPos, endPos) = startEnd

        var allPoints = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until maze.size){
            for (j in 0 until maze[0].size){
                allPoints.add(Pair(i, j))
            }
        }

        var visitedPoints = mutableMapOf<Pair<Int, Int>, Int>()
        var distances = mutableMapOf<Pair<Int, Int>, Int>()
        var nextPoints = mutableListOf(Pair(startPos.first-1, startPos.second),
            Pair(startPos.first+1, startPos.second),
            Pair(startPos.first, startPos.second-1),
            Pair(startPos.first, startPos.second+1))
            .filter { (it.first in 0..maze.size)&&(it.second in 0..maze[0].size) }
            .filter { maze[it.first][it.second] == 'a' }.toMutableList()
        var curPos = startPos
        distances[curPos] = 0

        fun MutableList<Pair<Int, Int>>.update(pos: Pair<Int, Int>): MutableList<Pair<Int, Int>> {
                return mutableListOf(Pair(pos.first-1, pos.second),
                Pair(pos.first+1, pos.second),
                Pair(pos.first, pos.second-1),
                Pair(pos.first, pos.second+1))
                .filter { (it.first in 0 until maze.size)&&(it.second in 0 until maze[0].size) }
                .filter { maze[it.first][it.second] - maze[pos.first][pos.second] <= 1 }.toMutableList()
        }

        while (true) {
            for (point in nextPoints){
                if (point in visitedPoints.keys) {continue}
                else if (distances.getOrDefault(point, 100000) > distances.getOrDefault(curPos, 100000) + 1 ){
                    distances[point] = distances.getOrDefault(curPos, 100000) + 1
                    }
                }
            visitedPoints[curPos] = distances.getOrDefault(curPos, 100000)
            if (visitedPoints.size == maze.size*maze[0].size){break}
            distances.remove(curPos)
            if (distances.size == 0){break}
            curPos = distances.filterValues { it == distances.values.min() }.keys.first()
            nextPoints = nextPoints.update(curPos)
            nextPoints = nextPoints.filter { it !in visitedPoints.keys }.toMutableList()
        }
        println("result part 1: ${visitedPoints[endPos]}")
    }


    fun part2(input: List<String>){
        var (maze, startEnd) = parseInput(input)
        var (_, startPos) = startEnd

        var allPoints = mutableListOf<Pair<Int, Int>>()
        for (i in 0..maze.size-1){
            for (j in 0..maze[0].size-1){
                allPoints.add(Pair(i, j))
            }
        }

        var visitedPoints = mutableMapOf<Pair<Int, Int>, Int>()
        var distances = mutableMapOf<Pair<Int, Int>, Int>()
        var nextPoints = mutableListOf(Pair(startPos.first-1, startPos.second),
            Pair(startPos.first+1, startPos.second),
            Pair(startPos.first, startPos.second-1),
            Pair(startPos.first, startPos.second+1))
            .filter { (it.first in 0..maze.size)&&(it.second in 0..maze[0].size) }
            .filter { maze[it.first][it.second] == 'z' }.toMutableList()
        var curPos = startPos
        distances[curPos] = 0

        fun MutableList<Pair<Int, Int>>.update(pos: Pair<Int, Int>): MutableList<Pair<Int, Int>> {
            return mutableListOf(Pair(pos.first-1, pos.second),
                Pair(pos.first+1, pos.second),
                Pair(pos.first, pos.second-1),
                Pair(pos.first, pos.second+1))
                .filter { (it.first in 0..maze.size-1)&&(it.second in 0..maze[0].size-1) }
                .filter { maze[pos.first][pos.second] - maze[it.first][it.second] <= 1 }.toMutableList()
        }

        while (true) {
            for (point in nextPoints){
                if (point in visitedPoints.keys) {continue}
                else if (distances.getOrDefault(point, 100000) > distances.getOrDefault(curPos, 100000) + 1 ){
                    distances[point] = distances.getOrDefault(curPos, 100000) + 1
                }
            }
            visitedPoints[curPos] = distances.getOrDefault(curPos, 100000)
            if (visitedPoints.size == maze.size*maze[0].size){break}
            distances.remove(curPos)
            if (distances.size == 0){break}
            curPos = distances.filterValues { it == distances.values.min() }.keys.first()
            nextPoints = nextPoints.update(curPos)
            nextPoints = nextPoints.filter { it !in visitedPoints.keys }.toMutableList()
        }
        println("part 2 answer: ${visitedPoints.filterKeys { maze[it.first][it.second] == 'a' }.values.min()}")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    part1(testInput)
    part2(testInput)


    val input = readInput("Day12")
    part1(input)
    part2(input)
}
