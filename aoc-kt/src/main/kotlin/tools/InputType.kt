package io.github.simonoyen.tools

import tools.TestDataReader

enum class InputType(val fileName: String) {
    PART_A("a_test"),
    PART_B("b_test");

    fun read(year: Int, day: Int) = TestDataReader.readInput(year, day, fileName)
}
