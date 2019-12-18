package galacticCombat.ui

import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameState
import galacticCombat.configs.AppConfig
import galacticCombat.configs.GameVarsInt
import galacticCombat.configs.IntGameVar
import galacticCombat.configs.LevelGameVars
import galacticCombat.entities.tower.TowerFactory
import galacticCombat.entities.tower.TowerType
import galacticCombat.ui.elements.TowerButton
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

fun initializeUi() {
  val scene = getGameScene()
  scene.setBackgroundColor(Color.DARKGREEN)

  val borderPane = BorderPane()
  borderPane.setPrefSize(AppConfig.WIDTH.toDouble(), AppConfig.HEIGHT.toDouble())
  borderPane.padding = Insets(20.0)
  borderPane.isMouseTransparent = false
  scene.addUINode(borderPane)

  borderPane.top = createTopBanner()
  borderPane.bottom = createFooter()
  borderPane.left = createShopSideBar()
  borderPane.right = createInfoSideBar()
}

private fun createFooter(): Pane {
  val pane = AnchorPane()
  pane.style

  return pane
}

private fun createTopBanner(): Pane {
  val pane = AnchorPane()
  pane.style = Style.topBannerStyle
  pane.children += Label("Wave X")

  return pane
}

private fun createShopSideBar(): Pane {
  val pane = GridPane()
//  pane.isMouseTransparent = false
  pane.style = Style.infoSideBarStyle
  pane.hgap = 25.0
  pane.vgap = 10.0

  TowerType.values().forEach {
    val button = TowerButton(TowerFactory.getTowerData(it))
    pane.addRow(pane.columnCount, button)
  }

  return pane
}

private fun createInfoSideBar(): Pane {
  val vbox = VBox()
  vbox.style = Style.shopSideBarStyle


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