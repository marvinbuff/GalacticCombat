package galacticCombat.invader

data class InvaderData(
    val maxHealth: Double,
    val baseSpeed: Double,
    val bounty: Double,
    val damage: Int = 1
) {
  companion object {
    const val id = "InvaderData"
  }
}