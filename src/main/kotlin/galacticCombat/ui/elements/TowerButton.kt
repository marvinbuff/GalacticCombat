@file:Suppress("unused")

//The constructor is used in GameView.fxml

package galacticCombat.ui.elements

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.getInput
import com.almasb.fxgl.entity.SpawnData
import galacticCombat.entities.PLACEHOLDER_SPAWN_ID
import galacticCombat.entities.tower.TowerData
import galacticCombat.entities.tower.TowerFactory
import galacticCombat.entities.tower.TowerType
import javafx.beans.NamedArg
import javafx.event.ActionEvent
import javafx.event.EventHandler

class TowerButton(data: TowerData) : IconButton(getIcon(data), getHandler(data)) {
  constructor(@NamedArg("type") type: String) : this(TowerFactory.getTowerData(TowerType.valueOf(type)))
}

private fun getIcon(data: TowerData) = FXGL.texture(data.texture)

private fun getHandler(data: TowerData) = EventHandler<ActionEvent> {
  getGameWorld().spawn(
    PLACEHOLDER_SPAWN_ID,
    SpawnData(getInput().mousePositionWorld).put(TowerData.id, data)
  )
}