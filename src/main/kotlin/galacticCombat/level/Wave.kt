package galacticCombat.level

import galacticCombat.entities.invader.InvaderType
import kotlinx.serialization.Serializable

@Serializable
data class Wave(
    val invaders: List<Pair<Double, InvaderArgs>>,
    val isBoss: Boolean = false
) : List<Pair<Double, InvaderArgs>> by invaders

@Serializable
data class InvaderArgs(
    //todo add which path to spawn on
    val type: InvaderType,
    val level: Int
)