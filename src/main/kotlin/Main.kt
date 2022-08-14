fun main(args: Array<String>) {
    var input = ""
    val robotCenter: RobotCenter = RobotCenter()

    while(true){
        debuggerPrint("Awaiting Input:")
        input=readLine().toString()

        robotCenter.processCommand(input)
    }
}