package galacticCombat.entities.tower

enum class TowerType(val title: String) {
  CANNON("Cannon Tower"),
  SPORE("Spore Launcher"),
  RAY("Ray Blaster"),
  CRYONIC("Cryonic Emitter"),
  BOMBER("Bomb Thrower");

  companion object {
    const val id = "TowerType"
  }
}