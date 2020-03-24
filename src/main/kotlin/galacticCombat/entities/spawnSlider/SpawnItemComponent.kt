package galacticCombat.entities.spawnSlider

import com.almasb.fxgl.dsl.getInput
import com.almasb.fxgl.entity.component.Component
import galacticCombat.entities.invader.InvaderData
import galacticCombat.ui.SpawnSliderController.Companion.SLIDER_WIDTH
import galacticCombat.ui.SpawnSliderController.Companion.SLIDER_X
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

class SpawnItemComponent(private val invaderData: InvaderData) : Component() {

  var isDragging = false
    private set

  private var offsetX = 0.0

  private val onPress = EventHandler<MouseEvent> {
    isDragging = true
    offsetX = getInput().mouseXWorld - entity.anchoredPosition.x
  }

  private val onRelease = EventHandler<MouseEvent> { isDragging = false }

  override fun onAdded() {
    entity.viewComponent.addEventHandler(MouseEvent.MOUSE_PRESSED, onPress)
    entity.viewComponent.addEventHandler(MouseEvent.MOUSE_RELEASED, onRelease)
  }

  override fun onUpdate(tpf: Double) {
    if (!isDragging)
      return

    val newX = (getInput().mouseXWorld - offsetX).coerceAtLeast(SLIDER_X).coerceAtMost(SLIDER_X+SLIDER_WIDTH)
    entity.setAnchoredPosition(newX, entity.anchoredPosition.y)
  }

  override fun onRemoved() {
    entity.viewComponent.removeEventHandler(MouseEvent.MOUSE_PRESSED, onPress)
    entity.viewComponent.removeEventHandler(MouseEvent.MOUSE_RELEASED, onRelease)
  }
}