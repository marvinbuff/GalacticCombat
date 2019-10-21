package galacticCombat.entities.controller

import com.almasb.fxgl.dsl.components.view.ChildViewComponent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.input.MouseEvent

//make button clickable with skip action, this will need the levelcontrollcomponent
class NumberDisplayComponent(timerComponent: LevelTimerComponent) : ChildViewComponent(-5.0, -15.0, false) {
  private val button = Button()
  private val timeProperty = timerComponent.getTimeProperty()

  private val onClick = EventHandler<MouseEvent> { event ->
    println("Clicked button on time ${timeProperty.value}")
  }

  init {
    button.textProperty().bind(timeProperty.asString("Next Wave: %.0f"))
    viewRoot.children.addAll(button)
    button.addEventHandler(MouseEvent.MOUSE_CLICKED, onClick)
  }
}