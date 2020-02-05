package bitspittle.autostretch.model.routine

import bitspittle.autostretch.model.stretch.Duration
import bitspittle.autostretch.model.stretch.MutableDuration
import bitspittle.autostretch.model.stretch.Side
import bitspittle.autostretch.model.stretch.Stretch
import bitspittle.autostretch.model.user.UserSettings
import bitspittle.autostretch.model.util.StateMachine

class RoutineRunner(
    private val settings: UserSettings,
    private val routine: Routine,
    private val routineEvents: RoutineEvents
) {
    private enum class State {
        INITIAL,
        IN_BETWEEN_STRETCHES,
        SWITCHING_SIDES,
        STRETCHING,
        FINISHED
    }

    private val stateMachine = StateMachine(State.INITIAL)

    private var currStretchIndex = 0
    private var currStretchSide: Side? = null // Only used for PER_SIDE stretches
    private val timeUntilStretch = MutableDuration()
    private val timeUntilStretchFinished = MutableDuration()

    private val currStretch
        get() = routine.stretches[currStretchIndex]

    init {
        if (routine.stretches.isEmpty()) {
            throw IllegalArgumentException("Can't run invalid program that has no stretches in it")
        }

        stateMachine.allowTransition(State.INITIAL, State.IN_BETWEEN_STRETCHES)
        stateMachine.allowTransition(State.IN_BETWEEN_STRETCHES, State.STRETCHING)
        stateMachine.allowTransition(State.SWITCHING_SIDES, State.STRETCHING)
        stateMachine.allowTransition(State.STRETCHING, State.SWITCHING_SIDES)
        stateMachine.allowTransition(State.STRETCHING, State.IN_BETWEEN_STRETCHES)
        stateMachine.allowTransition(State.STRETCHING, State.FINISHED)

        stateMachine.onStateEntered(State.IN_BETWEEN_STRETCHES) {
            timeUntilStretch.set(settings.timeBetweenStretches)
            currStretchSide = when (currStretch.type) {
                Stretch.Type.PER_SIDE -> settings.preferredInitialSide
                else -> null
            }

            routineEvents.getReadyForNext(currStretch, currStretchSide)
        }
        stateMachine.onStateEntered(State.SWITCHING_SIDES) {
            timeUntilStretch.set(settings.timeToSwitchSides)
            currStretchSide = currStretchSide!!.switch()

            routineEvents.switchSides(currStretch, currStretchSide!!)
        }
        stateMachine.onStateEntered(State.STRETCHING) {
            timeUntilStretchFinished.set(settings.timePerStretch)
            routineEvents.startStretching()
        }
        stateMachine.onStateEntered(State.FINISHED) {
            routineEvents.finished()
        }
    }

    fun elapse(duration: Duration) {
        when (stateMachine.currState) {
            State.INITIAL -> {
                routineEvents.starting(routine)
                stateMachine.enterState(State.IN_BETWEEN_STRETCHES)
            }
            State.IN_BETWEEN_STRETCHES -> {
                reduceTimeUntilStretch(duration)
                if (timeUntilStretch <= Duration.ZERO) {
                    stateMachine.enterState(State.STRETCHING)
                }
            }
            State.SWITCHING_SIDES -> {
                reduceTimeUntilStretch(duration)
                if (timeUntilStretch <= Duration.ZERO) {
                    stateMachine.enterState(State.STRETCHING)
                }
            }
            State.STRETCHING -> {
                reduceTimeUntilStretchFinished(duration)
                if (timeUntilStretchFinished <= Duration.ZERO) {
                    if (currStretch.type == Stretch.Type.PER_SIDE && currStretchSide == settings.preferredInitialSide) {
                        stateMachine.enterState(State.SWITCHING_SIDES)
                    }
                    else {
                        ++currStretchIndex
                        if (currStretchIndex < routine.stretches.size) {
                            stateMachine.enterState(State.IN_BETWEEN_STRETCHES)
                        }
                        else {
                            stateMachine.enterState(State.FINISHED)
                        }
                    }
                }
            }
            State.FINISHED -> {}
        }
    }

    private fun reduceTimeUntilStretch(duration: Duration) {
        reduceDurationAndFireIfSecondsChanged(timeUntilStretch, duration) { secs ->
            routineEvents.countdown(secs)
        }
    }

    private fun reduceTimeUntilStretchFinished(duration: Duration) {
        reduceDurationAndFireIfSecondsChanged(timeUntilStretchFinished, duration) { secs ->
            routineEvents.countdown(secs)
        }
    }

    private fun reduceDurationAndFireIfSecondsChanged(
        durationToReduce: MutableDuration,
        durationBy: Duration,
        notifySecondsLeft: (Int) -> Unit
    ) {
        val before = durationToReduce.toSeconds()
        durationToReduce -= durationBy
        val after = durationToReduce.toSeconds()
        if (before > after || durationToReduce <= Duration.ZERO) {
            notifySecondsLeft(before.toInt())
        }
    }
}