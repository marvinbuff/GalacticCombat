package galacticCombat.entities.controller

import com.almasb.fxgl.dsl.components.view.ChildViewComponent
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.Button

//todo change childviewcomponent to just component usable as view?
//make button clickable with skip action, this will need the levelcontrollcomponent
class NumberDisplayComponent(number: SimpleDoubleProperty) : ChildViewComponent(-5.0, -15.0, false) {
  private val button = Button()

  init {
    button.textProperty().bind(number.asString())
    viewRoot.children.addAll(button)
  }
}