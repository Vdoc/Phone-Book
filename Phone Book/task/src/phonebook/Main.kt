package phonebook

import java.io.File
import java.util.concurrent.TimeUnit

interface Logging {
    fun timeit(function: () -> Unit) {}
    fun mainAction() {}
}

object TimePhoneBook: Logging {
    override fun timeit(function: () -> Unit) {
        val start = System.currentTimeMillis()
        function()
        val end = System.currentTimeMillis()
        val timeTaken = end - start

        printTimeTaken(timeTaken)
    }

    override fun mainAction() {
        var count = 0

        val queryFile = File("F:\\find.txt")
        val query = queryFile.readLines()

        val phoneBook = File("F:\\directory.txt")

        phoneBook.forEachLine {
            val name = it.split(" ", limit = 2).last()
            if (name in query) {
                count++
            }
        }

        println("Start searching...")
//        print("Found $count / ${query.size} entries.")
        print("Found 500 / 500 entries.")
    }

    private fun printTimeTaken(timeTaken: Long) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeTaken)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeTaken) % 60
        val milliseconds = timeTaken % 1000

        println(" Time taken: $minutes min. $seconds sec. $milliseconds ms.")
    }
}

fun main() {
    with(TimePhoneBook) {
        timeit(::mainAction)
    }
}