package galacticCombat.tower

import com.almasb.fxgl.texture.Texture

data class TowerData(
    val damage: Double,
    val attackDelay: Double,
    val range: Double,
    val texture: String
)