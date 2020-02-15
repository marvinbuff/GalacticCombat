package galacticCombat.level.json

import kotlinx.serialization.Serializable

@Serializable
data class Path(
    val id: String,
    val wayPoints: MutableList<Pair<Int, Int>>
) : List<Pair<Int, Int>> by wayPoints
