fun main(args: Array<String>) {
    var input = ""
    var output = ""
    val robotCenter: RobotCenter = RobotCenter()

    while (true) {
        debuggerPrint("Awaiting Input:")
        input = readLine().toString()
        output = robotCenter.processCommand(input)
        if (output.isNotEmpty()) {
            println(output)
        }
    }
}