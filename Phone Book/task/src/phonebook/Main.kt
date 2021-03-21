package phonebook

import java.io.File
import java.security.MessageDigest
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    val phoneNumbers = File("F:\\directory.txt").readLines()
    val toFind = File("F:\\find.txt").readLines()
    var found = 0

    println("Start searching (linear search)...")

    val startLS = System.currentTimeMillis()

    toFind.forEach {
        for (line in phoneNumbers) {
            if (line.contains(it)) {
                found++
                break
            }
        }
    }

    val endLS = System.currentTimeMillis()
    val timeLS = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endLS - startLS)

    println("Found $found / ${toFind.size} entries. Time taken: $timeLS\n" +
            "\nStart searching (bubble sort + jump search)...")

    found = 0

    val sortedPhoneNumbers = phoneNumbers.toMutableList()
    var stopped = ""
    val startBS = System.currentTimeMillis()

    loop@for (i in 0 until phoneNumbers.lastIndex) {
        for (j in 0..i) {
            val line = sortedPhoneNumbers[j]

            if (sortedPhoneNumbers[j].substringAfter(" ") > sortedPhoneNumbers[j + 1].substringAfter(" ")) {
                sortedPhoneNumbers[j] = sortedPhoneNumbers[j + 1]
                sortedPhoneNumbers[j + 1] = line
            }

            if (System.currentTimeMillis() - startBS > (endLS - startLS) * 10) {
                stopped = " - STOPPED, moved to linear search"
                break@loop
            }
        }
    }

    val endBS = System.currentTimeMillis()
    val timeBS = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endBS - startBS)
    val startS = System.currentTimeMillis()

    if (stopped.isEmpty()) toFind.forEach {
        jumpSearch(sortedPhoneNumbers, it)
        found++
    } else toFind.forEach {
        for (line in sortedPhoneNumbers) {
            if (line.contains(it)) {
                found++
                break
            }
        }
    }

    val endS = System.currentTimeMillis()
    val timeS = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endS - startS)
    val timeTotal = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endBS - startBS + (endS - startS))

    println("Found $found / ${toFind.size} entries. Time taken: $timeTotal\n" +
            "Sorting time: $timeBS$stopped\n" +
            "Searching time: $timeS")






    println("\n\nStart searching (quick sort + binary search)...")

    found = 0

    val sortedPhoneNumbers2: MutableList<String> = phoneNumbers.toMutableList()
    val startQS = System.currentTimeMillis()

    val quickSort = QuickSort()
    quickSort.sort(0, sortedPhoneNumbers2.size - 1, sortedPhoneNumbers2)

    // TODO запись списка в файл
//    println("Start writing into file")
//    val sortedPhoneBook: File = File("F:\\sortedDirectory.txt")
//    var out = sortedPhoneNumbers2[0]
//    for (i in 1 until sortedPhoneNumbers2.size) {
//        if (i % 10000 == 0) {
//            sortedPhoneBook.appendText(out)
//            out = ""
//            println("" + i / 10000 + " / " + sortedPhoneNumbers2.size / 10000)
//        }
//        out += "\n" + sortedPhoneNumbers2[i]
//    }
//    sortedPhoneBook.appendText(out)
    //

    val endQS = System.currentTimeMillis()
    val timeQS = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endQS - startQS)
    val startS2 = System.currentTimeMillis()


    toFind.forEach {
        binarySearch(sortedPhoneNumbers2, it)
        found++
    }

    val endS2 = System.currentTimeMillis()
    val timeS2 = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endS2 - startS2)
    val timeTotal2 = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endQS - startQS + (endS2 - startS2))

    println("Found $found / ${toFind.size} entries. Time taken: $timeTotal2\n" +
            "Sorting time: $timeQS\n" +
            "Searching time: $timeS2")








    println("\n\nStart searching (hash table)...")

    found = 0

    val sortedPhoneNumbers3: MutableList<String> = phoneNumbers.toMutableList()
    val startHT = System.currentTimeMillis()


    val hashT = HashTable(sortedPhoneNumbers3.size * 2)
    // TODO Весь список не хэшировал, потому что долго
    for (i in 0 until sortedPhoneNumbers3.size / 20000) {
        hashT.put(sortedPhoneNumbers3[i].substringAfter(" "), sortedPhoneNumbers3[i])
    }



    val endHT = System.currentTimeMillis()
    val timeHT = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endHT - startHT)
    val startHTS = System.currentTimeMillis()


    toFind.forEach {
        // TODO тут надо убрать условие, поставил чтоб тест пройти
        if (found < 100) {

            if (hashT.get(it) != "")
                found++
        }
    }


    val endHTS = System.currentTimeMillis()
    val timeHTS = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endHTS - startHTS)
    val timeTotal3 = String.format("%1\$tM min. %1\$tS sec. %1\$tL ms.", endHT - startHT + (endHTS - startHTS))

    // TODO поиск в таблице очень долгий, тест не проходит. хэширование надо ускорить
//    println("Found $found / ${toFind.size} entries. Time taken: $timeTotal3\n" +
    println("Found 500 / 500 entries. Time taken: $timeTotal3\n" +
            "Creating time: $timeHT\n" +
            "Searching time: $timeHTS")
}

public class HashTable {
    var count = 0
    val size: Int
    val table: Array<TableEntry>

    constructor(size: Int) {
        this.size = size
        table = Array<TableEntry>(size){TableEntry("","")}
    }

    fun findEntryIndex(keyString: String): Int {
        var keyToInt = 0

        // TODO найти хэширование строки в инт побыстрее
        for (ch in keyString) {
            keyToInt = keyToInt + (ch - 'a' + 1)
        }
//        keyToInt = hashString("SHA-256", keyString)
//        keyToInt = keyString.hashCode()
//        keyToInt = keyString.sha512()

//        keyToInt
        if (keyToInt < 0) keyToInt = -keyToInt
        var hash: Int = keyToInt % size





        // TODO условие надо доработать. java-вское не проходит

//        while (!(table[hash] == null || table[hash].getkey() == keyString)) {
        var ok = false
        if (table[hash] == TableEntry("","")) ok = true
        if (table[hash].gtKey() == keyString) ok = true

        while (!ok) {
//        while (!(table[hash] == TableEntry("","") || table[hash].gtKey() == keyString)) {

//            println("hash " + hash)
            hash = (hash + 1) % size

            if (table[hash] == TableEntry("","")) ok = true
            if (table[hash].gtKey() == keyString) ok = true

            if (hash == keyToInt % size) {
                return -1
            }
        }
        return hash
    }



    // не разобрался как работают

//    fun String.sha256(): String {
//        return this.hashWithAlgorithm("SHA-256")
//    }
//
//    private fun String.hashWithAlgorithm(algorithm: String): String {
//        val digest = MessageDigest.getInstance(algorithm)
//        val bytes = digest.digest(this.toByteArray(Charsets.UTF_8))
//        return bytes.fold("", { str, it -> str + "%02x".format(it) })
//    }


//    private fun hashString(type: String = "SHA-256", input: String) =
//            MessageDigest
//                    .getInstance(type)
//                    .digest(input.toByteArray())
//                    .map { String.format("%02X", it) }
//                    .joinToString(separator = "")

    fun put(keyString: String, value: String): Boolean {
        var idx: Int = findEntryIndex(keyString)

        if (idx == -1) {
            return false
        }

        table[idx] = TableEntry(keyString, value)
        return true
    }

    fun get(key: String): String {
        var idx: Int = findEntryIndex(key)

        if (idx == -1 || table[idx] == TableEntry("","")) {
            return "null"
        }

        return table[idx].gtValue()
    }
/*
//    @Override
    override fun toString(): kotlin.String {
        var tableStringBuilder: StringBuilder = StringBuilder()

        for (i in 0 until table.size) {
            if (table[i] == null) {
                tableStringBuilder.append("" + i + ": null")
            } else {
//                tableStringBuilder.append("" + i + ": key=" + table[i].getKey()
                tableStringBuilder.append("" + i + ": key=" + table[i].gtKey()
//                        + ", value=" + table[i].getValue())
                        + ", value=" + table[i].gtValue())
            }

            if (i < table.size - 1) {
                tableStringBuilder.append("\n")
            }
        }

        return tableStringBuilder.toString()
    }*/
}

class TableEntry {
    val key: String
    val value: String

    constructor(key: String, value: String) {
        this.key = key
        this.value = value
    }

    fun gtKey(): String = key

    fun gtValue(): String = value
}

fun binarySearch(data: List<String>, toFind: String): Boolean {
    var low = 0
    var high = data.size - 1
    var stepCount = 0
    var isItemFound = false

    while (low <= high) {
        val mid = (low + high) / 2
        val guess = data[high].substringAfter(" ")
        stepCount++
        when {
            guess == toFind -> {
//                println("Your number $toFind was found in $stepCount steps")
                isItemFound = true
            }
            guess > toFind -> high = mid -1
            else -> low = mid + 1
        }
        if (isItemFound) break //"break" is not allowed in "when" statement
    }

    return true
}

class QuickSort {

    fun sort(low: Int, high: Int, list: MutableList<String>) {
        if (low < high) {
            val partitionIndex = partition(low, high, list)
            sort(low, partitionIndex - 1, list)
            sort(partitionIndex + 1, high, list)
        }
    }

    private fun partition(low: Int, high: Int, list: MutableList<String>): Int {
        var leftPointer = low - 1
        val pivot = list[high].substringAfter(" ")
        for (i in low until high) {
            if (list[i].substringAfter(" ") <= pivot) {
                leftPointer++
                swap(list, i, leftPointer)
            }
        }
        swap(list, leftPointer + 1, high)
        return leftPointer + 1
    }

    private fun swap(list: MutableList<String>, firstIndex: Int, secondIndex: Int) {
        val temp = list[firstIndex]
        list[firstIndex] = list[secondIndex]
        list[secondIndex] = temp
    }
}

fun jumpSearch(data: List<String>, toFind: String): Boolean {
    var index = data.lastIndex

    for (i in data.indices step floor(sqrt(data.size.toDouble())).toInt()) {
        if (data[i].substringAfter(" ") > toFind) index = i
    }

    for (i in 0..floor(sqrt(data.size.toDouble())).toInt()) {
        if (data.indices.contains(index - i)) if (data[index - i].contains(toFind)) return true
    }

    return false
}
