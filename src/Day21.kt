fun main() {

    class Monkey(var num: Long? = null, var operation: String? = null, var operLeft: String? = null, var operRight: String? = null){}

    fun Monkey.print(){
        println("num: ${this.num}")
        println("${this.operLeft} ${this.operation} ${this.operRight}")
    }

    fun Monkey.update(mapMonkeys: MutableMap<String, Monkey>): Long {
//        println("${mapMonkeys[this.operLeft]?.num} ${this.operation} ${mapMonkeys[this.operRight]?.num}")
        when (this.operation){
            "+" -> return mapMonkeys[this.operLeft]?.num?.let { mapMonkeys[this.operRight]?.num?.plus(it) } ?: 0
            "-" -> return mapMonkeys[this.operRight]?.num?.let { mapMonkeys[this.operLeft]?.num?.minus(it) } ?: 0
            "/" -> return (mapMonkeys[this.operLeft]?.num?:0) / (mapMonkeys[this.operRight]?.num?:1)
            "*" -> return (mapMonkeys[this.operLeft]?.num?:0) * (mapMonkeys[this.operRight]?.num?: 0)
            else -> {println("Error in operation"); return 0}
        }
    }


//    fun Monkey.updateInverse(flag:String, operand: Long?, res: Long?): Long {
////        println("${mapMonkeys[this.operLeft]?.num} ${this.operation} ${mapMonkeys[this.operRight]?.num}")
//        if (flag == "left"){
//        when (this.operation){
//            "+" -> return (res!! - operand!!) ?: 0
//            "-" -> return (res!! + mapMonkeys[this.operRight]?.num!!) ?: 0
//            "/" -> return (res!! * mapMonkeys[this.operRight]?.num!!) ?: 0
//            "*" -> return (res!! / mapMonkeys[this.operRight]?.num!!) ?: 0
//            else -> {println("Error in operation"); return 0}
//        }
//        }
//        else{
//            when (this.operation){
//                "+" -> return (res - mapMonkeys[this.operLeft]?.num!!) ?: 0
//                "-" -> return (mapMonkeys[this.operLeft]?.num!! - res) ?: 0
//                "/" -> return (mapMonkeys[this.operLeft]?.num!! / res) ?: 0
//                "*" -> return (res / mapMonkeys[this.operLeft]?.num!!) ?: 0
//                else -> {println("Error in operation"); return 0}
//            }
//        }
//    }

    fun parseMonkeys(input: List<String>): MutableMap<String, Monkey> {
        var mapMonkeys = mutableMapOf<String, Monkey>()

        for (line in input){
            if (line.map { it.isDigit() }.count { it } > 0){
                mapMonkeys[line.split(":")[0]] = Monkey(num=line.split(": ")[1].toLong())
            }
            else{
                var (operLeft, operation, operRight) = line.split(": ")[1].split(" ")
                mapMonkeys[line.split(":")[0]] = Monkey(operation = operation, operLeft = operLeft, operRight = operRight)
            }
        }
        return mapMonkeys
    }

    fun part1(input: List<String>):Long{
        var mapMonkeys = parseMonkeys(input)

        var initNumbers = mapMonkeys.filterValues { it.num != null }

        var nextMonkeys = mapMonkeys
            .filterValues { (it.operRight in initNumbers.keys)&&(it.operLeft in initNumbers.keys)&&(it.num == null) }

        while (true){
            for (monk in nextMonkeys){
                mapMonkeys[monk.key]?.num = mapMonkeys[monk.key]?.update(mapMonkeys)
            }
            initNumbers = mapMonkeys.filterValues { it.num != null }
            nextMonkeys = mapMonkeys
                .filterValues { (it.operRight in initNumbers.keys)&&(it.operLeft in initNumbers.keys)&&(it.num == null) }
            if ("root" in initNumbers.map { it.key }) break
        }
        return mapMonkeys["root"]?.num ?: 0
    }


    fun part2(input: List<String>):Long{
        var mapMonkeys = parseMonkeys(input)

        mapMonkeys["humn"]?.num = null

        var initNumbers = mapMonkeys.filterValues { it.num != null }

        var nextMonkeys = mapMonkeys
            .filterValues { (it.operRight in initNumbers.keys)&&(it.operLeft in initNumbers.keys)&&(it.num == null) }

        while (true){
            for (monk in nextMonkeys){
                if (monk.key == "humn") continue
                mapMonkeys[monk.key]?.num = mapMonkeys[monk.key]?.update(mapMonkeys)
            }
            initNumbers = mapMonkeys.filterValues { it.num != null }
            nextMonkeys = mapMonkeys
                .filterValues { (it.operRight in initNumbers.keys)&&(it.operLeft in initNumbers.keys)&&(it.num == null) }
            if ((mapMonkeys[mapMonkeys["nrvt"]?.operRight]?.num != null)||(mapMonkeys[mapMonkeys["nrvt"]?.operRight]?.num != null)) break
        }

        println("${mapMonkeys["nrvt"]?.operLeft} ${mapMonkeys[mapMonkeys["nrvt"]?.operLeft]?.num}")
        println("${mapMonkeys["nrvt"]?.operRight} ${mapMonkeys[mapMonkeys["nrvt"]?.operRight]?.num}")
        println("last unknown")

        println("${mapMonkeys["root"]?.operLeft} ${mapMonkeys[mapMonkeys["root"]?.operLeft]?.num}")
        println("${mapMonkeys["root"]?.operRight} ${mapMonkeys[mapMonkeys["root"]?.operRight]?.num}")
        println("root unknown")

        var a = 1.0.toDouble()
        var b = 0.0.toDouble()
        var nextX = "humn"

//        println(mapMonkeys.filterValues { it.operLeft == nextX || it.operRight == nextX }.keys.first())
//
        while (true){
            var monk = mapMonkeys.filterValues { it.operLeft == nextX || it.operRight == nextX }.keys.first()

            if (mapMonkeys[monk]?.operRight == nextX && mapMonkeys[mapMonkeys[monk]?.operLeft]?.num != null) {
//                println("${mapMonkeys[monk]?.operLeft}, ${mapMonkeys[mapMonkeys[monk]?.operLeft]?.num}")
//                println("${mapMonkeys[monk]?.operRight}, ${mapMonkeys[mapMonkeys[monk]?.operRight]?.num}")
                when(mapMonkeys[monk]?.operation){
                    "+" -> b += mapMonkeys[mapMonkeys[monk]?.operLeft]?.num!!
                    "-" -> {b = mapMonkeys[mapMonkeys[monk]?.operLeft]?.num!! - b; a *= -1L}
                    "*" -> {b *= mapMonkeys[mapMonkeys[monk]?.operLeft]?.num!!; a *= mapMonkeys[mapMonkeys[monk]?.operLeft]?.num!!}
                    "/" -> {println("x in denominator:(")}
                }
                nextX = monk
//                println("a = $a; b = $b")
                if (nextX == "dcsn") {break}
            }
            else if (mapMonkeys[monk]?.operLeft == nextX && mapMonkeys[mapMonkeys[monk]?.operRight]?.num != null) {
//                println("${mapMonkeys[monk]?.operLeft}, ${mapMonkeys[mapMonkeys[monk]?.operLeft]?.num}")
//                println("${mapMonkeys[monk]?.operRight}, ${mapMonkeys[mapMonkeys[monk]?.operRight]?.num}")
                when(mapMonkeys[monk]?.operation){
                    "+" -> b += mapMonkeys[mapMonkeys[monk]?.operRight]?.num!!
                    "-" -> {b -= mapMonkeys[mapMonkeys[monk]?.operRight]?.num!!}
                    "*" -> {b *= mapMonkeys[mapMonkeys[monk]?.operRight]?.num!!; a *= mapMonkeys[mapMonkeys[monk]?.operRight]?.num!!}
                    "/" -> {b /= mapMonkeys[mapMonkeys[monk]?.operRight]?.num!!.toDouble(); a /= mapMonkeys[mapMonkeys[monk]?.operRight]?.num!!.toDouble()}
                }
                nextX = monk
//                println("a = $a; b = $b")
                if (nextX == "dcsn") {break}
            }
            else{
//                    println("${mapMonkeys[monk]?.operLeft}, ${mapMonkeys[mapMonkeys[monk]?.operLeft]?.num}")
//                    println("${mapMonkeys[monk]?.operRight}, ${mapMonkeys[mapMonkeys[monk]?.operRight]?.num}")
            }
        }
        println("a = $a")
        println("b = $b")





        var nextOperand: String?
        var flag = "left"

        if (mapMonkeys[mapMonkeys["root"]?.operLeft]?.num == null){
            nextOperand = mapMonkeys["root"]?.operLeft
            flag = "left"
            mapMonkeys[nextOperand]?.num = mapMonkeys[mapMonkeys["root"]?.operRight]?.num
        }
        else {
            nextOperand = mapMonkeys["root"]?.operRight
            flag = "right"
            mapMonkeys[nextOperand]?.num = mapMonkeys[mapMonkeys["root"]?.operLeft]?.num
        }

        while (true){
            println(nextOperand)
            mapMonkeys[nextOperand]?.print()
            println("..................")
            if (mapMonkeys[mapMonkeys[nextOperand]?.operLeft]?.num == null && mapMonkeys[mapMonkeys[nextOperand]?.operRight]?.num != null){
                var res = mapMonkeys[nextOperand]?.num
//                println(res)
                var operand = mapMonkeys[mapMonkeys[nextOperand]?.operRight]?.num
//                println("$res, $operand")
                var operation = mapMonkeys[nextOperand]?.operation
                nextOperand = mapMonkeys[nextOperand]?.operLeft
                when (operation) {
                    "+" -> mapMonkeys[nextOperand]?.num = (res!! - operand!!) ?: 0
                    "-" -> mapMonkeys[nextOperand]?.num =  (res!! + operand!!) ?: 0
                    "/" -> mapMonkeys[nextOperand]?.num =  (res!! * operand!!) ?: 0
                    "*" -> mapMonkeys[nextOperand]?.num =  (res!! / operand!!) ?: 0
                }
            }
            else if (mapMonkeys[mapMonkeys[nextOperand]?.operRight]?.num == null && mapMonkeys[mapMonkeys[nextOperand]?.operLeft]?.num != null) {
                var res = mapMonkeys[nextOperand]?.num
//                println(res)
                var operand = mapMonkeys[mapMonkeys[nextOperand]?.operLeft]?.num
//                println("$res, $operand")

                var operation = mapMonkeys[nextOperand]?.operation
                nextOperand = mapMonkeys[nextOperand]?.operRight
                when (operation) {
                    "+" -> mapMonkeys[nextOperand]?.num = (res!! - operand!!) ?: 0
                    "-" -> mapMonkeys[nextOperand]?.num = (operand!! - res!!) ?: 0
                    "/" -> mapMonkeys[nextOperand]?.num = (operand!! / res!!) ?: 0
                    "*" -> mapMonkeys[nextOperand]?.num = (res!! / operand!!) ?: 0
                }
            }
            else {
                nextOperand = mapMonkeys[nextOperand]?.operRight
            }
            if (nextOperand == "humn") {println(mapMonkeys[nextOperand]?.num); break}

        }

        return mapMonkeys["root"]?.num ?: 0
    }


    val testInput = readInput("Day21")
//    println(part1(testInput))
    part2(testInput)
}