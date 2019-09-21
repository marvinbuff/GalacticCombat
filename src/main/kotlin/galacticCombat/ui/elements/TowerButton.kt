package galacticCombat.ui.elements

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameState
import galacticCombat.AssetsConfig
import galacticCombat.GameVars
import javafx.event.ActionEvent
import javafx.event.EventHandler

val icon = FXGL.texture(AssetsConfig.getTower("2.3.gif"))
val handler = EventHandler<ActionEvent> { _ ->
  val goldId = GameVars.GOLD.id
  val gold = getGameState().getInt(goldId)
  if (gold >= 100) {
    getGameState().increment(goldId, -100)
  }
}

class TowerButton : IconButton(icon, handler)