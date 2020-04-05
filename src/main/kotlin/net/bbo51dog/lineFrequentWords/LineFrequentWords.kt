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
            val ignorePart = setOf(
                "助詞",
                "助動詞",
                "記号",
                "フィラー"
            )
            val tokenizer = Tokenizer()
            val words = mutableMapOf<String, Int>()
            var messageCount = 0
            var stampCount = 0
            var imageCount = 0
            var movieCount = 0
            logFile.forEachLine {
                if (!messageRegex.matches(it)) {
                    return@forEachLine
                }
                val message = it.replace(replaceRegex, "")
                messageCount++
                if (message == "[スタンプ]") {
                    stampCount++
                    return@forEachLine
                }
                if (message == "[写真]") {
                    imageCount++
                    return@forEachLine
                }
                if (message == "[動画]") {
                    movieCount++
                    return@forEachLine
                }
                val tokens = tokenizer.tokenize(message)
                tokens.forEach {token ->
                    if (ignorePart.contains(token.partOfSpeechLevel1)) return@forEach
                    if (token.surface.length <= 2) return@forEach
                    words[token.surface] = if (words.containsKey(token.surface)) {
                        words[token.surface]!!.toInt() + 1
                    } else {
                        1
                    }
                }
            }
            val sorted: MutableMap<String, Int> = words.toList().sortedByDescending{ it.second }.toMap().toMutableMap()
            for (i in 1..20) {
                if (sorted.isEmpty()) break
                val word = sorted.keys.first()
                val count = sorted[word]
                println("$i : $word ($count)")
                sorted.remove(word)
            }
            println("$messageCount messages (including stamps, images, and movies)")
            println("$stampCount stamps")
            println("$imageCount images")
            println("$movieCount movies")
        }
    }
}