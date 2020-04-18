package galacticCombat.entities.invader

/**
 * @param title The invader name shown in the info panel.
 */
enum class InvaderType(val title: String) {
  COMMON("Basic Invader"),
  REINFORCED("Reinforced Invader"),
  ACCELERATED("Scout Invader"),

  // Boss Invaders
  BASTION("The Bastion") // Boss Level 1
  ;

  companion object {
    const val id = "InvaderType"
  }
}