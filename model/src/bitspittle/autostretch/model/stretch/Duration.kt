package bitspittle.autostretch.model.stretch

interface
Duration : Comparable<Duration> {
    companion object {
        val ZERO = ofNanos(0)
        fun ofNanos(nanos: Long) = MutableDuration.ofNanos(nanos) as Duration
        fun ofMicros(micros: Long) = MutableDuration.ofMicros(micros) as Duration
        fun ofMillis(millis: Long) = MutableDuration.ofMillis(millis) as Duration
        fun ofSeconds(seconds: Long) = MutableDuration.ofSeconds(seconds) as Duration
        fun ofMinutes(minutes: Long) = MutableDuration.ofMinutes(minutes) as Duration
        fun ofHours(hours: Long) = MutableDuration.ofHours(hours) as Duration
    }

    fun toNanos(): Long
    fun toMicros(): Long
    fun toMillis(): Long
    fun toSeconds(): Long
    fun toMinutes(): Long
    fun toHours(): Long

    override fun compareTo(other: Duration) = toNanos().compareTo(other.toNanos())

    fun toReadableString(): String {
        val sb = StringBuilder()
        toHours().let { h -> if (h > 0) sb.append("${h}h ") }
        (toMinutes() % 60).let { m -> if (m > 0) sb.append("${m}m ") }
        (toSeconds() % 60).let { s -> sb.append("${s}s")}
        return sb.toString()
    }
}

class MutableDuration private constructor(private var nanos: Long): Duration {
    constructor(): this(0)

    companion object {
        fun of(duration: Duration) = MutableDuration(duration.toNanos())
        fun ofNanos(nanos: Long) = MutableDuration(nanos)
        fun ofMicros(micros: Long) = MutableDuration().apply { setMicros(micros) }
        fun ofMillis(millis: Long) = MutableDuration().apply { setMillis(millis) }
        fun ofSeconds(seconds: Long) = MutableDuration().apply { setSeconds(seconds) }
        fun ofMinutes(minutes: Long) = MutableDuration().apply { setMinutes(minutes) }
        fun ofHours(hours: Long) = MutableDuration().apply { setHours(hours) }
    }

    override fun toNanos() = nanos
    override fun toMicros() = toNanos() / 1000
    override fun toMillis() = toMicros() / 1000
    override fun toSeconds() = toMillis() / 1000
    override fun toMinutes() = toSeconds() / 60
    override fun toHours() = toMinutes() / 60

    fun set(rhs: Duration) { this.nanos = rhs.toNanos() }
    fun setNanos(nanos: Long) { this.nanos = nanos }
    fun setMicros(micros: Long) { setNanos(micros * 1000) }
    fun setMillis(millis: Long) { setMicros(millis * 1000) }
    fun setSeconds(seconds: Long) { setMillis(seconds * 1000) }
    fun setMinutes(minutes: Long) { setSeconds(minutes * 60) }
    fun setHours(hours: Long) { setMinutes(hours * 60) }

    operator fun plusAssign(rhs: Duration) {
        nanos += rhs.toNanos()
    }

    operator fun minusAssign(rhs: Duration) {
        nanos -= rhs.toNanos()
    }
}