package tools

import java.io.File

object TestDataReader {
    fun readInput(year: Int, day: Int, fileName: String) = readFile("$year/$day/$fileName")

    private fun readFile(fileName: String): String {
        return try {
            File(ClassLoader.getSystemResource(fileName).file).readText()
        } catch (npe: NullPointerException) {
            ""
        }
    }
}

