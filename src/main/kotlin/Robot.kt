// This approach assumes the grid will always be a square.
const val GRID_MIN = 0
const val GRID_MAX = 4

/* Robot class, capable of moving around the grid and turning.
   Note that the cyclical 'facing' property matches to the (clockwise) cardinal directions as follows:
   0 = NORTH
   1 = EAST
   2 = SOUTH
   3 = WEST
   Robot coordinates assume that (0,0) is the south-western corner of the grid.
 */
class Robot(var id: Int, var xPos: Int, var yPos: Int, var facing: Int) {

    // Top-level logic for processing commands.
    fun processCommand(command: String): String{
        when (command) {
            "LEFT" -> this.turnLeft()
            "RIGHT" -> this.turnRight()
            "MOVE" -> this.move()
            "REPORT" -> return this.report()
            else -> debuggerPrint("$command is not a valid command.")
        }
        // Returns empty string if not reporting
        return ""
    }

    private fun turnLeft() {
        this.facing -= 1
        if (this.facing < 0) {
            this.facing = 3
        }
    }

    private fun turnRight() {
        this.facing = (this.facing + 1) % 4
    }

    private fun move() {
        debuggerPrint("Moving Robot...")
        when (facing) {
            0 -> {
                if (checkValidPosition(this.xPos, this.yPos + 1)) {
                    this.yPos += 1
                }
                else {
                    debuggerPrint("Cannot move that way.")
                }
            }

            1 -> {
                if (checkValidPosition(this.xPos + 1, this.yPos)) {
                    this.xPos += 1
                } else {
                    debuggerPrint("Cannot move that way.")
                }
            }

            2 -> {
                if (checkValidPosition(this.xPos, this.yPos - 1)) {
                    this.yPos -= 1
                } else {
                    debuggerPrint("Cannot move that way.")
                }
            }

            3 -> {
                if (checkValidPosition(this.xPos - 1, this.yPos)) {
                    this.xPos -= 1
                } else {
                    debuggerPrint("Cannot move that way.")
                }
            }
        }
    }

    // Converts the facing value to a String for printing
    private fun facingToString(): String {
        when (facing) {
            0 -> return "NORTH"
            1 -> return "EAST"
            2 -> return "SOUTH"
            3 -> return "WEST"
        }
        return "INVALID DIRECTION: ${this.facing}"
    }

    private fun report(): String = "${this.xPos},${this.yPos}," + this.facingToString()

    // Factory pattern as companion object for creating robots from String input and checking valid board positions
    companion object Factory {

        fun getRobot(robotCount: Int, robotArgs: Array<String>): Robot {

            // Convert the provided direction to an Int
            var robotFacing = when (robotArgs[2]) {
                "NORTH" -> 0
                "EAST" -> 1
                "SOUTH" -> 2
                "WEST" -> 3
                else -> throw RobotException("Invalid direction")
            }

            return if (checkValidPosition(robotArgs[0].toInt(), robotArgs[1].toInt())) {
                debuggerPrint(
                    "Created new robot with id ${robotCount + 1} at position (${robotArgs[0].toInt()}, " +
                            "${robotArgs[1].toInt()}) facing ${robotArgs[2]}"
                )
                Robot(robotCount + 1, robotArgs[0].toInt(), robotArgs[1].toInt(), robotFacing)
            } else {
                throw RobotException("Illegal coordinates")
            }
        }

        // Assumes multiple robots can share space.
        fun checkValidPosition(xVal: Int, yVal: Int): Boolean =
            (xVal in (GRID_MIN..GRID_MAX)) && (yVal in (GRID_MIN..GRID_MAX))
    }
}

class RobotException(message: String) : Exception(message)
