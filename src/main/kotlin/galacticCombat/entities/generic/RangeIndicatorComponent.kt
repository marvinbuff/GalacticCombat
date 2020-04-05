package galacticCombat.entities.generic

import com.almasb.fxgl.dsl.components.view.ChildViewComponent
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class RangeIndicatorComponent(range: Double, translation: Point2D) : ChildViewComponent(translation.x, translation.y, false) {

  // Expose circle property to be accessible by other components.
  val circle = getCircleIndicator(range)

  init {
    viewRoot.children.add(circle)
  }

  private fun getCircleIndicator(range: Double): Circle =
    Circle(range, Color.web("FFFFFF", 0.1))
      .apply {
        stroke = Color.BLACK
        strokeWidth = 2.0
        isMouseTransparent = true
      }

}