package galacticCombat.ui.elements

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.getInput
import com.almasb.fxgl.entity.SpawnData
import galacticCombat.configs.GameVars
import galacticCombat.entities.PLACEHOLDER_SPAWN_ID
import galacticCombat.entities.tower.TowerData
import javafx.event.ActionEvent
import javafx.event.EventHandler


class TowerButton(data: TowerData) : IconButton(getIcon(data), getHandler(data))

private fun getIcon(data: TowerData) = FXGL.texture(data.texture)

private fun getHandler(data: TowerData) = EventHandler<ActionEvent> { event ->
  val goldId = GameVars.GOLD.id

  val gold = getGameState().getInt(goldId)
  if (gold >= data.price) {
    getGameState().increment(goldId, -data.price)
    getGameWorld().spawn(
      PLACEHOLDER_SPAWN_ID,
        SpawnData(getInput().mousePositionWorld).put(TowerData.id, data)
    )

  }
}