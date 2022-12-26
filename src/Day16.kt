fun main(){
    class Valve(var flow: Int, var next: List<String>)

    fun parseInput(input: List<String>): MutableMap<String, Valve> {
        val valves: MutableMap<String, Valve> = mutableMapOf()
        for (line in input){
            val name = line.split(" ")[1]
            val flow = line.filter { it.isDigit() }.toInt()
            val next = line.split("to")[1].filter { it.isUpperCase() }.chunked(2)
            valves[name] = Valve(flow, next)
        }
        return valves
    }

    fun computeDistances(start: String, valves: MutableMap<String, Valve>): MutableMap<String, Int> {
        val visitedPoints = mutableMapOf<String, Int>()
        val distances = mutableMapOf<String, Int>()
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
        val valves = parseInput(input)

        val reducedGraphs = mutableMapOf<String, MutableMap<String, Int>>()

        valves.keys.forEach { reducedGraphs[it] = computeDistances(it, valves) }

        val valvesOpened = mutableListOf<String>()
        val pressureReleasedResult = mutableListOf<Int>()

        fun trace(start: String, time: Int, valvesOpened: MutableList<String>, pressureReleased: Int): Int{
            valvesOpened.add(start)
            val result = mutableListOf<Int>()
            if (time > 0){
                for (nextValve in reducedGraphs[start]?.keys!!){
                    if (nextValve !in valvesOpened){
                        result.add(trace(nextValve, time- reducedGraphs[start]?.get(nextValve)!!, valvesOpened,
                           pressureReleased+(valves[start]?.flow ?: 0) *time))
                    }
                }
            }
            if (result.isEmpty()) {
                valvesOpened.remove(start)
                pressureReleasedResult.add(pressureReleased+(valves[start]?.flow ?: 0) *time.coerceIn(0, 30))
                return pressureReleased+(valves[start]?.flow ?: 0) *time.coerceIn(0, 30)
            }
            else {valvesOpened.remove(start); return pressureReleased}
        }

        println(trace("AA", 30, valvesOpened, 0))
//        println(pressureReleasedResult)
        println(pressureReleasedResult.max())
    }


    fun part2(input: List<String>){
        val valves = parseInput(input)

        val reducedGraphs = mutableMapOf<String, MutableMap<String, Int>>()

        reducedGraphs["AA"] = computeDistances("AA", valves)
        valves.keys.forEach { if (valves[it]?.flow!! > 0) {reducedGraphs[it] = computeDistances(it, valves)} }

        fun trace2(start: String, time: Int, valvesOpened: MutableList<String>, pressureReleased: Int, redGraphs: MutableMap<String, MutableMap<String, Int>>): Int{
            valvesOpened.add(start)
            val result = mutableListOf<Int>()
            if (time > 0) {
                for (nextValve in redGraphs[start]?.keys!!) {
                    if (nextValve !in valvesOpened) {
                        result.add(
                            trace2(
                                nextValve, time - reducedGraphs[start]?.get(nextValve)!!, valvesOpened,
                                pressureReleased + (valves[start]?.flow ?: 0) * time, redGraphs
                            )
                        )
                    }
                }
                if (result.isNotEmpty()) {
                    valvesOpened.remove(start)
                    return result.max() //+ (valves[start]?.flow ?: 0) * time.coerceIn(0, 30)
                } else {
                    return pressureReleased+(valves[start]?.flow ?: 0) *time.coerceIn(0, 30)
                }
            }
            else {valvesOpened.remove(start); return pressureReleased}
        }

//        println(trace2("AA", 30, valvesOpened, 0, reducedGraphs))

        fun combinationUtil(arr: MutableList<String>, data: MutableList<String>, start: Int,
                            end: Int, index: Int, r: Int, res: MutableList<Int>): Int {
            if (index == r) {
                val myValves = data
                val elValves = arr.subtract(data.toSet())

                val myGraph = mutableMapOf<String, MutableMap<String, Int>>()
                reducedGraphs.forEach { myGraph[it.key] = it.value.filterKeys { valve -> valve in myValves }.toMutableMap() }
                val elGraph = mutableMapOf<String, MutableMap<String, Int>>()
                reducedGraphs.forEach { elGraph[it.key] = it.value.filterKeys { valve -> valve in elValves }.toMutableMap() }

                res.add(trace2("AA", 26, mutableListOf<String>(), 0, myGraph) + trace2("AA", 26, mutableListOf<String>(), 0, elGraph))

                return res.max()
            }

            var i = start
            while (i <= end && end - i + 1 >= r - index) {
                data[index] = arr[i]
                combinationUtil(arr, data, i + 1, end, index + 1, r, res)
                i++
            }
            return res.max()
        }

        var part2answer = 0

        for (r in 1.. (reducedGraphs.keys.size - 1) / 2){
            val outputList  = MutableList(r) { " " }
            val n = reducedGraphs.keys.size
            val resR = combinationUtil(reducedGraphs.keys.subtract(listOf("AA")).toMutableList(), outputList, 0, n-2, 0, r, mutableListOf() )
            part2answer = maxOf(part2answer, resR)
        }

        println("part2: $part2answer")

    }


    val testInput = readInput("Day16_test")
    part1(testInput)
    part2(testInput)


    val input = readInput("Day16")
    part1(input)
    part2(input)
}