package galacticCombat.level.json

import kotlinx.serialization.Serializable

@Serializable
data class LevelData(
    val title: String,
    val settings: Settings,
    val waves: List<Wave>,
    val paths: List<Path>
)
