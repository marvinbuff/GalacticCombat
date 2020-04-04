package galacticCombat.entities.generic

import com.almasb.fxgl.dsl.components.view.ChildViewComponent
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class RangeIndicatorComponent(range: Double, translation: Point2D) : ChildViewComponent(translation.x, translation.y, false) {

  private val circle = getCircleIndicator(range)
  //todo somehow connect to range property
  //todo change colour based on whether the position is valid or not


  init {
    viewRoot.children.add(circle)
  }

  private fun getCircleIndicator(range: Double): Circle =
    Circle(range, Color.web("63f939", 0.05))
      .apply {
        stroke = Color.BLACK
        strokeWidth = 2.0
      }

}