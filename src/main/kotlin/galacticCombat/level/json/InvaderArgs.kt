package galacticCombat.level.json

import galacticCombat.entities.invader.InvaderType
import kotlinx.serialization.Serializable

@Serializable
data class InvaderArgs(
    val type: InvaderType,
    val level: Int,
    val pathId: String = "default"
)