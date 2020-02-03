package bitspittle.autostretch.model.runner

import bitspittle.autostretch.model.stretch.Duration
import bitspittle.autostretch.model.stretch.MutableDuration
import bitspittle.autostretch.model.stretch.Side
import bitspittle.autostretch.model.stretch.Stretch

interface Routine {
    val stretches: List<Stretch>
    val timePerStretch: Duration
    val timeBetweenStretches: Duration
    val timeToSwitchSides: Duration
    val preferredInitialSide: Side

    fun timeFor(stretch: Stretch): MutableDuration {
        val duration = MutableDuration()
        duration += timeBetweenStretches
        duration += timePerStretch
        if (stretch.type == Stretch.Type.PER_SIDE) {
            duration += timeToSwitchSides
        }
        return duration
    }

    fun totalTime(): Duration {
        val totalTime = MutableDuration()
        stretches.forEach { stretch -> totalTime += timeFor(stretch) }
        return totalTime
    }
}

/**
 * A single program is a list of stretches
 */
class MutableRoutine : Routine {
    override val stretches: MutableList<Stretch> = mutableListOf()
    override val timePerStretch = MutableDuration.ofMinutes(1)
    override val timeBetweenStretches = MutableDuration.ofSeconds(7)
    override val timeToSwitchSides = MutableDuration.ofSeconds(3)
    override val preferredInitialSide = Side.LEFT

}