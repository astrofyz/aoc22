fun main(){
    class Valve(var flow: Int, var next: List<String>, var cost: Int, var open: Boolean, var time: Int)

    fun parseInput(input: List<String>): MutableMap<String, Valve> {
        val valves: MutableMap<String, Valve> = mutableMapOf()
        for (line in input){
            val name = line.split(" ")[1]
            val flow = line.filter { it.isDigit() }.toInt()
            val next = line.split("to")[1].filter { it.isUpperCase() }.chunked(2)
            valves[name] = Valve(flow, next, 0, false, 30)
        }
        return valves
    }

    fun computeDistances(start: String, valves: MutableMap<String, Valve>): MutableMap<String, Int> {
        var visitedPoints = mutableMapOf<String, Int>()
        var distances = mutableMapOf<String, Int>()
        var nextPoints = valves[start]?.next?.toMutableList()
        var curPos = start
        distances[curPos] = 0

        while (true) {
            if (nextPoints != null) {
                for (point in nextPoints){
                    if (point in visitedPoints) {continue} else if (distances.getOrDefault(point, 100000) > distances.getOrDefault(curPos, 100000) + 1 ){
                        distances[point] = distances.getOrDefault(curPos, 100000) + 1
                    }
                }
            }
            visitedPoints[curPos] = distances.getOrDefault(curPos, 100000)
            if (visitedPoints.size == valves.size){break}
            distances.remove(curPos)
            if (distances.size == 0){break}
            curPos = distances.filterValues { it == distances.values.min() }.keys.first()
            nextPoints = valves[curPos]?.next?.toMutableList()
            if (nextPoints != null) {
                nextPoints = nextPoints.filter { it !in visitedPoints }.toMutableList()
            }
        }

        visitedPoints.remove(start)
        for (point in visitedPoints.keys.toList()){
            if (valves[point]?.flow == 0) {visitedPoints.remove(point)}
            else visitedPoints[point] = visitedPoints.getOrDefault(point, 0) + 1
        }
        return visitedPoints
    }

    fun part1(input: List<String>){
        var valves = parseInput(input)

//        println(valves)
        var reducedGraphs = mutableMapOf<String, MutableMap<String, Int>>()

        valves.keys.forEach { reducedGraphs[it] = computeDistances(it, valves) }

        println(reducedGraphs)
        var valvesOpened = mutableListOf<String>()
        var pressureReleasedResult = mutableListOf<Int>()

        fun trace(start: String, time: Int, valvesOpened: MutableList<String>, pressureReleased: Int): Int{
            valvesOpened.add(start)
            var result = mutableListOf<Int>()
            if (time > 0){
                for (nextValve in reducedGraphs[start]?.keys!!){
                    if (nextValve !in valvesOpened){
                        println("$nextValve, $valvesOpened")
                        result.add(trace(nextValve, time- reducedGraphs[start]?.get(nextValve)!!, valvesOpened,
                           pressureReleased+(valves[start]?.flow ?: 0) *time))
                    }
                }
            }
            println(time)
            if (result.isEmpty()) {
                valvesOpened.remove(start);
                pressureReleasedResult.add(pressureReleased+(valves[start]?.flow ?: 0) *time.coerceIn(0, 30))
                return pressureReleased+(valves[start]?.flow ?: 0) *time.coerceIn(0, 30)
            }
            else {valvesOpened.remove(start); return pressureReleased}
        }

        println(trace("AA", 30, valvesOpened, 0))
        println(pressureReleasedResult)
        println(pressureReleasedResult.max())
    }

//    fun part2(){
//
//    }


    val testInput = readInput("Day16")
//    parseInput(testInput)
    part1(testInput)
//    part2(testInput)


    val input = readInput("Day16")
//    part1(Input)
//    part2(input)
}