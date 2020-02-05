package bitspittle.autostretch.data.routines

import bitspittle.autostretch.data.stretches.Legs
import bitspittle.autostretch.model.routine.MutableRoutine

// From https://www.youtube.com/watch?v=aFCEuADByyo
fun createMuscleWatching28MinuteRoutine() = MutableRoutine().apply {
    stretches.addAll(listOf(
        Legs.BOTH_LEGS_OPENER,
        Legs.SINGLE_LEG_OPENER))
}