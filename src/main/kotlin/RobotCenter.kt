/* The RobotCenter class holds the ID - robot map, as well as the regex for placing and activating robots.
    This way, input processing is separated from the main program.
 */
class RobotCenter {
    var robotMap: MutableMap<Int, Robot> = mutableMapOf()

    private val placeRegex = Regex("PLACE\\s\\d+,\\d+,\\w+")
    private val activateRegex = Regex("ROBOT\\s\\d+")

    var activeRobot: Robot? = null

    fun processCommand(input: String) {

        if (input.matches(placeRegex)) {
            debuggerPrint("Placing Robot...")
            val robotArgs = input.split(" ")[1].split(",").toTypedArray()

            try {
                this.activeRobot = Robot.getRobot(this.robotMap.size, robotArgs)
                this.robotMap[this.activeRobot!!.id] = this.activeRobot!!
            } catch (e: RobotException) {
                // Avoid halting the program if a robot is incorrectly created
            }

        } else if (input.matches(this.activateRegex)) {
            debuggerPrint("Activating Robot...")
            this.robotMap[input.split(" ").toTypedArray()[1].toInt()]?.let { this.activeRobot = it }

            // Assume that any other input is a command. Syntactically invalid commands are ignored by the robot.
        } else {
            debuggerPrint("Processing Command...")

            if (input == "REPORT") {
                println(this.summaryReport())
            }

            this.activeRobot?.processCommand(input)
        }
    }

    private fun summaryReport(): String {
        println(
            "${this.robotMap.size} ${
                if (this.robotMap.size == 1) {
                    "robot is"
                } else "robots are"
            } active."
        )
        return "The active robot's ID is ${this.activeRobot?.id ?: "therefore not set"}."
    }
}