fun main() {

    infix fun Long.modulo(modulus: Long): Long {
        val remainder = this % modulus
        if (this >= modulus) return this % (modulus-1)
        if ((kotlin.math.abs(this) >= modulus)&&(this < 0)) return (this % (modulus-1)) + modulus - 1
        return if (remainder >= 0) remainder else remainder + modulus - 1
    }


    fun part1(input:List<String>){

        val initList = input.map { it.toInt() }

        var currentList = initList.mapIndexed { index, i -> mapOf(index to i) }.toMutableList()
        var elem: Map<Int, Int>
        var elemId: Int
        println(initList)

        initList.forEachIndexed { index, i ->
            elem = currentList.first { it.keys.first() == index }
            elemId = currentList.indexOfFirst{it.keys.first() == index}
            currentList.removeIf { it.keys.first() == index }
            var newIndexCycle = elemId+elem.values.first().toLong() modulo (initList.size.toLong())
            currentList.add(index=newIndexCycle.toInt(), elem)
        }
//        println(currentList.map { it.values.take(1) })

        var idxsZero = currentList.indexOfFirst { it.values.first() == 0 }
//        println("base $idxsZero")
        println(currentList[(idxsZero+1000) % initList.size].values.first()+
                currentList[(idxsZero+2000) % initList.size].values.first()+
                currentList[(idxsZero+3000) % initList.size].values.first())

    }


    fun part2(input:List<String>){

        var key = 811589153.toLong()
        val initList = input.map { it.toLong()*key }

        var currentList = initList.mapIndexed { index, i -> mapOf(index to i) }.toMutableList()
        var elem: Map<Int, Long>
        var elemId: Int
        println(initList)

        repeat(10) {
            initList.forEachIndexed { index, i ->
                elem = currentList.first { it.keys.first() == index }
                elemId = currentList.indexOfFirst { it.keys.first() == index }
                currentList.removeIf { it.keys.first() == index }
                var newIndexCycle = elemId + elem.values.first() modulo (initList.size.toLong())
                currentList.add(index = newIndexCycle.toInt(), elem)
            }}
            println(currentList.map { it.values.take(1) })
//        }

        var idxsZero = currentList.indexOfFirst { it.values.first() == 0.toLong() }
//        println("base $idxsZero")

        println(currentList[(idxsZero+1000) % initList.size].values.first()+
                currentList[(idxsZero+2000) % initList.size].values.first()+
                currentList[(idxsZero+3000) % initList.size].values.first())

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20")
    part2(testInput)   // 6919 is too low
//    part2(testInput)
}