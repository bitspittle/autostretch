package bitspittle.autostretch.model.stretch

data class Stretch(
    val name: String,
    val desc: String,
    val type: Type = Type.NORMAL
) {


    fun nameWithSide(side: Side): String {
        return name.withSide(side)
    }
    fun descWithSide(side: Side): String {
        return desc.withSide(side)
    }
    private fun String.withSide(side: Side): String {
        return this
            .replace("(Side)", side.capitlized())
            .replace("(!Side)", side.switch().capitlized())
            .replace("(side)", side.toLowerCase())
            .replace("(!side)", side.switch().toLowerCase())
    }

    private fun Side.toLowerCase(): String {
        return name.toLowerCase()
    }
    private fun Side.capitlized(): String {
        return toLowerCase().capitalize()
    }


    enum class Type {
        /**
         * This is just a regular, single stretch.
         */
        NORMAL,

        /**
         * This stretch is done twice, once per side.
         */
        PER_SIDE,
    }
}