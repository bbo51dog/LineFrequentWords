package net.bbo51dog.lineFrequentWords

import com.atilika.kuromoji.ipadic.Tokenizer
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
            val messageRegex = Regex("""([0-1][0-9]|2[0-3]):[0-5][0-9]\t.+\t.+""")
            val replaceRegex = Regex("""([0-1][0-9]|2[0-3]):[0-5][0-9]\t.+\t""")
            val tokenizer = Tokenizer()
            val words = mutableMapOf<String, Int>()
            var messageCount = 0
            logFile.forEachLine {
                if (!messageRegex.matches(it)) {
                    return@forEachLine
                }
                var message = it.replace(replaceRegex, "")
                messageCount++
                if (message == "[スタンプ]" || message == "[写真]" || message == "[動画]") {
                    return@forEachLine
                }
                var tokens = tokenizer.tokenize(message)
                tokens.forEach {token ->
                    words[token.surface] = if (words.containsKey(token.surface)) {
                        words[token.surface]!!.toInt() + 1
                    } else {
                        1
                    }
                }
            }
            var sorted: MutableMap<String, Int> = words.toList().sortedByDescending{ it.second }.toMap().toMutableMap()
            for (i in 1..10) {
                if (sorted.isEmpty()) break
                var word = sorted.keys.first()
                var count = sorted[word]
                println("$i : $word ($count)")
                sorted.remove(word)
            }
            println("$messageCount M messages")
        }
    }
}