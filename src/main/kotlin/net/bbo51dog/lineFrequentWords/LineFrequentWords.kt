package net.bbo51dog.lineFrequentWords

import java.io.File
import kotlin.system.exitProcess

class LineFrequentWords {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val fileName = args.getOrNull(0)
            if (fileName === null) {
                println("Please enter the LINE log file path as the first argument")
                exitProcess(1)
            }
            val logFile = File(fileName)
            val messageRegex = Regex("""[0-1][0-9]|2[0-3]:[0-5][0-9]\t.*\t.*""")
            val replaceRegex = Regex("""[0-1][0-9]|2[0-3]:[0-5][0-9]\t.*\t""")
            logFile.forEachLine {
                if (!messageRegex.matches(it)) {
                    return@forEachLine
                }
                var message = it.replace(replaceRegex, "")
            }
        }
    }
}