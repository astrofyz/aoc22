import java.io.File

fun main() {
    class MyStack {
        var crates = mutableListOf<Char>()
    }

    fun MyStack.remove(n: Int): MutableList<Char>{
            val removedCrates = this.crates.subList(this.crates.size-n, this.crates.size)
            this.crates = this.crates.subList(0, this.crates.size-n)
            return removedCrates
        }

    fun MyStack.add(newCrates: MutableList<Char>) {
        this.crates.addAll(newCrates.asReversed())
    }

    fun MyStack.add9001(newCrates: MutableList<Char>) {
        this.crates.addAll(newCrates)
    }

    fun parseInput(lines: String): Pair<List<MyStack>, List<String>> {
        val stuff = lines.split("\n\n")[0].split("\n")
        val rules = lines.split("\n\n")[1].split("\n")
        val numberOfStacks = (stuff[stuff.size-1].filter {it.isDigit()}).length

        val stacks: List<MyStack> = List(numberOfStacks) { MyStack() }

        stuff.forEach{line ->
            line.chunked(4)
                .map{ it.filter { elem -> elem.isLetter() }}
                .mapIndexed{idx, it ->
                    when(it.isEmpty()){
                        true -> 0
                        else -> stacks[idx].add(mutableListOf(it[0]))
                    }
                }
            }

        stacks.forEach { stack -> stack.crates = stack.crates.asReversed() }
        return Pair(stacks, rules)
    }

//    fun List<MyStack>.printUpdated(){
//        for (stack in this){
//            println(stack.crates)
//        }
//    }


    fun part1(input: String): String {
        val (stacks, rules) = parseInput(input)
        rules.forEach{rule ->
            val numberOfStacks = rule.split(" ")[1].toInt()
            val moveFrom = rule.split(" ")[3].toInt() - 1
            val moveTo = rule.split(" ")[5].toInt() - 1
            stacks[moveTo].add(stacks[moveFrom].remove(numberOfStacks))
        }
    return stacks.map { it.crates[it.crates.size-1] }.joinToString("")
    }

    fun part2(input: String): String {
        val (stacks, rules) = parseInput(input)
        rules.forEach {rule ->
            val numberOfStacks = rule.split(" ")[1].toInt()
            val moveFrom = rule.split(" ")[3].toInt() - 1
            val moveTo = rule.split(" ")[5].toInt() - 1
            stacks[moveTo].add9001(stacks[moveFrom].remove(numberOfStacks))
        }
        return stacks.map { it.crates[it.crates.size-1] }.joinToString("")

    }

    // test if implementation meets criteria from the description, like:
    val testInput = File("src","Day05_test.txt").readText()
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = File("src","Day05.txt").readText()
    println(part1(input))
    println(part2(input))
}
