package galacticCombat.entities.generic

import galacticCombat.configs.InfoPanelVar
import galacticCombat.ui.HasInfo
import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent

class InfoComponent(private val infoSource: HasInfo) : ClickableComponent() {

  override val onClick = EventHandler<MouseEvent> { event ->
    if (event.button == MouseButton.PRIMARY) {
      InfoPanelVar.get().set(infoSource)
    }
  }

}