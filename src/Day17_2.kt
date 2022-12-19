import java.io.File
import kotlin.math.max

fun main() {
    fun Pair<Long, Long>.x(): Long{
        return this.first
    }

    fun Pair<Long, Long>.y(): Long{
        return this.second
    }

    class Shape(var points: MutableList<Pair<Long, Long>>){}


    fun Shape.rightmost(): Long{
        return this.points.map { it.x() }.max()
    }

    fun Shape.leftmost(): Long{
        return this.points.map { it.x() }.min()
    }

    fun Shape.lowest(): Long{
        return this.points.map { it.y() }.min()
    }

    fun Shape.updateStart(floor: Long): Shape{
        val leftref = this.leftmost()
        val lowref = this.lowest()
        for ((i, point) in this.points.withIndex()){
            this.points[i] = Pair(point.x() -leftref+2, point.y() -lowref+floor)
        }
        return this
    }

    fun Shape.updateJet(dir: Char, chamber: MutableSet<Pair<Long, Long>>): Shape{
        if ((dir == '>')&&(this.rightmost() < 6)) {
//            println("$dir ${this.points.map { chamber[it.x+1] == it.y }.toList().count{it}} $chamber")
            if (this.points.map { Pair(it.x()+1, it.y()) in chamber }.toList().count{it} == 0) {
                for ((i, point) in this.points.withIndex()) {
                    this.points[i] = Pair(point.x() + 1, point.y())
                }
                return this
            }
            else return this
        }
        else if ((dir == '<')&&(this.leftmost() > 0)){
//            println("$dir ${this.points.map { chamber[it.y][it.x - 1] == 0 }.toList().count()} ${this.points.size}")
            if (this.points.map { Pair(it.x()-1, it.y()) in chamber }.toList().count{it} == 0) {
                for ((i, point) in this.points.withIndex()) {
                    this.points[i] = Pair(point.x() - 1, point.y())
                }
                return this
            }
            else return this
        }
        else return this
    }


    fun Shape.check(chamber: MutableSet<Pair<Long, Long>>): Boolean{
//        println(this.points.map{chamber[it.y-1][it.x] == 1}.toList())
//        println(this.points.map{chamber[it.y-1][it.x] == 1}.toList().all{it == false})
//        chamber.map { print("chamber $it") }
//        this.points.map { print("checking... $it")}
//        println("${this.points.map { Pair(it.x(), it.y()-1) in chamber }.toList()}")
        return (this.points.map { Pair(it.x(), it.y()-1) in chamber }.toList().count{it} == 0)
    }

    fun Shape.updateFall(chamber: MutableSet<Pair<Long, Long>>): Pair<Boolean, Shape> {
        return if (!this.check(chamber)) Pair(false, this)
        else{
            for ((i, point) in this.points.withIndex()){
                this.points[i] = Pair(point.x(), point.y()-1)
            }
            Pair(true, this)
        }
    }

    fun MutableSet<Pair<Long, Long>>.update(shape: Shape): MutableSet<Pair<Long, Long>> {
//        shape.points.map { println("${it.x} ${it.y}") }
        for (point in shape.points){
            this.add(point)
        }
        return this
    }


    fun part1(input: String){
        var defaultShape = Shape(mutableListOf(Pair(2,0), Pair(3, 0), Pair(4,0), Pair(5,0)))
        var countShapes: Long = 0
        var floor: Long = 0
        var ystart: Long = 0
        var actionCount: Long = 0

        var savedAction: Int = 0
        var savedShape: Int = 0
        var savedState = mutableListOf<Long>()

        var chamber = mutableSetOf<Pair<Long, Long>>()
        for (i in 0..6){chamber.add(Pair(i.toLong(), 0.toLong()))}

        val shapes = mutableMapOf<Int, Shape>()

        shapes[0] = Shape(mutableListOf(Pair(2,0), Pair(3, 0), Pair(4,0), Pair(5,0)))
        shapes[1] = Shape(mutableListOf(Pair(2,1), Pair(3, 1), Pair(4,1), Pair(3,0), Pair(3, 2)))
        shapes[2] = Shape(mutableListOf(Pair(2,0), Pair(3, 0), Pair(4,0), Pair(4,1), Pair(4, 2)))
        shapes[3] = Shape(mutableListOf(Pair(2,0), Pair(2, 1), Pair(2,2), Pair(2,3)))
        shapes[4] = Shape(mutableListOf(Pair(2,0), Pair(3, 0), Pair(2,1), Pair(3,1)))

        stop@while (countShapes < 750000){
//            println("number of shapes $countShapes, key of shape: ${countShapes % 5}")
            var shape = shapes.getOrDefault((countShapes % 5).toInt(), defaultShape)
//            println("check floor ${IntRange(0, 6).map{j -> chamber.map { it[j] }.toList().indexOfLast { it > 0 }}.toList()}")
            floor = chamber.map { it.y() }.toList().max()
            ystart = floor + 4
            shape = shape.updateStart(ystart.toLong())
//            println("starting shape ${shape.lowest()} ${shape.rightmost()} ${shape.leftmost()}")
            var moveFlag = true
            move@while (moveFlag){
                for (action in listOf("jet", "fall")){
                    when (action) {
                        "jet" -> {
//                            println("${input[actionCount % input.length]}")
//                            println("shape befor action ${shape.lowest()} ${shape.rightmost()} ${shape.leftmost()}")
                            shape = shape.updateJet(input[(actionCount % input.length).toInt()], chamber)
//                            println("shape after action ${shape.lowest()} ${shape.rightmost()} ${shape.leftmost()}")
                            moveFlag = shape.check(chamber)
//                            println("move? $moveFlag")
                            actionCount++
                            if (!moveFlag){
                                chamber = chamber.update(shape)
                                break@move
                            }
                            if (shape.lowest() < 0) break@stop
                        }
                        "fall" -> {
//                            println(action)
//                            println("shape befor action ${shape.lowest()} ${shape.rightmost()} ${shape.leftmost()}")
                            var res = shape.updateFall(chamber)
                            moveFlag = res.first
                            shape = res.second
//                            println("shape after action ${shape.lowest()} ${shape.rightmost()} ${shape.leftmost()}")
//                            println("move? $moveFlag")
                            if (!moveFlag){
                                chamber = chamber.update(shape)
                                break@move
                            }
                            if (shape.lowest() < 0) break@stop
                        }
                    }
                }
            }
            countShapes++

            var maxHeight = IntRange(0,6).map{index ->chamber.filter { it.x().toInt() == index }.map { elem -> elem.y() }.toList().max().toLong()}.toList()

            if (countShapes.toInt() == 560){
                savedAction = ((actionCount - 1) % input.length).toInt()
                savedShape = ((countShapes - 1) % 5).toInt()
                savedState = maxHeight.map { it - maxHeight.min() }.toMutableList()
                println("saved action $savedAction, saved shape $savedShape saved state $savedState")
            }
            else if (countShapes > 560.toLong()){
                var currentAction = ((actionCount - 1) % input.length).toInt()
                var currentShape = ((countShapes - 1) % 5).toInt()
                var currentState = maxHeight.map { it - maxHeight.min() }.toList()

//                println("$savedShape $currentShape $savedAction $currentAction $savedState $currentState")

//                if ((currentShape == savedShape)&&(currentAction == savedAction)&&(currentState == savedState)){
//                    println("found duplicate! $countShapes $maxHeight")
//                }
            }
//            var maxHeight = IntRange(0,6).map{index ->chamber.filter { it.x().toInt() == index }.map { elem -> elem.y() }.toList().max().toLong()}.toList()
//            if (maxHeight.zipWithNext().map { it.first == it.second }.toList().count { it } == 7) {println("$maxHeight $countShapes")}  // поиск ещё одного ровного пола, не нашёл
            if ((countShapes % 1000.toLong()) == 0.toLong()){
                var maxHeight = IntRange(0,6).map{index ->chamber.filter { it.x().toInt() == index }.map { elem -> elem.y() }.toList().max().toLong()}.toList()
                for (i in 0..6){
                    chamber = chamber.subtract(chamber.filter { (it.x().toInt() == i)&&(it.y() < maxHeight[i]-1000) }.toMutableSet()) as MutableSet<Pair<Long, Long>>
                }
//                for (index in 0..6){
//                println("$countShapes ${IntRange(0,6).map{index ->chamber.filter { it.x().toInt() == index }.map { elem -> elem.y() }.toList().max()}.toList()}")
            }
            if (countShapes % 1725.toLong() == 0.toLong()){
                var maxHeight = IntRange(0,6).map{index ->chamber.filter { it.x().toInt() == index }.map { elem -> elem.y() }.toList().max().toLong()}.toList()
               println("1725 divider $countShapes, ${maxHeight.max()} ${maxHeight.min()} ${maxHeight.map { it - maxHeight.min() }.toList()}, ${((actionCount - 1) % input.length).toInt()}, ${((countShapes - 1) % 5).toInt()}")
            }
            if (countShapes % 1725.toLong() == 1600.toLong()){
                var maxHeight = IntRange(0,6).map{index ->chamber.filter { it.x().toInt() == index }.map { elem -> elem.y() }.toList().max().toLong()}.toList()
                println("1600 divider $countShapes, ${maxHeight.max()} ${maxHeight.min()} ${maxHeight.map { it - maxHeight.min() }.toList()}, ${((actionCount - 1) % input.length).toInt()}, ${((countShapes - 1) % 5).toInt()}")
            }
        }
        println(chamber.map { it.y() }.toList().max())
//        println(chamber.filter { it.y > 20 })

    }


    // test if implementation meets criteria from the description, like:
    val testInput = File("src", "Day17.txt").readText()
    part1(testInput)
//    println(testInput.length)
//    println("${testInput.length * 5}")
//    part2(testInput)


    val input = readInput("Day17")
//    part1(testInput)
//    part2(input)
}