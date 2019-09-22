package galacticCombat.tower

import com.almasb.fxgl.dsl.getInput
import com.almasb.fxgl.entity.component.Component
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.MouseEvent

class PlaceholderComponent(towerData: TowerData) : Component() {

  private val onClick = EventHandler<MouseEvent> { event ->
    TowerFactory.spawnFromTowerData(towerData, Point2D(event.x, event.y))
    entity.removeFromWorld()
  }

  override fun onAdded() {
    entity.viewComponent.addEventHandler(MouseEvent.MOUSE_CLICKED, onClick)
  }

  //TODO maybe add DraggableComponent and OnClickComponent
  override fun onUpdate(tpf: Double) {
    entity.position = getInput().mousePositionWorld.subtract(TowerComponent.center)

    //TODO check ground for space
  }

  override fun onRemoved() {
    entity.viewComponent.removeEventHandler(MouseEvent.MOUSE_CLICKED, onClick)
  }
}