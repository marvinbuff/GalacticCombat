package galacticCombat.ui

import javafx.beans.binding.StringBinding
import javafx.scene.image.Image

interface HasInfo {
  fun getTitle(): String

  fun getInformation(): StringBinding

  fun getTexture(): Image

  fun activate()

  fun deactivate()
}