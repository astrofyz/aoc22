fun main() {

    class Monkey(
        var items: MutableList<Long> = mutableListOf(), var operation: String = "",
        var test: Int = 1, var testTrue: Int = 0, var testFalse: Int = 1,
        var business: Int = 0){
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

    fun Monkey.inspection(): Pair<Long, Int> {
        var worryLevel: Long = this.items[0]
        this.business++
        worryLevel = worryLevel.update(this.operation) / 3
        if (worryLevel % this.test == 0.toLong()){
            return Pair(worryLevel, this.testTrue)
        }
        else { return Pair(worryLevel, this.testFalse)}
    }

    fun Monkey.throwing(other: Monkey, itemWorryLevel: Long){
        this.items.removeAt(0)
        other.items.add(itemWorryLevel)
    }

    fun part1(input: List<String>): Int {
        println("inputsize = ${input.size} number of monkeys = ${(input.size+1) / 7}")
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
                    contains("Test") -> Monkeys[currentIndex].test = line.filter { it.isDigit() }.toInt()
                    contains("Operation") -> Monkeys[currentIndex].operation = line.split("= ")[1]
                    contains("If true") -> Monkeys[currentIndex].testTrue = line.filter { it.isDigit() }.toInt()
                    contains("If false") -> Monkeys[currentIndex].testFalse = line.filter { it.isDigit() }.toInt()
                }
            }
        }

        repeat(20){
            for (monkey in Monkeys){
                while (monkey.items.size > 0){
                    var (worryLevel, indexReceiver) = monkey.inspection()
                    monkey.throwing(Monkeys[indexReceiver], worryLevel)
                    }
                }
//            Monkeys.mapIndexed { index, mnk -> println("Monkey $index: ${Monkeys[index].items}") }
            }
        Monkeys.mapIndexed { index, mnk -> println("Monkey $index inspected items ${Monkeys[index].business} times") }
        var business = Monkeys.map { it.business }.sortedDescending()
        return business[0]*business[1]
        }

//    fun part2(input: List<String>): Int {
//    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    println(part1(testInput))
//    check(part1(testInput) == 157)
//    check(part2(testInput) == 70)

    val input = readInput("Day11")
    println(part1(input))
//    println(part2(input))
}
