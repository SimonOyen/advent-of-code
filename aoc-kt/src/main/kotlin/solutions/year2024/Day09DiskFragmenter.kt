package io.github.simonoyen.solutions.year2024

import io.github.simonoyen.tools.Day

object Day09DiskFragmenter : Day {
    // 93.451.884.968 too low
    // 6.299.243.228.569
    override fun partA(input: String): Number {
        val disk = input.toCharArray().map { it.digitToInt() }
            .mapIndexed { index, digit ->
                if (index % 2 == 0) {
                    List<Int>(digit) { index / 2 }
                } else {
                    List<Int?>(digit) { null }
                }
            }.flatten()

        val reordered = reorderLastBlock(disk)
        val checksum = reordered.mapIndexed { index, digit -> (index.toLong()) * digit!! }

        return checksum.sum()
    }

    tailrec fun reorderLastBlock(disk: List<Int?>): List<Int?> =
        if (!disk.contains(null)) disk else {
            val lastValue = disk.last()!!
            val positionOfFirstNull = disk.indexOf(null)
            val listBeforeNull = disk.take(positionOfFirstNull)
            val listAfterNull = disk.drop(positionOfFirstNull + 1).dropLast(1).dropLastWhile { it == null }

            reorderLastBlock(listBeforeNull + listOf(lastValue) + listAfterNull)
        }


    override fun partB(input: String): Number {
        val disk = input.toCharArray().map { it.digitToInt() }
            .mapIndexed { index, digit ->
                if (index % 2 == 0) {
                    File(digit, index / 2)
                } else {
                    Empty(digit)
                }
            }

        val reorderedDisk = reorder(disk).filter { if (it is Empty) it.size > 0 else true }
        
        val listAsBlocks = reorderedDisk.mapIndexed { index, space -> List<Int>(space.size) { if (space is CheckedFile) space.id else 0 } }.flatten()
        val checksum = listAsBlocks.mapIndexed { index, digit -> (index.toLong()) * digit }.sum()
        return checksum
    }

    open class Space(val size: Int)

    class File(size: Int, val id: Int) : Space(size) {
        override fun toString(): String = "File(size=$size, id=$id)"
    }

    class Empty(size: Int) : Space(size) {
        override fun toString(): String = "Empty(size=$size)"
    }

    class CheckedFile(size: Int, val id: Int) : Space(size) {
        override fun toString(): String = "Checked(size=$size, id=$id)"
    }

    fun reorder(disk: List<Space>) = disk.foldRight(disk) { currentSpace, resultingDisk ->
        return@foldRight when (currentSpace) {
            is Empty -> resultingDisk
            is CheckedFile -> resultingDisk
            is File -> {
                val listWithoutCurrentFile = resultingDisk.map { if (it == currentSpace) Empty(currentSpace.size) else it }
                val fittingSpaces =
                    listWithoutCurrentFile.mapIndexedNotNull { index, search -> if (search is Empty && search.size >= currentSpace.size) index to search else null }

                if (fittingSpaces.isEmpty()) {
                    return@foldRight resultingDisk.map { if (it == currentSpace) CheckedFile(currentSpace.size, currentSpace.id) else it }
                }

                val (indexOfFittingSpace, fittingSpace) = fittingSpaces.first()

                val filler = Empty(fittingSpace.size - currentSpace.size)

                val diskBeforeEmptySpace = listWithoutCurrentFile.take(indexOfFittingSpace)
                val diskAfterEmptySpace = listWithoutCurrentFile.drop(indexOfFittingSpace + 1)


                diskBeforeEmptySpace + listOf(CheckedFile(currentSpace.size, currentSpace.id), filler) + diskAfterEmptySpace
            }

            else -> resultingDisk
        }

        resultingDisk
    }
}