package galacticCombat.level.json

import kotlinx.serialization.Serializable

@Serializable
data class Wave(
    val invaders: List<Pair<Double, InvaderArgs>>,
    val isBoss: Boolean = false
) : List<Pair<Double, InvaderArgs>> by invaders