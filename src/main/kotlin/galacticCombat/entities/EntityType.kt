package galacticCombat.entities

import com.almasb.fxgl.dsl.EntityBuilder

// we need to have const val for the compiler to use it in the annotations
const val TOWER_SPAWN_ID = "Tower"
const val BULLET_SPAWN_ID = "Bullet"
const val PLACEHOLDER_SPAWN_ID = "Placeholder"

enum class EntityType(val zLevel: Int = 0) {
  TOWER(0),
  INVADER(-2),
  BULLET(-1),
  BARRICADE,
  CONTROLLER,
  PATH(-5)
  ;
}

fun EntityBuilder.setTypeAdvanced(type: EntityType) = this.type(type).zIndex(type.zLevel)