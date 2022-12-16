import java.awt.font.NumericShaper.Range

fun main() {
    fun readCoordinates(input: String): List<List<Int>> {
        val regex = "(-?\\d+)".toRegex()
        return regex.findAll(input).map { it.groups[1]?.value?.toInt() ?: 0 }.toList().chunked(2)
    }

    fun IntRange.myIntersect(other: IntRange): Boolean{
        return (this.start <= other.endInclusive)&&(other.start <= this.endInclusive)
    }

    fun IntRange.myAdjacent(other: IntRange): Boolean{
        return (this.last + 1)==other.first
    }

    fun part1(input: List<String>, yRef: Int): Pair<Int, MutableList<IntRange>> {
        val pairSensorBeacon = input.map { readCoordinates(it) }.toList()
        val rangesNoBeacon = mutableListOf<IntRange>()
        var beaconsInRef = mutableSetOf<List<Int>>()
        pairSensorBeacon.forEach {
            var d = kotlin.math.abs(it[0][0]-it[1][0]) + kotlin.math.abs(it[0][1] - it[1][1])
            if (it[1][1] == yRef) beaconsInRef.add(it[1])
            if (d >= kotlin.math.abs(yRef - it[0][1])){
                rangesNoBeacon.add(it[0][0]-(d - kotlin.math.abs(yRef - it[0][1])) .. it[0][0]+(d - kotlin.math.abs(yRef - it[0][1])))
            }
        }
        rangesNoBeacon.sortBy { it.first }
        val nonIntersectingRanges = mutableListOf<IntRange>()
        nonIntersectingRanges.add(rangesNoBeacon[0])
        for (range in rangesNoBeacon){
            if (nonIntersectingRanges.takeLast(1)[0].myIntersect(range)){
                nonIntersectingRanges[nonIntersectingRanges.size-1] =
                    minOf(nonIntersectingRanges[nonIntersectingRanges.size-1].first, range.first) ..
                            maxOf(nonIntersectingRanges[nonIntersectingRanges.size-1].last, range.last)
            }
            else if (nonIntersectingRanges.takeLast(1)[0].myAdjacent(range)){
                nonIntersectingRanges[nonIntersectingRanges.size-1] =
                    minOf(nonIntersectingRanges[nonIntersectingRanges.size-1].first, range.first) ..
                            maxOf(nonIntersectingRanges[nonIntersectingRanges.size-1].last, range.last)
            }
            else nonIntersectingRanges.add(range)
        }
        return Pair(nonIntersectingRanges.sumOf { it.count() } - beaconsInRef.size, nonIntersectingRanges)
    }

    fun part2(input: List<String>, searchRegion: Int): Int {
        val pairSensorBeacon = input.map { readCoordinates(it) }.toList()

        val pairSensorDist = pairSensorBeacon.map {
            Pair(Pair(it[0][0], it[0][1]), kotlin.math.abs(it[0][0]-it[1][0]) + kotlin.math.abs(it[0][1] - it[1][1]))
        }.toList()

        pairSensorDist.sortedBy { it.first.first }

        var intersections = mutableSetOf<Pair<Int, Int>>()

        for (i in pairSensorDist.indices){
            for (j in i until pairSensorDist.size){
                if (pairSensorDist[j].first.first - pairSensorDist[i].first.first > pairSensorDist[j].second + pairSensorDist[i].second) {
                    break
                }
                var biList = listOf<Int>(pairSensorDist[i].first.second - pairSensorDist[i].first.first + pairSensorDist[i].second,
                    pairSensorDist[i].first.second - pairSensorDist[i].first.first - pairSensorDist[i].second)
                var BiList = listOf<Int>(pairSensorDist[i].first.second + pairSensorDist[i].first.first + pairSensorDist[i].second,
                    pairSensorDist[i].first.second + pairSensorDist[i].first.first - pairSensorDist[i].second)
                var bjList = listOf<Int>(pairSensorDist[j].first.second - pairSensorDist[j].first.first + pairSensorDist[j].second,
                    pairSensorDist[j].first.second - pairSensorDist[j].first.first - pairSensorDist[j].second)
                var BjList = listOf<Int>(pairSensorDist[j].first.second + pairSensorDist[j].first.first + pairSensorDist[j].second,
                    pairSensorDist[j].first.second + pairSensorDist[j].first.first - pairSensorDist[j].second)

                for (bi in biList){
                    for (Bj in BjList){
                        var interPoint = Pair((Bj - bi)/2, (Bj + bi)/2)
                        if ((interPoint.first in 0 .. searchRegion)&&(interPoint.second in 0 .. searchRegion)){
                        intersections.add(interPoint)}
                    }
                }
                for (Bi in BiList){
                    for (bj in bjList){
                        var interPoint = Pair((Bi - bj)/2, (Bi + bj)/2)
                        if ((interPoint.first in 0 .. searchRegion)&&(interPoint.second in 0 .. searchRegion)){
                        intersections.add(interPoint)}
                    }
                }
            }
        }

        fun Pair<Int, Int>.notInside(): Boolean {
            for (sensor in pairSensorDist){
                if (kotlin.math.abs(this.first - sensor.first.first)+ kotlin.math.abs(this.second-sensor.first.second) <= sensor.second) {
                    return false
                }
            }
            return true
        }

        loop@ for (point in intersections){
            for (step in listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))){
                if (Pair(point.first+step.first, point.second+step.second).notInside()){
                    if ((point.first+step.first in 0 .. searchRegion)&&(point.second+step.second in 0 .. searchRegion)) {
                        var answer:Long = (point.first + step.first).toLong()*searchRegion.toLong()+point.second + step.second
                        println(answer)
                        break@loop
                    }
                }
            }
        }


    return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    println(part1(testInput, 10).first)
    part2(testInput, 20)


    val input = readInput("Day15")
    println(part1(input, 2000000).first)
    part2(input, 4000000)

}
