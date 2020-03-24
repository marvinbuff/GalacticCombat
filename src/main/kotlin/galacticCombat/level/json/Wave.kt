package galacticCombat.level.json

import kotlinx.serialization.Serializable

@Serializable
data class Wave(
    val invaders: List<InvaderSpawnArgs>,
    val isBoss: Boolean = false
) : List<InvaderSpawnArgs> by invaders


@Serializable
data class InvaderSpawnArgs(
    var time: Double,
    var args: InvaderArgs
)