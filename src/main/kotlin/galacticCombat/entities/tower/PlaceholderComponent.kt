package galacticCombat.entities.tower

import com.almasb.fxgl.dsl.getAppHeight
import com.almasb.fxgl.dsl.getAppWidth
import com.almasb.fxgl.dsl.getInput
import com.almasb.fxgl.entity.component.Component
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.input.MouseEvent

class PlaceholderComponent(towerData: TowerData) : Component() {

  private val onClick = EventHandler<MouseEvent> { event ->
    if (!isValidTowerPosition(getInput().mousePositionWorld)) return@EventHandler
    TowerFactory.spawnFromTowerData(towerData, Point2D(event.x, event.y))
    entity.removeFromWorld()
  }

  override fun onAdded() {
    entity.viewComponent.addEventHandler(MouseEvent.MOUSE_CLICKED, onClick)
  }

  override fun onUpdate(tpf: Double) {
    val mousePosition = getInput().mousePositionWorld
    entity.position = mousePosition.subtract(TowerComponent.center)
    if (isValidTowerPosition(mousePosition)) {
      //todo draw some indication
    }
  }

  override fun onRemoved() {
    entity.viewComponent.removeEventHandler(MouseEvent.MOUSE_CLICKED, onClick)
  }

  fun isValidTowerPosition(point: Point2D): Boolean {
    val worldBounds = Rectangle2D(0.0, 0.0, getAppWidth().toDouble(), getAppHeight().toDouble())
    return worldBounds.contains(point)
  }
}