package bitspittle.autostretch.model.user

import bitspittle.autostretch.model.stretch.Duration
import bitspittle.autostretch.model.stretch.MutableDuration
import bitspittle.autostretch.model.stretch.Side

interface UserSettings {
    val timePerStretch: Duration
    val timeBetweenStretches: Duration
    val timeToSwitchSides: Duration
    val preferredInitialSide: Side
}

class MutableUserSettings : UserSettings {
    override val timePerStretch = MutableDuration.ofMinutes(1)
    override val timeBetweenStretches = MutableDuration.ofSeconds(7)
    override val timeToSwitchSides = MutableDuration.ofSeconds(3)
    override val preferredInitialSide = Side.LEFT
}