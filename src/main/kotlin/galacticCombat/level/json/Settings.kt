package galacticCombat.level.json

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val initialGold: Int = 100,
    val trickleGold: Int = 5,
    val trickleScore: Int = 5,
    val initialHealth: Int = 10,
    val initialExperience: Int = 0,
    val timePerWave: Double = 60.0
)