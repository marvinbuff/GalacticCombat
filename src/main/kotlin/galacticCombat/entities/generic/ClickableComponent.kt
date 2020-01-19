package galacticCombat.entities.generic

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import com.almasb.fxgl.entity.components.ViewComponent
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

@Required(ViewComponent::class)
abstract class ClickableComponent : Component() {

  abstract val onClick: EventHandler<MouseEvent>

  override fun onAdded() {
    super.onAdded()
    entity.viewComponent.addEventHandler(MouseEvent.MOUSE_CLICKED, onClick)
  }

  override fun onRemoved() {
    super.onRemoved()
    entity.viewComponent.removeEventHandler(MouseEvent.MOUSE_CLICKED, onClick)
  }

}