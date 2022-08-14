// Top level constants for the min and max grid coordinates. This approach assumes the grid will always be a square.
const val GRID_MIN = 0
const val GRID_MAX = 4

// A debugging variable. Additional output is created if set to true.
const val verbose = false

fun main(args: Array<String>) {
    var input = ""
    val robotCenter: RobotCenter = RobotCenter()

    // Continuously read from user input.
    while(true){
        verbosePrint("Awaiting Input:")
        input=readLine().toString()

        // Pass input for processing.
        robotCenter.processCommand(input)
    }
}

fun verbosePrint(arg: String){
    if (verbose){println(arg)}
}

/* The RobotCenter class holds the ID - robot map, as well as the regex for placing and activating robots.
    This way, input processing is separated from the main program.
 */
class RobotCenter{
    var robotMap: MutableMap<Int, Robot> = mutableMapOf()

    // The REGEX patterns that identify commands with arguments.
    val placeRegex = Regex("PLACE\\s{1}\\d+,\\d+,\\w+")
    val activateRegex = Regex("ROBOT\\s{1}\\d+")

    // The tracker for the currently active robot, which commands are issued to.
    var activeRobot: Robot? = null

    fun processCommand(input: String){

        // In the case of placing a new robot on the board, the first 6 characters ("PLACE ") must be removed.
        if (input.matches(placeRegex)){
            verbosePrint("Placing Robot...")
            var robotArgs = input.slice(6 until input.length).split(",").toTypedArray()

            // If the factory is able to create a new robot from the command, it becomes the active robot.
            Robot.getRobot(this.robotMap.size, robotArgs)?.let{
                this.robotMap[it.id] = it
                this.activeRobot = it
            }


        // If a new robot is activated, the number is sliced from the command and used as the map index.
        } else if(input.matches(this.activateRegex)) {
            verbosePrint("Activating Robot...")
            this.robotMap[input.split(" ").toTypedArray()[1].toInt()]?.let{this.activeRobot=it}

        // Assume that any other input is a command. Non-valid commands are rejected by the robot.
        } else {
            verbosePrint("Processing Command...")

            // The REPORT command also outputs a text summary.
            if (input == "REPORT") {
                this.summaryReport()
            }

            this.activeRobot?.processCommand(input)
        }
    }

    // The summary report outputs how many robots are active, and which is the active one.
    private fun summaryReport(){
        println("${this.robotMap.size} ${if (this.robotMap.size==1){"robot is"} else "robots are"} active.")
        println("The active robot's ID is ${this.activeRobot?.id ?: "therefore not set"}.")
    }
}

/* Robot class, capable of moving around the grid and turning.
   Note that the cyclical 'facing' property matches to the (clockwise) cardinal directions as follows:
   0 = NORTH
   1 = EAST
   2 = SOUTH
   3 = WEST
   Robot coordinates assume that (0,0) is the south-western corner of the grid.
 */
class Robot(var id: Int, var xPos: Int, var yPos: Int, var facing: Int){

    // Top-level logic for processing commands.
    fun processCommand(command: String){
        when (command) {
            "LEFT" -> this.turnLeft()
            "RIGHT" -> this.turnRight()
            "MOVE" -> this.move()
            "REPORT" -> this.report()
            else -> verbosePrint("$command is not a valid command.")
        }
    }

    // The robot turns left by decrementing the facing value. If it turns left from NORTH (0), the value is reset to 3.
    private fun turnLeft(){
        this.facing-=1
        if (this.facing <0) {
            this.facing = 3
        }
    }

    // The robot turns right by decrementing the facing value. If it turns left from WEST (3), the value is reset to 0.
    private fun turnRight(){
        this.facing = (this.facing+1)%4
    }

    // Moves the robot 1 step forward, checking that such a move will not cause it to fall from the table
    private fun move(){
        when (facing){
            // If facing north, check that the position at y+1 is valid
            0 -> if(checkValidPosition(this.xPos,this.yPos+1)) {this.yPos+=1}

            // If facing east, check that the position at x+1 is valid
            1 -> if(checkValidPosition(this.xPos+1,this.yPos)) {this.xPos+=1}

            // If facing south, check that the position at y-1 is valid
            2 -> if(checkValidPosition(this.xPos, this.yPos-1)) {this.yPos-=1}

            // If facing west, check that the position at x-1 is valid
            3 -> if(checkValidPosition(this.xPos-1, this.yPos)) {this.xPos-=1}
        }
    }

    // Converts the facing value to a String for printing
    private fun facingToString(): String{
        when (facing){
            0 -> return "NORTH"
            1 -> return "EAST"
            2 -> return "SOUTH"
            3 -> return "WEST"
        }
        return "INVALID DIRECTION: ${this.facing}"
    }

    // Outputs a report of where the robot is on the grid coordinates, and what direction it is facing.
    private fun report(){
        println("${this.xPos},${this.yPos},"+ this.facingToString())
    }

    // Factory pattern as companion object for creating robots from String input and checking valid board positions
    companion object Factory{

        // Creates a new robot
        fun getRobot(robotCount: Int, robotArgs: Array<String>): Robot?{

            // Convert the provided direction to an Int
            var robotFacing = when (robotArgs[2]){
                "NORTH" -> 0
                "EAST" -> 1
                "SOUTH" -> 2
                "WEST" -> 3
                else -> {verbosePrint("Invalid direction"); return null}
            }

            // Check to ensure position is valid
            if (checkValidPosition(robotArgs[0].toInt(), robotArgs[1].toInt())) {
                verbosePrint(
                    "Created new robot with id ${robotCount + 1} at position (${robotArgs[0].toInt()}, " +
                            "${robotArgs[1].toInt()}) facing ${robotArgs[2]}"
                )
                return Robot(robotCount + 1, robotArgs[0].toInt(), robotArgs[1].toInt(), robotFacing)
            }
            verbosePrint("Could not place robot at specified coordinates")
            return null
        }

        // Checks that coordinates given are valid according to board size. Assumes multiple robots can share space.
        fun checkValidPosition(xVal: Int, yVal: Int): Boolean {
            return ((xVal in GRID_MIN..GRID_MAX) && (yVal in GRID_MIN..GRID_MAX))
        }
    }
}