import io.github.simonoyen.tools.Day
import io.github.simonoyen.tools.InputDownloader
import io.github.simonoyen.tools.InputType
import io.kotest.datatest.WithDataTestName
import io.kotest.matchers.shouldBe

data class TestData(val year: Int, val day: Int, val expectedA: Number, val expectedB: Number, val implementation: () -> Day) : WithDataTestName {
    private val testInputA: String = InputType.PART_A.read(year, day)
    private val testInputB: String = InputType.PART_B.read(year, day)
    private val realInput: String = InputDownloader().get(year, day)
    private val solution: Day = implementation()

    fun testPartA() {
        solution.partA(testInputA) shouldBe expectedA
        println("$day.12.$year: Starting real input")
        println("$day.12.$year: A: ${solution.partA(realInput)}")
    }

    fun testPartB() {
        solution.partB(testInputB) shouldBe expectedB
        println("$day.12.$year: Starting real input")
        println("$day.12.$year: B: ${solution.partB(realInput)}")
    }

    override fun dataTestName(): String = "$day.12.$year"
}
