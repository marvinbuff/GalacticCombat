package galacticCombat.entities.path

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import galacticCombat.configs.UIConfig
import javafx.geometry.Point2D
import javafx.scene.shape.Rectangle

class PathEdgeComponent(private var startVertex: Point2D, private var endVertex: Point2D) : Component() {

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

    entity.boundingBoxComponent.clearHitBoxes()
    entity.boundingBoxComponent.addHitBox(HitBox(BoundingShape.box(rectangle.width, rectangle.height)))
  }
}