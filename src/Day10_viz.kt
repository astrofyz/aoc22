import java.awt.Color
import java.awt.Graphics
import java.awt.Paint
import java.awt.Point
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JPanel

fun main() {
    class myPaint : JPanel() {
        private var paintImage = BufferedImage(500, 150, BufferedImage.TYPE_3BYTE_BGR)

        var darkPixels = mutableListOf<Pair<Int, Int>>()
        var brightPixels = mutableListOf<Pair<Int, Int>>()
        var currentPosition = Pair(0, 1)

        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            g.drawImage(paintImage, 0, 0, null)
        }

        // draw painting
        fun updatePaint() {
            val g: Graphics = paintImage.createGraphics()

            g.color = Color.DARK_GRAY
            for (pos in darkPixels){
                g.fillRect(pos.first+3+20, pos.second+3+20, 4, 4)
            }

            g.color = Color.RED
            for (pos in brightPixels){
                g.fillRect(pos.first+20, pos.second+20, 10, 10)
            }

            g.clearRect(0, currentPosition.second+20, 450, 10)

            g.color = Color.WHITE
            g.fillRect(currentPosition.first+10, currentPosition.second+20, 30, 10)

            // draw on paintImage using Graphics
            g.dispose()
            // repaint panel with new modified paint
            repaint()
        }

        @Throws(IOException::class)
        fun save(filename: String) {
            ImageIO.write(paintImage, "PNG", File("day10_frames/$filename.png"))
        }
    }

    fun part2(input: List<String>, pic: myPaint): Int {
        val CRT = MutableList(6, { MutableList(40, {0}) })
        var row: Int
        var col: Int
        var cycle = 0
        var X = 1
        for (line in input){
            if (line.startsWith("noop")){
                row = cycle / 40
                col = cycle % 40
                cycle += 1
                if (kotlin.math.abs(X-col) <= 1){
                    CRT[row][col] += 1
                    pic.brightPixels.add(Pair(10*col, 10*row))
                }
                else{pic.darkPixels.add(Pair(10*col, 10*row))}
                pic.currentPosition = Pair(X*10, 8*10)
                pic.updatePaint()
                pic.save(cycle.toString().padStart(3, '0'))
//                pic.clear()
            }
            else{
                repeat(2) {
                    row = cycle / 40
                    col = cycle % 40
                    cycle += 1
                    if (kotlin.math.abs(X - col) <= 1) {
                        CRT[row][col] += 1
                        pic.brightPixels.add(Pair(10*col, 10*row))
                    }
                    else{pic.darkPixels.add(Pair(10*col, 10*row))}
                    pic.currentPosition = Pair(X*10, 8*10)
                    pic.updatePaint()
                    pic.save(cycle.toString().padStart(3, '0'))
//                    pic.clear()
                }
                X += line.split(" ")[1].toInt()
            }
        }
        for (line in CRT){
            println(line.map { when(it){
                0 -> "."
                else -> "#"
            } }.joinToString(separator = ""))

        }
        return 0
    }

    var pic = myPaint()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val input = readInput("Day10")
    part2(input, pic)

}