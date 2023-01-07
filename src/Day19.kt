import kotlin.math.max

fun main() {

    class State(var oreRobot: Int, var clayRobot: Int, var obsidianRobot: Int, var geodeRobot: Int,
        var ore: Int, var clay: Int, var obsidian: Int, var geode: Int, var t: Int)

    fun parseBlueprint(input: String): MutableMap<String, MutableMap<String, Int>> {
        val ruleMap = mutableMapOf<String, MutableMap<String, Int>>()
        for (rule in input.split(":")[1].split(" Each ").subList(1, 5)){
            ruleMap[rule.split(" ")[0]] = mutableMapOf<String, Int>()
            val elems = rule.split(" ")
            for ((index, elem) in elems.withIndex()){
                if (elem.toIntOrNull() != null){
                    ruleMap[rule.split(" ")[0]]?.set(elems[index+1].replace(".", ""), elem.toInt())
                }
            }
        }
        return ruleMap
    }

    fun State.print(){
        println("oreRobot: ${this.oreRobot} clayRobot: ${this.clayRobot} obsRobot: ${this.obsidianRobot} geodeRobot: ${this.geodeRobot}")
        println("ore: ${this.ore} clay: ${this.clay} obsidian: ${this.obsidian} geode: ${this.geode}; t = ${this.t}")
        println("---------------------")
    }

    fun part12(input: List<String>){
        var geodes = mutableListOf<Int>()

        for (blueprint in input.take(3)){
            val ruleMap = parseBlueprint(blueprint)

            var initState = State(oreRobot = 1, clayRobot = 0, obsidianRobot = 0, geodeRobot = 0,
                ore = 0, clay = 0, obsidian = 0, geode = 0, t=0)

            var stateQueue = mutableListOf(initState)

            var maxGeode = 0
            var maxState = State(oreRobot = 0, clayRobot = 0, obsidianRobot = 0, geodeRobot = 0,
                ore = 0, clay = 0, obsidian = 0, geode = 0, t=0)

            var time = 0

            val visited = hashSetOf<List<Int>>()

            val maxOreRobots = listOf(ruleMap["geode"]?.get("ore")!!,
                ruleMap["obsidian"]?.get("ore")!!,
                ruleMap["clay"]?.get("ore")!!,
                ruleMap["ore"]?.get("ore")!!).max()

            while (stateQueue.isNotEmpty()){
                var currentState = stateQueue.removeFirst()

                if (visited.contains(listOf(currentState.oreRobot, currentState.clayRobot, currentState.obsidianRobot, currentState.geodeRobot,
                    currentState.ore, currentState.clay, currentState.obsidian, currentState.geode))) continue

                visited.add(listOf(currentState.oreRobot, currentState.clayRobot, currentState.obsidianRobot, currentState.geodeRobot,
                    currentState.ore, currentState.clay, currentState.obsidian, currentState.geode))

                if (currentState.t == 33) {
                    break
                }

                if (currentState.t > time) {
                    time = currentState.t
                }

                if (currentState.t == 31 && currentState.geodeRobot == 0) continue
                if (currentState.t == 30 && currentState.obsidianRobot == 0) continue
                if (currentState.t == 29 && currentState.clayRobot == 0) continue

                var timeLeft = 31 - currentState.t

                if ((currentState.obsidian + currentState.obsidianRobot*timeLeft + (timeLeft-1)*(timeLeft)/2 < ruleMap["geode"]?.get("obsidian")!!) && (currentState.geodeRobot == 0)) {
                    continue
                }


                if (currentState.geode > maxGeode) {
                    maxState = State(currentState.oreRobot, currentState.clayRobot,
                        currentState.obsidianRobot, currentState.geodeRobot,
                        currentState.ore, currentState.clay, currentState.obsidian, currentState.geode, currentState.t)
                    maxGeode = currentState.geode
                }
                else if ((currentState.geode < maxGeode) && (currentState.t >= maxState.t)) {continue}


                if (currentState.t == 32) {
                    continue
                }
                currentState.print()


                var OreTillEnd = listOf(ruleMap["geode"]?.get("ore")!! + ruleMap["obsidian"]?.get("ore")!! + ruleMap["clay"]?.get("ore")!!).max()*(24-currentState.t)
                var ClayTillEnd = (ruleMap["obsidian"]?.get("clay")!!)*(24-currentState.t)
                var ObsidianTillEnd = (ruleMap["geode"]?.get("obsidian")!!)*(24-currentState.t)


                if (currentState.ore + currentState.oreRobot*(24 - currentState.t) < OreTillEnd
                    || currentState.clay + currentState.clayRobot*(24 - currentState.t) < ClayTillEnd
                    || currentState.obsidian + currentState.obsidianRobot*(24 - currentState.t) < ObsidianTillEnd) {
                    stateQueue.add(
                        State(
                            oreRobot = currentState.oreRobot,
                            clayRobot = currentState.clayRobot,
                            obsidianRobot = currentState.obsidianRobot,
                            geodeRobot = currentState.geodeRobot,
                            ore = currentState.ore + currentState.oreRobot,
                            clay = currentState.clay + currentState.clayRobot,
                            obsidian = currentState.obsidian + currentState.obsidianRobot,
                            geode = currentState.geode + currentState.geodeRobot,
                            t = currentState.t + 1
                        )
                    )
                }

                if ((currentState.ore >= (ruleMap["geode"]?.get("ore") ?: 0))
                    && (currentState.obsidian >= (ruleMap["geode"]?.get("obsidian") ?: 0))){
                    stateQueue.add(State(
                        oreRobot = currentState.oreRobot,
                        clayRobot = currentState.clayRobot,
                        obsidianRobot = currentState.obsidianRobot,
                        geodeRobot = currentState.geodeRobot+1,
                        ore = currentState.ore+currentState.oreRobot - ruleMap["geode"]?.get("ore")!!,
                        clay = currentState.clay+currentState.clayRobot,
                        obsidian = currentState.obsidian+currentState.obsidianRobot - ruleMap["geode"]?.get("obsidian")!!,
                        geode = currentState.geode + currentState.geodeRobot,
                        t = currentState.t+1))
                    continue
                }

                if ((currentState.ore >= (ruleMap["obsidian"]?.get("ore") ?: 0))
                    && (currentState.clay >= (ruleMap["obsidian"]?.get("clay") ?: 0))
                    && (currentState.obsidianRobot < (ruleMap["geode"]?.get("obsidian") ?: 0))){
                    stateQueue.add(State(
                        oreRobot = currentState.oreRobot,
                        clayRobot = currentState.clayRobot,
                        obsidianRobot = currentState.obsidianRobot+1,
                        geodeRobot = currentState.geodeRobot,
                        ore = currentState.ore+currentState.oreRobot - ruleMap["obsidian"]?.get("ore")!!,
                        clay = currentState.clay+currentState.clayRobot - ruleMap["obsidian"]?.get("clay")!!,
                        obsidian = currentState.obsidian+currentState.obsidianRobot,
                        geode = currentState.geode + currentState.geodeRobot,
                        t = currentState.t+1))
                }


                if ((currentState.ore >= (ruleMap["clay"]?.get("ore") ?: 0))
                    && (currentState.clayRobot < (ruleMap["obsidian"]?.get("clay") ?: 0))){
                        stateQueue.add(State(
                            oreRobot = currentState.oreRobot,
                            clayRobot = currentState.clayRobot+1,
                            obsidianRobot = currentState.obsidianRobot,
                            geodeRobot = currentState.geodeRobot,
                            ore = currentState.ore+currentState.oreRobot - ruleMap["clay"]?.get("ore")!!,
                            clay = currentState.clay+currentState.clayRobot,
                            obsidian = currentState.obsidian+currentState.obsidianRobot,
                            geode = currentState.geode + currentState.geodeRobot,
                            t = currentState.t+1))
                }


                if ((currentState.ore >= (ruleMap["ore"]?.get("ore") ?: 0))
                    && (currentState.oreRobot < maxOreRobots)) {
                    stateQueue.add(State(oreRobot = currentState.oreRobot+1,
                        clayRobot = currentState.clayRobot,
                        obsidianRobot = currentState.obsidianRobot,
                        geodeRobot = currentState.geodeRobot,
                        ore = currentState.ore+currentState.oreRobot - ruleMap["ore"]?.get("ore")!!,
                        clay = currentState.clay+currentState.clayRobot,
                        obsidian = currentState.obsidian+currentState.obsidianRobot,
                        geode = currentState.geode + currentState.geodeRobot,
                        t = currentState.t+1))
                }
            }
            println("max state")
            maxState.print()
            geodes.add(maxState.geode)
        }
        println(geodes)
        println(geodes.mapIndexed { index, i -> i*(index+1) }.toList().sum())
    }


    val testInput = readInput("Day19")
//    println(parseBlueprint(testInput[0]))
    part12(testInput)

}