import io.github.simonoyen.tools.Day
import io.github.simonoyen.tools.InputDownloader
import io.github.simonoyen.tools.InputType
import io.kotest.datatest.WithDataTestName
import io.kotest.matchers.shouldBe

data class TestData(
    val year: Int,
    val day: Int,
    val expectedA: Number,
    val expectedB: Number,
    val implementation: Day
) : WithDataTestName {
    private val testInputA: String = InputType.PART_A.read(year, day)
    private val testInputB: String = InputType.PART_B.read(year, day)
    private val realInput: String = InputDownloader().get(year, day)

    fun testPartA() {
        implementation.partA(testInputA) shouldBe expectedA
        println("$day.12.$year: Starting real input")
        println("$day.12.$year: A: ${implementation.partA(realInput)}")
    }

    fun testPartB() {
        implementation.partB(testInputB) shouldBe expectedB
        println("$day.12.$year: Starting real input")
        println("$day.12.$year: B: ${implementation.partB(realInput)}")
    }

    override fun dataTestName(): String = "$day.12.$year"
}
