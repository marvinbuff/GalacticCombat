package galacticCombat.entities.path

import com.almasb.fxgl.dsl.components.DraggableComponent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required

@Required(DraggableComponent::class)
class PathVertexComponent(previousEdgeEntity: Entity?, nextEdgeEntity: Entity?) : Component() {
  lateinit var draggableComponent: DraggableComponent

  private val previousEdge: PathEdgeComponent? = previousEdgeEntity?.getComponent(PathEdgeComponent::class.java)
  private val nextEdge: PathEdgeComponent? = nextEdgeEntity?.getComponent(PathEdgeComponent::class.java)

  override fun onAdded() {
    super.onAdded()
    draggableComponent = entity.getComponent(DraggableComponent::class.java)
  }

  override fun onUpdate(tpf: Double) {
    super.onUpdate(tpf)

    //Notify connected edges of dragging
    if (draggableComponent.isDragging) {
      previousEdge?.updateEndVertex(entity.position)
      nextEdge?.updateStartVertex(entity.position)
    }
  }

//  private fun isLastVertex() = nextEdge == null
//  private fun isFirstVertex() = previousEdge == null
}