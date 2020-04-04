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
        }
    }
}