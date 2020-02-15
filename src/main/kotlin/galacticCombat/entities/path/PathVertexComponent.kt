package galacticCombat.entities.path

import com.almasb.fxgl.dsl.components.DraggableComponent
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import galacticCombat.configs.LevelDataVar
import galacticCombat.utils.toIntegerPair

@Required(DraggableComponent::class)
class PathVertexComponent(pathVertexArgs: PathVertexArgs) : Component() {
  lateinit var draggableComponent: DraggableComponent

  private val pathID = pathVertexArgs.pathID
  private val wayPointIndex = pathVertexArgs.wayPointIndex
  private val previousEdge: PathEdgeComponent? = pathVertexArgs.previousEdge?.getComponent(PathEdgeComponent::class.java)
  private val nextEdge: PathEdgeComponent? = pathVertexArgs.nextEdge?.getComponent(PathEdgeComponent::class.java)

  override fun onAdded() {
    super.onAdded()
    draggableComponent = entity.getComponent(DraggableComponent::class.java)
  }

  override fun onUpdate(tpf: Double) {
    super.onUpdate(tpf)

    // Notify connected edges of dragging
    if (draggableComponent.isDragging) {
      previousEdge?.updateEndVertex(entity.position)
      nextEdge?.updateStartVertex(entity.position)

      // Notify LevelData of change
      val path = LevelDataVar.get().paths.find { it.id == pathID }
      require(path != null)
      path.wayPoints[wayPointIndex] = entity.position.toIntegerPair()
    }
  }
}