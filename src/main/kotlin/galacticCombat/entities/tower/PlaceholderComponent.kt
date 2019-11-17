package galacticCombat.entities.tower

import com.almasb.fxgl.dsl.getAppHeight
import com.almasb.fxgl.dsl.getAppWidth
import com.almasb.fxgl.dsl.getInput
import com.almasb.fxgl.entity.component.Component
import galacticCombat.configs.LevelGameVars
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.input.MouseEvent

class PlaceholderComponent(private val towerData: TowerData) : Component() {

  private val onClick = EventHandler<MouseEvent> { event ->
    if (isValidPosition()) {
      LevelGameVars.GOLD.increment(-towerData.price)
      TowerFactory.spawnFromTowerData(towerData, Point2D(event.x, event.y))
      entity.removeFromWorld()
    }
  }

  override fun onAdded() {
    entity.viewComponent.addEventHandler(MouseEvent.MOUSE_CLICKED, onClick)
  }

  override fun onUpdate(tpf: Double) {
    if (isValidPosition()) {
      entity.viewComponent.opacity = 1.0
      //show as valid
    } else {
      entity.viewComponent.opacity = 0.2
      //show as invalid
    }

    entity.position = getInput().mousePositionWorld.subtract(TowerComponent.center)
  }

  override fun onRemoved() {
    entity.viewComponent.removeEventHandler(MouseEvent.MOUSE_CLICKED, onClick)
  }

  private fun isValidPosition() = hasSufficientGold() && isAtValidTowerPosition()

  private fun hasSufficientGold(): Boolean = LevelGameVars.GOLD.get() >= towerData.price

  private fun isAtValidTowerPosition(): Boolean {
    val mousePosition = getInput().mousePositionWorld
    val worldBounds = Rectangle2D(0.0, 0.0, getAppWidth().toDouble(), getAppHeight().toDouble())
    return worldBounds.contains(mousePosition)
  }
}