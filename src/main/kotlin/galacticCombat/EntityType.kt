package galacticCombat

enum class EntityType {
  TOWER, //searches for SHIP; launches PROJECTILES
  SHIP, //gets hit by PROJECTILE; Seeks WAYPOINT
  PROJECTILE, //seeks SHIP; hits SHIP; hits BARRICADE
  BARRICADE,
  WAYPOINT
}