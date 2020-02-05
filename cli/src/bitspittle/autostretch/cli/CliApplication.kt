package bitspittle.autostretch.cli

import bitspittle.autostretch.data.routines.createMuscleWatching28MinuteRoutine
import bitspittle.autostretch.model.routine.Routine
import bitspittle.autostretch.model.routine.RoutineEvents
import bitspittle.autostretch.model.routine.RoutineRunner
import bitspittle.autostretch.model.stretch.MutableDuration
import bitspittle.autostretch.model.stretch.Side
import bitspittle.autostretch.model.stretch.Stretch
import bitspittle.autostretch.model.user.MutableUserSettings

fun main() {
    var finished = false
    val settings = MutableUserSettings()
    val routineEvents = object : RoutineEvents {
        override fun starting(routine: Routine) {
            println("Let's stretch!!")
            println()
            println("The following routine will take ${routine.totalTime(settings).toReadableString()}")
        }

        private fun beep() {
            print("\u0007")
        }

        override fun countdown(secondsRemaining: Int) {
            if (secondsRemaining > 0) {
                print("$secondsRemaining... ")
                if (secondsRemaining <= 3) {
                    beep()
                }
            }
            if (secondsRemaining % 10 == 1) {
                println()
            }
        }

        override fun getReadyForNext(stretch: Stretch, side: Side?) {
            println()
            print("GET READY! Next stretch: ")
            println(if (side != null) stretch.nameWithSide(side) else stretch.name)
            println()
            println(if (side != null) stretch.descWithSide(side) else stretch.desc)
            println()
        }

        override fun startStretching() {
            println()
            println("Start stretching!")
            println()
        }

        override fun switchSides(stretch: Stretch, side: Side) {
            println()
            print("Switch sides: ")
            println(stretch.nameWithSide(side))
            println()
            println(stretch.descWithSide(side))
            println()
        }

        override fun finished() {
            println()
            println("Congratulations! You're done.")
            finished = true
        }
    }

    val routine = createMuscleWatching28MinuteRoutine()
    val runner = RoutineRunner(settings, routine, routineEvents)

    val elapsed = MutableDuration()
    var lastFrameStart = System.nanoTime()
    while (!finished) {
        val now = System.nanoTime()
        elapsed.setNanos(now - lastFrameStart)
        lastFrameStart = now
        // Set max value in case of debugging
        if (elapsed.toSeconds() > 1) {
            elapsed.setSeconds(1)
        }
        runner.elapse(elapsed)
        Thread.sleep(100)
    }
}