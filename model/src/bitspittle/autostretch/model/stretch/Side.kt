package bitspittle.autostretch.model.stretch

enum class Side {
    LEFT,
    RIGHT;

    fun switch(): Side {
        return when (this) {
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}