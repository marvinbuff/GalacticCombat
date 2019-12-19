package galacticCombat.entities.controller

import com.almasb.fxgl.dsl.components.view.ChildViewComponent
import javafx.beans.property.SimpleDoubleProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.input.MouseEvent

class NumberDisplayComponent(timerComponent: LevelTimerComponent) : ChildViewComponent(-5.0, -15.0, false) {
  private val button = Button()
  private val timeProperty = timerComponent.getTimeProperty()

  private val onClick = EventHandler<MouseEvent> {
    timerComponent.skipToNextWave()
  }

  init {
    button.textProperty().bind(timeProperty.formatAsTime())
    viewRoot.children.addAll(button)
    button.addEventHandler(MouseEvent.MOUSE_CLICKED, onClick)
  }

  private fun SimpleDoubleProperty.formatAsTime() = negate().add(60).asString("Next Wave: %.0f")
}