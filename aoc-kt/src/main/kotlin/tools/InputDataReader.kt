package tools

import io.github.simonoyen.tools.InputType
import java.io.File

object InputDataReader {
    fun readInput(year: Int, day: Int, fileName: String) = readFile("$year/$day/$fileName")
    fun readInput(year: Int, day: Int, type: InputType) = readFile("$year/$day/${type.fileName}")

    private fun readFile(fileName: String): String {
        return try {
            //  {}.javaClass.getResource(fileName)!!.readText()
            File(ClassLoader.getSystemResource(fileName).file).readText()
        } catch (npe: NullPointerException) {
            ""
        }
    }
}

