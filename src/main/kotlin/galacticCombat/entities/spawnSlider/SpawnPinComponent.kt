package galacticCombat.entities.spawnSlider

import com.almasb.fxgl.entity.component.Component
import galacticCombat.configs.LevelController
import galacticCombat.ui.SpawnSliderController
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class SpawnPinComponent : Component() {

  override fun onAdded() {
    val xPosition = LevelController.get().timerComponent
      .getTimeProperty()
      .multiply(SpawnSliderController.SLIDER_STEP)
      .add(entity.x)
    entity.xProperty().bind(xPosition)
  }

  companion object {
    internal fun createViewRectangle(edge: Double): Rectangle =
      Rectangle(edge, edge, Color.TRANSPARENT).apply {
        stroke = Color.BLACK
        strokeWidth = 2.0
        arcHeight = 10.0
        arcWidth = 10.0
      }

  }
}