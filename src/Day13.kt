import java.io.File
fun main() {

    fun parseInput(input: String): List<Pair<String, String>> {
        return input.split("\n\n").map { Pair(it.split("\n")[0], it.split("\n")[1]) }
    }

    fun parseSomething(input:MutableList<String>): Pair<MutableList<Any>, Int> {
        var res = mutableListOf<Any>()
        var index = 0
        while (index < input.size) {
            if (input[index] == "["){
                var (innerList, endIndex) = parseSomething(input.slice(index+1 until input.size).toMutableList())
                res.add(innerList)
                index += endIndex+2
            }
            else if (input[index] == "]") {
                return Pair(res, index)
            }
            else {
                res.add(input[index].toInt())
                index ++
            }
        }
        return Pair(res, index)
    }

    fun turnToList(input: String): MutableList<String>{
        var testList = mutableListOf<String>()
        var index = 0
        while (index < input.length) {
            if (input[index] == ',') index++
            else if (input[index] in listOf(']', '[')) {
                testList.add(input[index].toString()); index++
            } else {
                var number = input.substring(index)
                    .takeWhile { it.isDigit() }; testList.add(number); index += number.length
            }
        }
        return testList
    }

    fun ifLess(left: ArrayList<*>, right: ArrayList<*>): Int{
        var index = 0
        var flag = 0
        if ((left.size == 0)&&(right.size > 0)) flag = 1
        if ((right.size == 0)&&(left.size > 0)) flag = -1
        while ((index < minOf(left.size, right.size))&&(flag == 0)){
            var it = Pair(left[index], right[index])
            if ((it.first is Int)&&(it.second is Int)){
                if ((it.first as Int) < (it.second as Int)) flag = 1
                if ((it.first as Int) > (it.second as Int)) flag = -1
            }
            if ((it.first is ArrayList<*>)&&(it.second is Int)) flag = ifLess(it.first as ArrayList<*>, arrayListOf(it.second))
            if ((it.second is ArrayList<*>)&&(it.first is Int)) flag = ifLess(arrayListOf(it.first), it.second as ArrayList<*>)
            if ((it.first is ArrayList<*>)&&(it.second is ArrayList<*>)) flag = ifLess(it.first as ArrayList<*>, it.second as ArrayList<*>)
            index ++
        }
        if ((left.size > right.size)&&(flag == 0)) flag = -1
        if ((left.size < right.size)&&(flag == 0)) flag = 1
        return flag
    }


    fun part1(input: String) {
        var res = 0
        var listPairs = parseInput(input)
        listPairs.forEachIndexed { index, pair ->
                var leftList = parseSomething(turnToList(pair.first)).first[0] as List<Any>
                var rightList = parseSomething(turnToList(pair.second)).first[0] as List<Any>
                var flag = ifLess(leftList as ArrayList<*>, rightList as ArrayList<*>).coerceIn(0..1)
                res += (index+1)*flag
        }
        println(res)
    }

    fun part2(input: String) {
        var res = 0
        var listPairs = parseInput(input)
        var divider1 = arrayListOf(arrayListOf(2)) as ArrayList<*>
        var divider2 = arrayListOf(arrayListOf(6)) as ArrayList<*>
        var allPackets = arrayListOf(divider1, divider2)
        listPairs.forEachIndexed { index, pair ->
                var leftList = parseSomething(turnToList(pair.first)).first[0] as List<Any>
                var rightList = parseSomething(turnToList(pair.second)).first[0] as List<Any>
                allPackets.add(leftList as ArrayList<*>)
                allPackets.add(rightList as ArrayList<*>)
            }
        val packetsComparator = Comparator{packet1: ArrayList<*>, packet2: ArrayList<*> -> ifLess(packet1, packet2)}
        val sortedPackets = allPackets.sortedWith(packetsComparator).reversed()
        println(sortedPackets.indexOf(divider1))
        println(sortedPackets.indexOf(divider2))
        println((sortedPackets.indexOf(divider1)+1)*(sortedPackets.indexOf(divider2)+1))
    }





//    fun part2(input: List<String>){}

    // test if implementation meets criteria from the description, like:
    val textTest = File("src", "Day13_test.txt").readText()
    part1(textTest)
    part2(textTest)


    val text = File("src", "Day13.txt").readText()
    part1(text)
    part2(text)
}
