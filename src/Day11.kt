fun main() {

    class Monkey(
        var items: MutableList<Long> = mutableListOf(), var operation: String = "",
        var test: Long = 1, var testTrue: Long = 0, var testFalse: Long = 1,
        var business: Long = 0){
    }

    fun Long.update(operation: String): Long {
        return if (('+' in operation)&&(operation.any{it.isDigit()})) {
            this + operation.filter { it.isDigit() }.toLong()
        } else if (('*' in operation)&&(operation.any{it.isDigit()})) {
            this * operation.filter { it.isDigit() }.toLong()
        } else if ('+' in operation) {
            this + this
        } else {
            this * this
        }
    }

    fun Monkey.inspectionPart1(): Pair<Long, Long> {
        var worryLevel: Long = this.items[0]
        this.business++
        worryLevel = worryLevel.update(this.operation) / 3
        if (worryLevel % this.test == 0.toLong()){
            return Pair(worryLevel, this.testTrue)
        }
        else { return Pair(worryLevel, this.testFalse)}
    }

    fun Monkey.inspectionPart2(lcm: Long): Pair<Long, Long> {
        var worryLevel: Long = this.items[0].toLong()
        this.business++
        worryLevel = worryLevel.update(this.operation)
        worryLevel = worryLevel % lcm
        if (worryLevel % this.test == 0.toLong()){
            return Pair(worryLevel, this.testTrue)
        }
        else { return Pair(worryLevel, this.testFalse)}
    }

    fun Monkey.throwing(other: Monkey, itemWorryLevel: Long){
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
                            .map{it.toLong()}
                            .toMutableList()
                    contains("Test") -> Monkeys[currentIndex].test = line.filter { it.isDigit() }.toLong()
                    contains("Operation") -> Monkeys[currentIndex].operation = line.split("= ")[1]
                    contains("If true") -> Monkeys[currentIndex].testTrue = line.filter { it.isDigit() }.toLong()
                    contains("If false") -> Monkeys[currentIndex].testFalse = line.filter { it.isDigit() }.toLong()
                }
            }
        }
        return Monkeys
    }

    fun part1(input: List<String>): Long{
        var Monkeys = parseInput(input)

        repeat(20){
            for (monkey in Monkeys){
                while (monkey.items.size > 0){
                    var (worryLevel, indexReceiver) = monkey.inspectionPart1()
                    monkey.throwing(Monkeys[indexReceiver.toInt()], worryLevel)
                    }
                }
            }
        Monkeys.mapIndexed { index, mnk -> println("Monkey $index inspected items ${Monkeys[index].business} times") }
        var business = Monkeys.map { it.business }.sortedDescending()
        return business[0]*business[1]
        }


    fun part2(input: List<String>): Long{
        var Monkeys = parseInput(input)

        val lcm = Monkeys.map { it.test }.reduce{acc, testValue -> acc*testValue}

        repeat(10000){
            for (monkey in Monkeys){
                while (monkey.items.size > 0){
                    var (worryLevel, indexReceiver) = monkey.inspectionPart2(lcm)
                    monkey.throwing(Monkeys[indexReceiver.toInt()], worryLevel)
                }
            }
        }
        Monkeys.mapIndexed { index, mnk -> println("Monkey $index inspected items ${Monkeys[index].business} times") }
        var business = Monkeys.map { it.business }.sortedDescending()
        return business[0]*business[1]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
