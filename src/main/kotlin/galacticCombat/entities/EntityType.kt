package galacticCombat.entities

// we need to have const val for the compiler to use it in the annotations
const val TOWER_SPAWN_ID = "Tower"
const val BULLET_SPAWN_ID = "Bullet"
const val PLACEHOLDER_SPAWN_ID = "Placeholder"

enum class EntityType {
  TOWER,
  INVADER,
  BULLET,
  BARRICADE,
  CONTROLLER,
  PATH
  ;
}