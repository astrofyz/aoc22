import kotlin.math.log
import kotlin.math.pow

fun main() {

    val mapSNAFU = mapOf<Char, Int>('2' to 2, '1' to 1, '0' to 0, '-' to -1, '=' to -2)
    val SNAFUback = "=-012"

    fun SNAFU2Dec(input: String): Long{
        var res: Long = 0
        for ((place, elem) in input.reversed().withIndex()){
            res += mapSNAFU.getOrDefault(elem, 0)*(5.toDouble().pow(place.toDouble()).toLong())
        }
//        println("$input -> $res")
        return res
    }

    fun part1(input: List<String>){
        println(input.sumOf { SNAFU2Dec(it) })
        var res = input.sumOf { SNAFU2Dec(it) }
        var logR = log(res.toDouble(), 5.0)

//        while (logR > 0){
//            println("$res $logR, ${res / 5.0.pow(logR.toInt().toDouble())} ${logR.toInt().toDouble()} ${(5.0).pow(logR.toInt().toDouble()).toLong()}")
//            res -= (5.0).pow(logR.toInt().toDouble()).toLong()
//            logR = log(res.toDouble(), 5.0)
//        }
        var i = 15
        var resStr = ""
        while (res > 0){
            var m = (res + 2L) % 5L
            res = (res + 2L) / 5L
            resStr += SNAFUback[m.toInt()]
        }
        println(resStr.reversed())
        println(SNAFU2Dec(resStr.reversed()))
    }


    val testInput = readInput("Day25")
    part1(testInput)
    println(log(47045194.0, 5.0))
}