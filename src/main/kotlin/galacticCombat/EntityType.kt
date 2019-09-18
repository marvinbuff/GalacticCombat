package galacticCombat

const val TOWER_ID = "Tower"
const val ENEMY_ID = "Enemy"

enum class EntityType {
  TOWER, //searches for SHIP; launches PROJECTILES
  INVADER, //gets hit by PROJECTILE; Seeks WAYPOINT
  PROJECTILE, //seeks SHIP; hits SHIP; hits BARRICADE
  BARRICADE,
  WAYPOINT
  ;
}