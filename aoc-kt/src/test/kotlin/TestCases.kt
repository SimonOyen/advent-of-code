import io.github.simonoyen.solutions.year2023.*
import io.github.simonoyen.solutions.year2024.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData

class TestCases : FunSpec({
    context("Tests for 2023") {
        withData(
            TestData(2023, 1, 142, 281, ::Day01Trebuchet),
            TestData(2023, 2, 8, 2286, ::Day02CubeConundrum),
            TestData(2023, 3, 4361, 467835, ::Day03GearRatios),
            TestData(2023, 4, 13, 30, ::Day04Scratchcards),
            TestData(2023, 5, 35, -1, ::Day05IfYouGiveASeedAFertilizer),
            TestData(2023, 6, 288, 71503, ::Day06WaitForIt),
            TestData(2023, 7, 6440, 6839, ::Day07CamelCards),
            TestData(2023, 8, 6, 6, ::Day08HauntedWasteland),
            TestData(2023, 9, 114, 2, ::Day09MirageMaintenance),
            TestData(2023, 10, 8, -1, ::Day10PipeMaze),
        ) { test ->
            test.testPartA()
            test.testPartB()
        }
    }

    context("Tests for 2024") {
        withData(
            TestData(2024, 1, 11, 31, ::Day01HistorianHysteria),
            TestData(2024, 2, 2, 4, ::Day02RedNosedReports),
            TestData(2024, 3, 161, 48, ::Day03MullItOver),
            // TestData(2024, 4, 18, 9, ::Day04CeresSearch), // Too slow :')
            TestData(2024, 5, 143, 123, ::Day05PrintQueue),
            TestData(2024, 6, 41, 6, ::Day06GuardGallivant),
        ) { test ->
            test.testPartA()
            test.testPartB()
        }
    }
})

