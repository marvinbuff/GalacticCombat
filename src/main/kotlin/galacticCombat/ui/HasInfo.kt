package galacticCombat.ui

import javafx.beans.binding.StringBinding
import javafx.scene.image.Image

interface HasInfo {
  fun getInformation(): StringBinding

  fun getTitle(): String

  fun getTexture(): Image
}