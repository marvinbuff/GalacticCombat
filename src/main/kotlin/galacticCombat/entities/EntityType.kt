package galacticCombat.entities

import com.almasb.fxgl.dsl.EntityBuilder

// We need to have const val for the compiler to use it in the annotations
const val TOWER_SPAWN_ID = "Tower"
const val PLACEHOLDER_SPAWN_ID = "Tower Placeholder"
const val SLIDER_ITEM_SPAWN_ID = "Invader Spawn Item"
const val SLIDER_PIN_SPAWN_ITEM = "Slider Pin"
const val BULLET_SPAWN_ID = "Bullet"
const val INVADER_SPAWN_ID = "Invader"
const val PATH_VERTEX_SPAWN_ID = "Path Vertex"
const val PATH_EDGE_SPAWN_ID = "Path Edge"

const val UI_Z_LEVEL = 1

enum class EntityType(val zLevel: Int = 0) {
  BARRICADE,
  CONTROLLER,
  TOWER_PLACEHOLDER(5),
  SLIDER_PIN(4),
  SLIDER_ITEM(3),

  //UI_Z_LEVEL = 1
  TOWER(0),
  BULLET(-1),
  INVADER(-2),
  PATH(-5)
  ;
}

fun EntityBuilder.setTypeAdvanced(type: EntityType) = this.type(type).zIndex(type.zLevel)