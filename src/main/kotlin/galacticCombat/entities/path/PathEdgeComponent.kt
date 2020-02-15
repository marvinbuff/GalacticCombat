package galacticCombat.entities.path

import com.almasb.fxgl.entity.component.Component
import galacticCombat.configs.UIConfig
import javafx.geometry.Point2D
import javafx.scene.shape.Rectangle

class PathEdgeComponent(var startVertex: Point2D, var endVertex: Point2D) : Component() {

  override fun onAdded() {
    super.onAdded()
    updateView()
  }

  fun updateStartVertex(vertex: Point2D) {
    startVertex = vertex
    updateView()
  }

  fun updateEndVertex(vertex: Point2D) {
    endVertex = vertex
    updateView()
  }

  private fun updateView() {
    val rectangle = Rectangle(startVertex.distance(endVertex), PathFactory.pathWidth, UIConfig.PATH_COLOR)

    entity.viewComponent.clearChildren()
    entity.viewComponent.addChild(rectangle)

    entity.position = startVertex
    entity.transformComponent.translate(0.0, -PathFactory.halfPathWidth)
    entity.transformComponent.rotationOrigin = Point2D(0.0, PathFactory.halfPathWidth)
    entity.rotateToVector(endVertex.subtract(startVertex))
  }
}