// A debugging variable. Additional output is created if set to true.
const val debug = true

// Debugger function turned off for actual use
fun debuggerPrint(arg: String){
    if (debug){println(arg)}
}

