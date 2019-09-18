package galacticCombat.tower

import com.almasb.fxgl.entity.component.Component

data class TowerDataComponent(
  val damage: Double,
  val attackDelay: Double
) : Component()