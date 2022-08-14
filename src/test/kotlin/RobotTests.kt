import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RobotTests {
    companion object {
        var robotCenter: RobotCenter = RobotCenter()
    }

    @BeforeEach
    fun setUp() {
        robotCenter.destroyRobots()
    }

    @Test
    fun `Should place robot correctly`() {
        robotCenter.processCommand("PLACE 0,0,NORTH")
        assertEquals(
            "1 robot is active.\n" +
                    "The active robot's ID is 1.\n" +
                    "0,0,NORTH", robotCenter.processCommand("REPORT")
        )
    }

    @Test
    fun `should not place invalid robots`() {
        robotCenter.processCommand("PLACE 90,0,NORTH")
        robotCenter.processCommand("PLACE -90,0,NORTH")
        robotCenter.processCommand("PLACE 0,2,WIDDER-SHINS")
        assertEquals(
            "0 robots are active.\n" +
                    "The active robot's ID is therefore not set.", robotCenter.processCommand("REPORT")
        )
    }

    @Test
    fun `Should move robot`() {
        robotCenter.processCommand("PLACE 0,0,NORTH")
        robotCenter.processCommand("MOVE")
        assertEquals(
            "1 robot is active.\n" +
                    "The active robot's ID is 1.\n" +
                    "0,1,NORTH", robotCenter.processCommand("REPORT")
        )
    }

    @Test
    fun `Should turn robot`() {
        robotCenter.processCommand("PLACE 0,0,NORTH")
        robotCenter.processCommand("LEFT")
        assertEquals(
            "1 robot is active.\n" +
                    "The active robot's ID is 1.\n" +
                    "0,0,WEST", robotCenter.processCommand("REPORT")
        )
        robotCenter.processCommand("RIGHT")
        robotCenter.processCommand("RIGHT")
        assertEquals(
            "1 robot is active.\n" +
                    "The active robot's ID is 1.\n" +
                    "0,0,EAST", robotCenter.processCommand("REPORT")
        )
    }

    @Test
    fun `Should not move robot off grid`() {
        robotCenter.processCommand("PLACE 4,4,EAST")
        assertEquals(
            "1 robot is active.\n" +
                    "The active robot's ID is 1.\n" +
                    "4,4,EAST", robotCenter.processCommand("REPORT")
        )
        robotCenter.processCommand("MOVE")
        robotCenter.processCommand("MOVE")
        robotCenter.processCommand("LEFT")
        robotCenter.processCommand("MOVE")
        robotCenter.processCommand("MOVE")
        assertEquals(
            "1 robot is active.\n" +
                    "The active robot's ID is 1.\n" +
                    "4,4,NORTH", robotCenter.processCommand("REPORT")
        )
    }

    @Test
    fun `Should place multiple robots correctly`() {
        robotCenter.processCommand("PLACE 0,0,NORTH")
        robotCenter.processCommand("PLACE 1,1,EAST")
        assertEquals(
            "2 robots are active.\n" +
                    "The active robot's ID is 2.\n" +
                    "1,1,EAST", robotCenter.processCommand("REPORT")
        )
        robotCenter.processCommand("PLACE 2,2,SOUTH")
        assertEquals(
            "3 robots are active.\n" +
                    "The active robot's ID is 3.\n" +
                    "2,2,SOUTH", robotCenter.processCommand("REPORT")
        )
    }

    @Test
    fun `Should activate and move second robot`() {
        robotCenter.processCommand("PLACE 0,0,NORTH")
        robotCenter.processCommand("MOVE")
        robotCenter.processCommand("PLACE 1,1,SOUTH")
        assertEquals(
            "2 robots are active.\n" +
                    "The active robot's ID is 2.\n" +
                    "1,1,SOUTH", robotCenter.processCommand("REPORT")
        )
        robotCenter.processCommand("MOVE")
        assertEquals(
            "2 robots are active.\n" +
                    "The active robot's ID is 2.\n" +
                    "1,0,SOUTH", robotCenter.processCommand("REPORT")
        )
        robotCenter.processCommand("ROBOT 1")
        assertEquals(
            "2 robots are active.\n" +
                    "The active robot's ID is 1.\n" +
                    "0,1,NORTH", robotCenter.processCommand("REPORT")
        )
    }

    @Test
    fun `Should ignore invalid commands`() {
        robotCenter.processCommand("PLACE 2,2,WEST")
        robotCenter.processCommand("MOVE")
        robotCenter.processCommand("MOVE")
        robotCenter.processCommand("ROTATE")
        robotCenter.processCommand("LEFT")
        robotCenter.processCommand("MOVE")
        robotCenter.processCommand("DANCE")
        robotCenter.processCommand("MOVE")
        robotCenter.processCommand("PLACE @#@#@#")
        robotCenter.processCommand("ROBOT 77")
        assertEquals(
            "1 robot is active.\n" +
                    "The active robot's ID is 1.\n" +
                    "0,0,SOUTH", robotCenter.processCommand("REPORT")
        )
    }
}