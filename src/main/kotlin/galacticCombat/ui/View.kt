package galacticCombat.ui

import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameState
import galacticCombat.configs.GameVarsInt
import galacticCombat.configs.IntGameVar
import galacticCombat.configs.LevelGameVars
import galacticCombat.entities.tower.TowerFactory
import galacticCombat.entities.tower.TowerType
import galacticCombat.ui.elements.TowerButton
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox

fun initializeUi() {
  val scene = getGameScene()

  val borderPane = BorderPane()
  scene.addUINode(borderPane)

  borderPane.left = createLeftSideBar()
  borderPane.right = createRightSideBar()
}

private fun createRightSideBar(): Pane {
  val pane = GridPane()
  pane.hgap = 25.0
  pane.vgap = 10.0

  TowerType.values().forEach {
    val button = TowerButton(TowerFactory.getTowerData(it))
    pane.addRow(pane.columnCount, button)
  }

  return pane
}

private fun createLeftSideBar(): Pane {
  val vbox = VBox()
  vbox.style = Style.vboxStyle


  listOf<IntGameVar>(LevelGameVars.GOLD, LevelGameVars.EXPERIENCE, LevelGameVars.HEALTH, GameVarsInt.SCORE)
      .forEach { key ->
        val value = getGameState().properties.getValueObservable(key.id)

        val binded = (value as SimpleIntegerProperty).asString("${key.id}: %d")

        val label = Label()
        label.textProperty().bind(binded)

        label.style = Style.labelStyle

        vbox.children += label
      }

  return vbox
}