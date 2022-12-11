fun main() {

    class Monkey(
        var items: MutableList<Int> = mutableListOf(), var operation: String = "",
        var test: Int = 1, var testTrue: Int = 0, var testFalse: Int = 1,
        var business: Int = 0){
    }

    fun Int.update(operation: String): Int {
        return if (('+' in operation)&&(operation.any{it.isDigit()})) {
            this + operation.filter { it.isDigit() }.toInt()
        } else if (('*' in operation)&&(operation.any{it.isDigit()})) {
            this * operation.filter { it.isDigit() }.toInt()
        } else if ('+' in operation) {
            this + this
        } else {
            if (this * this >= 2147483647){
                println("greater than Int Max")
            }
//            println("$this, ${this * this}")
            this * this
        }
    }

    fun Monkey.inspectionPart1(): Pair<Int, Int> {
        var worryLevel: Int = this.items[0]
        this.business++
        worryLevel = worryLevel.update(this.operation) / 3
        if (worryLevel % this.test == 0.toInt()){
            return Pair(worryLevel, this.testTrue)
        }
        else { return Pair(worryLevel, this.testFalse)}
    }

    fun Monkey.inspectionPart2(lcm: Int): Pair<Int, Int> {
        var worryLevel: Int = this.items[0]
        this.business++
        worryLevel = (worryLevel.update(this.operation)) % lcm
        if (worryLevel % this.test == 0){
            return Pair(worryLevel, this.testTrue)
        }
        else { return Pair(worryLevel, this.testFalse)}
    }

    fun Monkey.throwing(other: Monkey, itemWorryLevel: Int){
        this.items.removeAt(0)
        other.items.add(itemWorryLevel)
    }

    fun parseInput(input: List<String>): List<Monkey> {
        var Monkeys = List((input.size+1) / 7, {Monkey()})
        var currentIndex = 0
        for (line in input){
            with(line) {
                when {
                    contains("Monkey") -> currentIndex = line.filter { it.isDigit() }.toInt()
                    contains("Starting items") ->
                        Monkeys[currentIndex].items = line
                            .split(": ")[1]
                            .split(", ")
                            .map{it.toInt()}
                            .toMutableList()
                    contains("Test") -> Monkeys[currentIndex].test = line.filter { it.isDigit() }.toInt()
                    contains("Operation") -> Monkeys[currentIndex].operation = line.split("= ")[1]
                    contains("If true") -> Monkeys[currentIndex].testTrue = line.filter { it.isDigit() }.toInt()
                    contains("If false") -> Monkeys[currentIndex].testFalse = line.filter { it.isDigit() }.toInt()
                }
            }
        }
        return Monkeys
    }

    fun part1(input: List<String>): Int{
        var Monkeys = parseInput(input)

        repeat(20){
            for (monkey in Monkeys){
                while (monkey.items.size > 0){
                    var (worryLevel, indexReceiver) = monkey.inspectionPart1()
                    monkey.throwing(Monkeys[indexReceiver], worryLevel)
                    }
                }
//            Monkeys.mapIndexed { index, mnk -> println("Monkey $index: ${Monkeys[index].items}") }
            }
        Monkeys.mapIndexed { index, mnk -> println("Monkey $index inspected items ${Monkeys[index].business} times") }
        var business = Monkeys.map { it.business }.sortedDescending()
        return business[0]*business[1]
        }


    fun part2(input: List<String>): Int{
        var Monkeys = parseInput(input)

        val lcm = Monkeys.map { it.test }.reduce{acc, testValue -> acc*testValue}
        println("lcm $lcm")

        repeat(20){
            for (monkey in Monkeys){
                while (monkey.items.size > 0){
                    var (worryLevel, indexReceiver) = monkey.inspectionPart2(lcm)
                    monkey.throwing(Monkeys[indexReceiver], worryLevel)
                }
            }
        }
        Monkeys.mapIndexed { index, mnk -> println("Monkey $index inspected items ${Monkeys[index].business} times") }
        var business = Monkeys.map { it.business }.sortedDescending()
        println(business)
        return business[0]*business[1]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
//    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day11")
//    println(part1(input))
    println(part2(input))
}
