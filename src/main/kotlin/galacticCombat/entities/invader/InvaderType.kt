package galacticCombat.entities.invader

/**
 * @param title The invader name shown in the info panel.
 */
enum class InvaderType(val title: String) {
  COMMON("Invader"), // Common Invader
  REINFORCED("Invader"), // Reinforced Invader
  ACCELERATED("Invader") // Accelerated Invader
  ;

  companion object {
    const val id = "InvaderType"
  }
}