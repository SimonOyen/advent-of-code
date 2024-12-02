package io.github.simonoyen.tools

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import java.io.File

class InputDownloader(private val client: HttpClient = HttpClient()) {
    fun get(year: Int, day: Int): String {
        val file = File("input/$year/$day.txt")
        return when {
            file.exists() -> file.readText().replace("\r\n", "\n")
            else -> download(year, day).removeSuffix("\n")
                .also {
                    file.write(it)
                    println("Input saved to cache.")
                }
        }
    }

    private fun File.write(data: String) {
        parentFile.mkdirs()
        writeText(data)
    }

    private fun download(year: Int, day: Int): String {
        val input = runBlocking {
            client.get(getUrl(year, day)) {
                cookie(name = "session", value = System.getenv("AOC_SESSION_COOKIE"))
            }.bodyAsText()
        }

        return input
    }

    private fun getUrl(year: Int, day: Int): String = "https://adventofcode.com/$year/day/$day/input"
}