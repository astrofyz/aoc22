import java.io.File

fun main() {
    class Point(var x: Int, var y: Int){}

    class Shape(var points: MutableList<Point>){}


    fun Shape.rightmost(): Int{
        return this.points.map { it.x }.max()
    }

    fun Shape.leftmost(): Int{
        return this.points.map { it.x }.min()
    }

    fun Shape.lowest(): Int{
        return this.points.map { it.y }.min()
    }

    fun Shape.updateStart(floor: Int): Shape{
        val leftref = this.leftmost()
        val lowref = this.lowest()
        for ((i, point) in this.points.withIndex()){
            this.points[i] = Point(point.x-leftref+2, point.y-lowref+floor)
        }
        return this
    }

    fun Shape.updateJet(dir: Char, chamber: MutableList<MutableList<Int>>): Shape{
        if ((dir == '>')&&(this.rightmost() < 6)) {
//            println("$dir ${this.points.map { chamber[it.y][it.x + 1] == 0}.toList().count{it}} ${this.points.size}")
            if (this.points.map { chamber[it.y][it.x + 1] == 0 }.toList().count{it} == this.points.size) {
                for ((i, point) in this.points.withIndex()) {
                    this.points[i] = Point(point.x + 1, point.y)
                }
                return this
            }
            else return this
        }
        else if ((dir == '<')&&(this.leftmost() > 0)){
//            println("$dir ${this.points.map { chamber[it.y][it.x - 1] == 0 }.toList().count()} ${this.points.size}")
            if (this.points.map { chamber[it.y][it.x - 1] == 0 }.toList().count{it} == this.points.size) {
                for ((i, point) in this.points.withIndex()) {
                    this.points[i] = Point(point.x - 1, point.y)
                }
                return this
            }
            else return this
        }
        else return this
    }

    fun MutableList<MutableList<Int>>.print(n: Int = this.size){
        for (k in this.size-1 downTo  this.size-n){
            println(this[k].joinToString(separator = "").replace('0', '.').replace('1', '#'))
        }
    }

    fun Shape.check(chamber: MutableList<MutableList<Int>>): Boolean{
//        println(this.points.map{chamber[it.y-1][it.x] == 1}.toList())
//        println(this.points.map{chamber[it.y-1][it.x] == 1}.toList().all{it == false})
        return this.points.map{chamber[it.y-1][it.x] == 1}.toList().all{it == false}
    }

    fun Shape.updateFall(chamber: MutableList<MutableList<Int>>): Pair<Boolean, Shape> {
        return if (!this.check(chamber)) Pair(false, this)
        else{
            for ((i, point) in this.points.withIndex()){
                this.points[i] = Point(point.x, point.y-1)
            }
            Pair(true, this)
        }
    }

    fun MutableList<MutableList<Int>>.update(shape: Shape): MutableList<MutableList<Int>> {
        for (point in shape.points){
            this[point.y][point.x] = 1
        }
        return this
    }


    fun part1(input: String){
        var defaultShape = Shape(mutableListOf(Point(2,0), Point(3, 0), Point(4,0), Point(5,0)))
        var countShapes = 0
        var floor = 0
        var ystart = 0
        var actionCount = 0

        var chamber = MutableList(5) { MutableList<Int>(7) { 0 } }
        chamber[0].mapIndexed{index, i -> chamber[0][index] = 1 }

        val shapes = mutableMapOf<Int, Shape>()

        shapes[0] = Shape(mutableListOf(Point(2,0), Point(3, 0), Point(4,0), Point(5,0)))
        shapes[1] = Shape(mutableListOf(Point(2,1), Point(3, 1), Point(4,1), Point(3,0), Point(3, 2)))
        shapes[2] = Shape(mutableListOf(Point(2,0), Point(3, 0), Point(4,0), Point(4,1), Point(4, 2)))
        shapes[3] = Shape(mutableListOf(Point(2,0), Point(2, 1), Point(2,2), Point(2,3)))
        shapes[4] = Shape(mutableListOf(Point(2,0), Point(3, 0), Point(2,1), Point(3,1)))

        while (countShapes < 10000){
//            println("number of shapes $countShapes, key of shape: ${countShapes % 5}")
            var shape = shapes.getOrDefault(countShapes % 5, defaultShape)
//            println("check floor ${IntRange(0, 6).map{j -> chamber.map { it[j] }.toList().indexOfLast { it > 0 }}.toList()}")
            floor = IntRange(0, 6).map{j -> chamber.map { it[j] }.toList().indexOfLast { it > 0 }}.toList().max()
            ystart = floor + 4
            if (ystart + 4 > chamber.size){while (chamber.size <= ystart+3) chamber.add(MutableList<Int>(7) { 0 })}
            shape = shape.updateStart(ystart)
//            println("starting shape ${shape.lowest()} ${shape.rightmost()} ${shape.leftmost()}")
            var moveFlag = true
            move@while (moveFlag){
                for (action in listOf("jet", "fall")){
                    when (action) {
                        "jet" -> {
//                            println("${input[actionCount % input.length]}")
//                            println("shape befor action ${shape.lowest()} ${shape.rightmost()} ${shape.leftmost()}")
                            shape = shape.updateJet(input[actionCount % input.length], chamber)
//                            println("shape after action ${shape.lowest()} ${shape.rightmost()} ${shape.leftmost()}")
                            moveFlag = shape.check(chamber)
//                            println("move? $moveFlag")
                            actionCount++
                            if (!moveFlag){
                                chamber = chamber.update(shape)
                                break@move
                            }
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
                        }
                    }
                }
            }
//            println("finished shape $countShapes")
            countShapes++
//            chamber.print()
        }
        println(IntRange(0, 6).map{j -> chamber.map { it[j] }.toList().indexOfLast { it > 0 }}.toList())
//        chamber.print(20)

    }


    // test if implementation meets criteria from the description, like:
    val testInput = File("src", "Day17.txt").readText()
    part1(testInput)
//    part2(testInput)


    val input = readInput("Day17")
//    part1(testInput)
//    part2(input)
}