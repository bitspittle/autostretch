package bitspittle.autostretch.model.util

class StateMachine<S : Enum<S>>(initialState: S) {
    private data class Transition<S>(val stateFrom: S, val stateTo: S)

    var currState = initialState
        private set

    private val onStateExited = mutableMapOf<S, () -> Unit>()
    private val onStateEntered = mutableMapOf<S, () -> Unit>()
    private val onStateChanging = mutableMapOf<Transition<S>, () -> Unit>()

    fun allowTransition(stateFrom: S, stateTo: S, callback: () -> Unit = {}) {
        val t = Transition(stateFrom, stateTo)
        if (onStateChanging.contains(t)) {
            throw IllegalArgumentException("Duplicate transition registration: $stateFrom -> $stateTo")
        }

        onStateChanging[t] = callback
    }

    fun onStateEntered(state: S, callback: () -> Unit) {
        if (onStateEntered.contains(state)) {
            throw IllegalArgumentException("Second callback registered for onStateEntered($state)")
        }
        onStateEntered[state] = callback
    }

    fun onStateExited(state: S, callback: () -> Unit) {
        if (onStateExited.contains(state)) {
            throw IllegalArgumentException("Second callback registered for onStateExited($state)")
        }
        onStateExited[state] = callback
    }

    fun enterState(stateTo: S) {
        val t = Transition(currState, stateTo)
        if (!onStateChanging.contains(t)) {
            throw IllegalArgumentException("State change not allowed: $currState -> $stateTo")
        }
        onStateExited[currState]?.invoke()
        onStateChanging[t]!!.invoke()

        currState = stateTo
        onStateEntered[currState]?.invoke()
    }
}