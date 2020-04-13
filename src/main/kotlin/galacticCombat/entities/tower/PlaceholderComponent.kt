package galacticCombat.entities.tower

import com.almasb.fxgl.dsl.getInput
import com.almasb.fxgl.entity.component.Required
import galacticCombat.configs.GameConfig
import galacticCombat.configs.LevelGameVars
import galacticCombat.entities.generic.ClickableComponent
import galacticCombat.entities.generic.RangeIndicatorComponent
import galacticCombat.moddable.towerConfig.TowerData
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import java.util.concurrent.atomic.AtomicInteger

@Required(RangeIndicatorComponent::class)
class PlaceholderComponent(private val towerData: TowerData) : ClickableComponent() {
  private lateinit var rangeIndicatorComponent: RangeIndicatorComponent
  var isCollidingWith = AtomicInteger(0)

  override val onClick = EventHandler<MouseEvent> { event ->
    when (event.button) {
      MouseButton.PRIMARY   -> {
        if (isValidPosition()) {
          LevelGameVars.GOLD.increment(-towerData.price)
          TowerFactory.spawnFromTowerData(towerData, Point2D(event.x, event.y))
          entity.removeFromWorld()
        }
      }
      MouseButton.SECONDARY -> entity.removeFromWorld()
      else                  -> {
      }
    }
  }

  override fun onUpdate(tpf: Double) {
    entity.anchoredPosition = getInput().mousePositionWorld
    if (isValidPosition()) {
      entity.viewComponent.opacity = 1.0
      rangeIndicatorComponent.circle.stroke = Color.GREEN.darker()
    } else {
      entity.viewComponent.opacity = 0.8
      rangeIndicatorComponent.circle.stroke = Color.RED.darker().darker()
    }
  }

  private fun isValidPosition() = hasSufficientGold() && isAtValidTowerPosition()

  private fun hasSufficientGold(): Boolean = LevelGameVars.GOLD.get() >= towerData.price

  private fun isAtValidTowerPosition(): Boolean {
    return isCollidingWith.get() == 0 && isPositionWithinWorld()
  }

  private fun isPositionWithinWorld(): Boolean {
    val pos = entity.anchoredPosition
    val anchor = entity.localAnchor

    val corners = listOf(
      pos.x - anchor.x to pos.y - anchor.y, //top left
      pos.x + anchor.x to pos.y - anchor.y, //top right
      pos.x - anchor.x to pos.y + anchor.y, //bottom left
      pos.x + anchor.x to pos.y + anchor.y  //bottom right
    ).map { (x, y) -> Point2D(x, y) }

    return corners.all { GameConfig.isPointInWorld(it) }
  }
}