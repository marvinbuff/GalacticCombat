package galacticCombat.configs

object AssetConfig {
  private const val INVADERS = "invaders/"
  private const val TOWERS = "towers/"
  private const val UI = "ui/"

  fun getInvader(file: String): String = "$INVADERS$file"
  fun getTower(file: String): String = "$TOWERS$file"
  fun getUI(file: String): String = "$UI$file"
  fun get(file: String): String = file
}