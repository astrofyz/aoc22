import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import javax.swing.JFrame
import javax.swing.JPanel

fun abs(a: Int, b: Int): Int{
    return maxOf(a, b) - minOf(a, b)
}

fun sign(a: Int): Int{
    if (a > 0) return 1
    else if (a < 0) return -1
    return 0
}

fun Pair<Int, Int>.move(moveDir: String):Pair<Int, Int>{
    return when (moveDir){
        "R" -> Pair(this.first, this.second+1)
        "L" -> Pair(this.first, this.second-1)
        "U" -> Pair(this.first+1, this.second)
        "D" -> Pair(this.first-1, this.second)
        else -> this
    }
}

fun Pair<Int, Int>.adjustTail(head: Pair<Int, Int>): Pair<Int, Int>{
    return when(Pair(abs(head.first, this.first), abs(head.second, this.second))){
        Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(0, 0) -> this
        else -> Pair(this.first+sign(head.first-this.first),
            this.second+sign(head.second - this.second))
    }
}

fun part1(input: List<String>, window: JFrame, grid: CoreControl.Grid): Pair<Int, MutableSet<Pair<Int, Int>>> {
    var headPos = Pair(0, 0)
    var tailPos = Pair(0, 0)
    val tailVisited = mutableSetOf(tailPos)
    val headVisited = mutableSetOf(headPos)

    for (step in input){
        repeat(step.split(" ")[1].toInt()){
            window.revalidate()
            headPos = headPos.move(step.split(" ")[0])
            tailPos = tailPos.adjustTail(headPos)
            tailVisited.add(tailPos)
            headVisited.add(headPos)
            grid.fillRope(mutableListOf(headPos, tailPos))
//            grid.fillVisited(tailVisited.toMutableList())
            Thread.sleep(2)
            grid.rope.removeAt(0)
            grid.rope.removeAt(0)

        }
    }
//    println("${tailVisited.maxOf { it.first }}, ${tailVisited.minOf { it.first }}, ${tailVisited.maxOf { it.second }}, ${tailVisited.minOf { it.second }}")
//    println("${headVisited.maxOf { it.first }}, ${headVisited.minOf { it.first }}, ${headVisited.maxOf { it.second }}, ${headVisited.minOf { it.second }}")

    return Pair(tailVisited.size, tailVisited)
}

fun part2(input: List<String>, window: JFrame, grid: CoreControl.Grid): Int {
    val knotsPos = MutableList(10) { Pair(0, 0) }
    val tailVisited = mutableSetOf(knotsPos[8])

    for (step in input) {
        val (directionOfStep, numberOfRepetition) = step.split(" ")
        repeat(numberOfRepetition.toInt()) {
            window.revalidate()
            knotsPos[0] = knotsPos[0].move(directionOfStep)
            for (i in 1 .. 9){
                knotsPos[i] = knotsPos[i].adjustTail(knotsPos[i-1])
            }
            tailVisited.add(knotsPos[9])
            grid.fillRope(knotsPos)
//            grid.fillVisited(tailVisited.toMutableList())
            Thread.sleep(5)
            grid.rope = mutableListOf()

        }
    }
    return tailVisited.size
}

object CoreControl {
    @JvmStatic
    fun main(a: Array<String>) {
        val grid = Grid()
        val window = JFrame()
        window.setSize(2000, 2000)
        window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        val testInput = readInput("Day09")
        window.isVisible = true
        window.add(grid)

//        part1(testInput, window, grid)
        part2(testInput, window, grid)
    }

    class Grid : JPanel() {
        var rope: MutableList<Point>
        val tailVisited: MutableList<Point>

        init {
            rope = mutableListOf()
            tailVisited = mutableListOf()
        }

        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)

            for (knot in rope) {
                g.color = Color.RED
                g.fillRect(knot.x * 6 + 140, 300 - (knot.y * 6 + 20), 6, 6)
            }

            for (visited in tailVisited) {
                g.color = Color.BLUE
                g.fillRect(visited.x * 6 + 2 + 140, 300 - (visited.y * 6 - 2 + 20), 2, 2)
            }
        }

        fun fillRope(currentRope: MutableList<Pair<Int, Int>>) {
            for (knot in currentRope) {
                rope.add(Point(knot.second, knot.first))
            }
            repaint()
        }

        fun fillVisited(visited: MutableList<Pair<Int, Int>>) {
            for (cell in visited) {
                tailVisited.add(Point(cell.second, cell.first))
            }
            repaint()
        }
    }
}


