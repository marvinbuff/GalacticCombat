package galacticCombat.configs

object AssetConfig {
  private const val INVADERS = "invaders/"
  private const val TOWERS = "towers/"

  fun getInvader(file: String): String = "$INVADERS$file"
  fun getTower(file: String): String = "$TOWERS$file"
  fun get(file: String): String = file
}