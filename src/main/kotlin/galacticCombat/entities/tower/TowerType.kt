package galacticCombat.entities.tower

enum class TowerType(val title: String) {
  CANNON("Cannon Tower"),
  SPORE("Spore Launcher"),
  TACTICAL("Tactical Tower"),
  CRYONIC("Cryonic Tower"),
  STORM("Storm Conjurer");

  companion object {
    const val id = "TowerType"
  }
}