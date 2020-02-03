package bitspittle.autostretch.model.runner

import bitspittle.autostretch.model.stretch.Side
import bitspittle.autostretch.model.stretch.Stretch

interface RoutineEvents {
    fun starting(routine: Routine)
    fun countdown(secondsRemaining: Int)
    fun getReadyForNext(stretch: Stretch, side: Side?)
    fun startStretching()
    fun switchSides(stretch: Stretch, side: Side)
    fun finished()
}