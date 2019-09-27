package galacticCombat.entities.invader

enum class InvaderType(val title: String) {
  COMMON("Common Invader"),
  REINFORCED("Common Invader"),
  ACCELERATED("Common Invader")
  ;

  companion object {
    const val id = "InvaderType"
  }
}