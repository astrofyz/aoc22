fun main() {
    class Directory(
        var size: Int = 0,
        val root: Directory? = null,
        var listdir: MutableMap<String, Directory>? = null
    ){
        constructor(root: Directory): this(0, root, mutableMapOf<String, Directory>())
        constructor(size: Int, root: Directory): this(size, root, null)
//        constructor()
    }

    fun Directory.addDir(name: String) {
        this.listdir?.set(name, Directory(this))
    }

    fun Directory.addFile(name: String, size: Int){
        this.listdir?.set(name, Directory(size, this))
        this.size += size
    }


    fun part1(input: List<String>): Int {
        var currentDir = Directory(listdir = mutableMapOf<String, Directory>()) // init root directory
        val directoriesSize = mutableListOf<Int>()
        for (line in input.slice(1 until input.size)){
            if (line.startsWith("$ cd") and !line.contains("..")){
                currentDir = currentDir.listdir!![line.drop(5)]!!
            }
            else if (line.startsWith("dir ")){
                currentDir.addDir(line.drop(4))
            }
            else if (line[0].isDigit()){
                currentDir.addFile(line.split(" ")[1], line.split(" ")[0].toInt())
            }
            else if (line.startsWith("$ cd ..")){
                currentDir.size = currentDir.listdir!!.values.sumOf{it.size}
                directoriesSize.add(currentDir.size)
                currentDir = currentDir.root!!
            }
        }
        while (currentDir.root!=null){
            currentDir.size = currentDir.listdir!!.values.sumOf{it.size}
            directoriesSize.add(currentDir.size)
            currentDir = currentDir.root!!
        }
        currentDir.size = currentDir.listdir!!.values.sumOf{it.size}
        directoriesSize.add(currentDir.size)

        println("part 1 : ${directoriesSize.filter { it < 100000 }.sum()}")

        val needToFree: Int = 30000000 - (70000000 - directoriesSize.max())
        println("part 2: ${directoriesSize.filter { it > needToFree }.min()}")

        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    part1(testInput)

    val input = readInput("Day07")
    part1(input)
}
