import java.io.File

fun main(){

    fun List<Int>.isAdjacent(other: List<Int>):Boolean{
        var diff = this.mapIndexed { index, i -> kotlin.math.abs(this[index] - other[index]) }.toList()
        return (diff.count{it==0} == 2)&&(diff.count{it==1} == 1)
    }

    fun List<Int>.isOneAdjacent(other: List<Int>):Boolean{
        var diff = this.mapIndexed { index, i -> kotlin.math.abs(this[index] - other[index]) }.toList()
        return (diff.count{it==0} == 2)&&(diff.count{it==2} == 1)
    }

    fun part1(input: List<String>){
        var cubes = listOf<List<Int>>()
        cubes = input.map { it.split(',').map { elem -> elem.toInt() } }
        var result = mutableListOf<Boolean>()

        for (i in 0 until cubes.size){
            for (j in i until cubes.size){
                result.add(cubes[i].isAdjacent(cubes[j]))
            }
        }
        println(input.size)
        println(result.count { it })
        println("res1 : ${input.size * 6 - 2*result.count { it }}")

    }

    fun part2(input: List<String>) {
        var cubes = listOf<List<Int>>()
        cubes = input.map { it.split(',').map { elem -> elem.toInt() } }
        var result = mutableListOf<Boolean>()

        var notAdjPairs = mutableListOf<List<Int>>()

        for (i in 0 until cubes.size) {
            for (j in i until cubes.size) {
                if (cubes[i].isOneAdjacent(cubes[j])) {
                    var middleCube = cubes[i].zip(cubes[j]).map { (it.first + it.second) / 2 }.toList()
                    if (middleCube !in cubes){
                        notAdjPairs.add(cubes[i].zip(cubes[j]).map { (it.first + it.second) / 2 }.toList())
                    }
                }
            }
        }
//        println(notAdjPairs.size)
        println(notAdjPairs.groupingBy { it }.eachCount())
        println(notAdjPairs.groupingBy { it }.eachCount().filterValues { it==3 }.size)
    }


// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18")
    part1(testInput)
    part2(testInput)


//    val input = readInput("Day17")
}