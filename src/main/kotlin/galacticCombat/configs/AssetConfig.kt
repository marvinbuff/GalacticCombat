package galacticCombat.configs

import com.almasb.fxgl.dsl.FXGL
import javafx.scene.image.Image

object AssetConfig {
  private const val INVADERS = "invaders/"
  private const val TOWERS = "towers/"
  private const val UI = "ui/"

  fun getInvader(file: String): String = "$INVADERS$file"
  fun getTower(file: String): String = "$TOWERS$file"
  fun getUI(file: String): String = "$UI$file"
  fun get(file: String): String = file
}

fun String.loadInvaderImage(): Image = FXGL.image(AssetConfig.getInvader(this))
fun String.loadTowerImage(): Image = FXGL.image(AssetConfig.getTower(this))
fun String.loadUiImage(): Image = FXGL.image(AssetConfig.getUI(this))
fun String.loadImage(): Image = FXGL.image(AssetConfig.get(this))