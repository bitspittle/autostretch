package bitspittle.autostretch.data.stretches

import bitspittle.autostretch.model.stretch.Stretch

object Legs {
    val BOTH_LEGS_OPENER = Stretch(
        "Light Leg Opener",
        "Sit straight, open your legs as wide as is comfortable, and lean side to side."
    )

    val SINGLE_LEG_OPENER = Stretch(
        "Light (Side) Leg Opener",
        "Bend your (!side) leg, foot against groin, and stretch out your (side) leg",
        Stretch.Type.PER_SIDE
    )
}