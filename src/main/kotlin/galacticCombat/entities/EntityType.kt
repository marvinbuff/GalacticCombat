package galacticCombat.entities

import com.almasb.fxgl.dsl.EntityBuilder

// We need to have const val for the compiler to use it in the annotations
const val TOWER_SPAWN_ID = "Tower"
const val PLACEHOLDER_SPAWN_ID = "Tower Placeholder"
const val BULLET_SPAWN_ID = "Bullet"
const val SPAWN_ID_INVADER = "Invader"
const val PATH_VERTEX_SPAWN_ID = "Path Vertex"
const val PATH_EDGE_SPAWN_ID = "Path Edge"

enum class EntityType(val zLevel: Int = 0) {
  BARRICADE,
  CONTROLLER,
  SLIDER_INVADER(3),
  SLIDER_PIN(2),
  TOWER(0),
  BULLET(-1),
  INVADER(-2),
  PATH(-5)
  ;
}

fun EntityBuilder.setTypeAdvanced(type: EntityType) = this.type(type).zIndex(type.zLevel)