import org.junit.jupiter.api.Assertions.assertEquals

class RobotE2E {
    companion object {
        val robotCenter: RobotCenter = RobotCenter()
    }
    fun `Should place robot correctly`(){
        robotCenter.processCommand("PLACE 0,0,NORTH")
        assertEquals(String(ByteArrayOutputStream().toByteArray(), Charsets.UTF_8,),"")
    }
}