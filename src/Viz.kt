import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
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

//fun part1(input: List<String>, ): Pair<Int, MutableSet<Pair<Int, Int>>> {
//    var headPos = Pair(0, 0)
//    var tailPos = Pair(0, 0)
//    val tailVisited = mutableSetOf(tailPos)
//    val headVisited = mutableSetOf(headPos)
//
//    for (step in input){
//        repeat(step.split(" ")[1].toInt()){
//            window.revalidate()
//            headPos = headPos.move(step.split(" ")[0])
//            tailPos = tailPos.adjustTail(headPos)
//            tailVisited.add(tailPos)
//            headVisited.add(headPos)
//            grid.fillRope(mutableListOf(headPos, tailPos))
////            grid.fillVisited(tailVisited.toMutableList())
//            Thread.sleep(2)
//            grid.rope.removeAt(0)
//            grid.rope.removeAt(0)
//
//        }
//    }
//    println("${tailVisited.maxOf { it.first }}, ${tailVisited.minOf { it.first }}, ${tailVisited.maxOf { it.second }}, ${tailVisited.minOf { it.second }}")
//    println("${headVisited.maxOf { it.first }}, ${headVisited.minOf { it.first }}, ${headVisited.maxOf { it.second }}, ${headVisited.minOf { it.second }}")

//    return Pair(tailVisited.size, tailVisited)
//}


fun main() {
    class myPaint : JPanel() {
        private var paintImage = BufferedImage(2700, 4500, BufferedImage.TYPE_3BYTE_BGR)

        var rope: MutableList<Pair<Int, Int>>
        val tailVisited: MutableList<Pair<Int, Int>>
        var ropeBefore: MutableList<Pair<Int, Int>>

        init {
            rope = mutableListOf()
            ropeBefore = mutableListOf()
            tailVisited = mutableListOf()
        }

        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            g.drawImage(paintImage, 0, 0, null)
        }

        fun updatePaint() {
            val g: Graphics = paintImage.createGraphics()

//            println("ropeBefore")
            for (knot in ropeBefore){
                g.clearRect(knot.second * 6 + 140, (knot.first * 6 + 20) + 200, 6, 6)
//                println("${knot.second * 6 + 140} ${(knot.first * 6 + 20) + 420}")
            }

//            println("ropeNow")
            g.color = Color.YELLOW
            for (knot in rope) {
                g.fillRect(knot.second * 6 + 140, (knot.first * 6 + 20) + 200, 6, 6)
//                println("${knot.second * 6 + 140} ${(knot.first * 6 + 20) + 420}")
            }
//            println("done")

            g.color = Color.GREEN
            for (visited in tailVisited) {
                g.fillRect(visited.second * 6 + 2 + 140, (visited.first * 6 - 2 + 20) + 200, 2, 2)
            }

            g.dispose()
            repaint()
        }

        @Throws(IOException::class)
        fun save(filename: String) {
            ImageIO.write(paintImage, "PNG", File("day9_frames/$filename.png"))
        }
    }

    fun part2(input: List<String>, pic: myPaint): Int {
        val knotsPos = MutableList(10) { Pair(0, 0) }
        val tailVisited = mutableSetOf(knotsPos[8])

        var fname = 0
        for (step in input) {
            val (directionOfStep, numberOfRepetition) = step.split(" ")
            repeat(numberOfRepetition.toInt()) {
                pic.ropeBefore = knotsPos.map{it.copy()}.toMutableList()
                knotsPos[0] = knotsPos[0].move(directionOfStep)
                for (i in 1 .. 9){
                    knotsPos[i] = knotsPos[i].adjustTail(knotsPos[i-1])
                }
                pic.tailVisited.add(knotsPos[9])
                pic.rope = knotsPos
                pic.updatePaint()
                pic.save(fname.toString().padStart(6, '0'))
                fname++
                if (fname % 100 == 0){
                    println(fname)
                }
            }
        }
        return tailVisited.size
    }

    val testInput = readInput("Day09")

    var pic = myPaint()

    part2(testInput, pic)
}



