package galacticCombat.entities.spawnSlider

import com.almasb.fxgl.dsl.getInput
import com.almasb.fxgl.entity.component.Component
import galacticCombat.configs.LevelDataVar
import galacticCombat.level.json.InvaderSpawnArgs
import galacticCombat.ui.SpawnSliderController.Companion.SLIDER_WIDTH
import galacticCombat.ui.SpawnSliderController.Companion.SLIDER_X
import galacticCombat.ui.SpawnSliderController.Companion.getTimeFromSliderX
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

class SpawnItemComponent(private val waveIndex: Int, private var invaderSpawnArgs: InvaderSpawnArgs) : Component() {

  private var isDragging = false

  private var offsetX = 0.0

  private val onPress = EventHandler<MouseEvent> {
    isDragging = true
    offsetX = getInput().mouseXWorld - entity.anchoredPosition.x
  }

  private val onRelease = EventHandler<MouseEvent> {
    isDragging = false
    val time = getTimeFromSliderX(entity.anchoredPosition.x)
    invaderSpawnArgs = LevelDataVar.get().changeInvaderSpawnTime(waveIndex, invaderSpawnArgs, time)
  }

  override fun onAdded() {
    entity.viewComponent.addEventHandler(MouseEvent.MOUSE_PRESSED, onPress)
    entity.viewComponent.addEventHandler(MouseEvent.MOUSE_RELEASED, onRelease)
  }

  override fun onUpdate(tpf: Double) {
    if (!isDragging)
      return

    val newX = (getInput().mouseXWorld - offsetX)
      .coerceAtLeast(SLIDER_X)
      .coerceAtMost(SLIDER_X + SLIDER_WIDTH - 1) // minus 1 to capture a closed interval [0-60)
    entity.setAnchoredPosition(newX, entity.anchoredPosition.y)
  }

  override fun onRemoved() {
    entity.viewComponent.removeEventHandler(MouseEvent.MOUSE_PRESSED, onPress)
    entity.viewComponent.removeEventHandler(MouseEvent.MOUSE_RELEASED, onRelease)
  }
}