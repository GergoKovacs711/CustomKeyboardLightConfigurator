package hu.gergokovacs.blender

import hu.gergokovacs.blender.model.RGB

enum class BlendPoint(val value: Int) {
    TWO(2), FOUR(4)
}

fun main(args: Array<String>) {
    while (true) {
        println("Add the number of points (2, 4)")

        var blendPointOption: BlendPoint? = null
        while (blendPointOption == null) {
            try {
                val enteredString = readLine()
                val parsedNumber = Integer.valueOf(enteredString)
                if (parsedNumber != 2 && parsedNumber != 4)
                    throw IllegalArgumentException()

                when (parsedNumber) {
                    2 -> {
                        blendPointOption = BlendPoint.TWO
                    }
                    4 -> {
                        blendPointOption = BlendPoint.FOUR
                    }
                }

            } catch (ex: Exception) {
                println("Please enter an exceptable value!")
            }
        }

        var i = 0
        val controlRGBPoints = mutableListOf<RGB>()
        while (i < blendPointOption.value) {
            var rgbColorString = readLine()

            while (rgbColorString == null) {
                println("Please enter something!")
                rgbColorString = readLine()
            }

            try {
                val rgbColor = rgbColorParser(rgbColorString)
                controlRGBPoints.add(rgbColor)
                println("${i + 1}th color is OK. The value is $rgbColor")
                i++
            } catch (ex: Exception) {
                println("Could no parse the ${i + 1}th color! Enter it again!")
            }
        }


        var numberOfRows = 0
        var numberOfColumns = 0
        var divisionsValid = false
        while (!divisionsValid){
            try {
                when(blendPointOption){
                    BlendPoint.TWO -> {
                        println("Please enter the number of divisions")
                        val numberOfRowsString = readLine()
                        numberOfRows = Integer.valueOf(numberOfRowsString)
                        divisionsValid = true
                    }
                    BlendPoint.FOUR -> {
                        println("Please enter the number of rows")
                        val numberOfRowsString = readLine()
                        numberOfRows = Integer.valueOf(numberOfRowsString)

                        println("Please enter the number of columns")
                        val numberOfColumnsString = readLine()
                        numberOfColumns = Integer.valueOf(numberOfColumnsString)
                        divisionsValid = true
                    }
                }
            } catch (ex: Exception){
                println("Please enter ${if(blendPointOption == BlendPoint.TWO) "a valid division!" else "valid values!"}")
            }
        }

        val blendedColors = rgbBlender(controlRGBPoints, blendPointOption, numberOfRows, numberOfColumns)
        printRGBMatrix(blendedColors, numberOfColumns)
    }
}

fun rgbColorParser(input: String): RGB {
    val rgbStringList = input.split(",").map { it -> it.trim() }
    if (rgbStringList.count() != 3)
        throw IllegalArgumentException()

    val rgbIntList = rgbStringList.map { it -> it.toInt() }

    return RGB(rgbIntList[0], rgbIntList[1], rgbIntList[2])
}

fun rgbBlender(rgbPoints: List<RGB>, blendOption: BlendPoint, rows: Int, columns: Int): List<RGB> {
    when (blendOption) {
        BlendPoint.TWO -> {
            if (rgbPoints.count() != 2)
                throw IllegalArgumentException()

            return blendRow(rgbPoints[0], rgbPoints[1], columns)
        }
        BlendPoint.FOUR -> {
            if (rgbPoints.count() != 4)
                throw IllegalArgumentException()

            val colorMatrix = mutableListOf<RGB>()

            for (i in 0..(rows - 1)) {
                val scale: Double = i.toDouble() / (rows - 1)
                val firstPoint = blendPoint(rgbPoints[0], rgbPoints[2], scale)
                val secondPoint = blendPoint(rgbPoints[1], rgbPoints[3], scale)
                val colorRow = blendRow(firstPoint, secondPoint, columns)
                colorRow.forEach { colorMatrix.add(it) }
            }

            return colorMatrix
        }
    }
}

fun blendRow(firstPoint: RGB, secondPoint: RGB, divisions: Int): List<RGB> {
    val colors = mutableListOf<RGB>()

    for (i in 0..(divisions - 1)) {
        val scale: Double = i.toDouble() / (divisions - 1)

        val tempColor = blendPoint(firstPoint, secondPoint, scale)

        colors.add(tempColor)
    }

    return colors
}

fun blendPoint(firstPoint: RGB, secondPoint: RGB, scale: Double): RGB {
    if (scale < 0 && scale > 1)
        throw IllegalArgumentException()
    val red = firstPoint.red * (1 - scale) + secondPoint.red * (scale)
    val green = firstPoint.green * (1 - scale) + secondPoint.green * (scale)
    val blue = firstPoint.blue * (1 - scale) + secondPoint.blue * (scale)

    val resultColor = RGB(red.toInt(), green.toInt(), blue.toInt())
    return resultColor
}

fun Any.print() = println(this)

fun printRGBMatrix(matrix: List<RGB>, rowLenght: Int) {
    if (matrix.count().rem(rowLenght) != 0)
        throw IllegalArgumentException("matrix must be divisible by rowLenght!")

    val numberOfRows = matrix.count() / rowLenght

    for (i in 0..(numberOfRows - 1)) {
        val divider : StringBuffer = StringBuffer("")
        val rowOfIndecies : StringBuffer = StringBuffer("")
        val rowOfColors: StringBuffer = StringBuffer("")
        for (j in 0..(rowLenght - 1)) {
            val index = i * rowLenght + j
            divider.append("_________________")
            rowOfIndecies.append("[     ${intToStringAlwaysThreeDigit(index)}       ]")
            rowOfColors.append(matrix[index].formatString())
        }
        println(divider)
        println(rowOfIndecies)
        println(rowOfColors)
        println()
    }
}

fun RGB.formatString(): String {
    return "[ ${intToStringAlwaysThreeDigit(this.red)}, " +
            "${intToStringAlwaysThreeDigit(this.green)}, ${intToStringAlwaysThreeDigit(this.blue)} ]"
}

fun intToStringAlwaysThreeDigit(value: Int): String {
    if ((value / 100) != 0)
        return value.toString()

    if ((value / 10) != 0)
        return "0$value"

    return "00$value"
}


