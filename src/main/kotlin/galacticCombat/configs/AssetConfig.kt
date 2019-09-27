package galacticCombat.configs

object AssetConfig {
  private const val ENEMIES = "enemies/"
  private const val TOWERS = "towers/"

  fun getInvader(file: String): String = "$ENEMIES$file"
  fun getTower(file: String): String = "$TOWERS$file"
  fun get(file: String): String = file
}