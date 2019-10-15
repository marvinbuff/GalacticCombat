package galacticCombat.entities.invader

enum class InvaderType(val title: String) {
  COMMON("Common Invader"),
  REINFORCED("Reinforced Invader"),
  ACCELERATED("Accelerated Invader")
  ;

  companion object {
    const val id = "InvaderType"
  }
}