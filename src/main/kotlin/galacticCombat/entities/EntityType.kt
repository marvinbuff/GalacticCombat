package galacticCombat.entities

// we need to have const val for the compiler to use it in the annotations
const val TOWER_SPAWN_ID = "Tower"
const val INVADER_SPAWN_ID = "Invader"
const val BULLET_SPAWN_ID = "Bullet"
const val LEVEL_CONTROLLER_ID = "Level Controller"
const val PLACEHOLDER_SPAWN_ID = "Placeholder"

enum class EntityType {
  TOWER,
  INVADER,
  BULLET,
  BARRICADE,
  CONTROLLER
  ;
}