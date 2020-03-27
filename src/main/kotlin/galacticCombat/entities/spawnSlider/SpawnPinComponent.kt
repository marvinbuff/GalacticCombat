package galacticCombat.entities.spawnSlider

import com.almasb.fxgl.entity.component.Component
import galacticCombat.configs.LevelController
import galacticCombat.ui.SpawnSliderController

class SpawnPinComponent : Component() {

  override fun onAdded() {
    val xPosition = LevelController.get().timerComponent
      .getTimeProperty()
      .multiply(SpawnSliderController.SLIDER_STEP)
      .add(entity.x)
    entity.xProperty().bind(xPosition)
  }
}