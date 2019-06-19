package hu.gergokovacs.blender.model

class RGB {
    companion object {
        val RGB_VALUE_MAX = 255
        val RGB_VALUE_MIN = 0
    }

    var red: Int
        set(value) {
            field = valueChecker(value)
        }

    var green: Int
        set(value) {
            field = valueChecker(value)
        }
    var blue: Int
        set(value) {
            field = valueChecker(value)
        }

    constructor(red: Int, green: Int, blue: Int) {
        this.red = valueChecker(red)
        this.green = valueChecker(green)
        this.blue = valueChecker(blue)
    }

    private fun valueChecker(value: Int): Int {
        if (value in RGB_VALUE_MIN..RGB_VALUE_MAX) return value
        return if (value > RGB_VALUE_MAX) RGB_VALUE_MAX else RGB_VALUE_MIN
    }

    override fun toString(): String {
        return "(R:$red, G:$green, B:$blue)"
    }


}
