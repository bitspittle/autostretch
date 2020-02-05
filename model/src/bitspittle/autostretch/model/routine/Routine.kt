package bitspittle.autostretch.model.routine

import bitspittle.autostretch.model.stretch.Duration
import bitspittle.autostretch.model.stretch.MutableDuration
import bitspittle.autostretch.model.stretch.Stretch
import bitspittle.autostretch.model.user.UserSettings

interface Routine {
    val stretches: List<Stretch>

    fun timeFor(stretch: Stretch, settings: UserSettings): MutableDuration {
        val duration = MutableDuration()
        duration += settings.timeBetweenStretches
        duration += settings.timePerStretch
        if (stretch.type == Stretch.Type.PER_SIDE) {
            duration += settings.timeToSwitchSides
        }
        return duration
    }

    fun totalTime(settings: UserSettings): Duration {
        val totalTime = MutableDuration()
        stretches.forEach { stretch -> totalTime += timeFor(stretch, settings) }
        return totalTime
    }
}

/**
 * A single program is a list of stretches
 */
class MutableRoutine : Routine {
    override val stretches: MutableList<Stretch> = mutableListOf()
}