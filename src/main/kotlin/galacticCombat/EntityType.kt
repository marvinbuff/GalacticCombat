package galacticCombat

// we need to have const val for the compiler to use it in the annotations
const val TOWER_ID = "Tower"
const val INVADER_ID = "Invader"
const val BULLET_ID = "Bullet"

enum class EntityType {
  TOWER, //searches for SHIP; launches PROJECTILES
  INVADER, //gets hit by PROJECTILE; Seeks WAYPOINT
  BULLET, //seeks SHIP; hits SHIP; hits BARRICADE
  BARRICADE,
  WAYPOINT
  ;
}