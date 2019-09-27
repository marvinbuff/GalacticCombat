package galacticCombat.ui

import com.almasb.fxgl.app.GameScene
import com.almasb.fxgl.ui.InGamePanel
import galacticCombat.entities.tower.TowerFactory
import galacticCombat.entities.tower.TowerType
import galacticCombat.ui.elements.TowerButton
import javafx.geometry.HorizontalDirection
import javafx.geometry.Insets
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane

class SideBar(scene: GameScene) {

  private val panel = InGamePanel(350.0, scene.height, HorizontalDirection.RIGHT)

  init {
    panel.styleClass.add("dev-pane")
    panel.layoutX = 600.0

    val pane = Pane(createShopPane())
    pane.prefWidth = 200.0
    pane.prefHeight = scene.height

    pane.isMouseTransparent = false

    panel.children += pane
    scene.addUINodes(panel)
  }

  fun open() {
    panel.open()
  }

  private fun createShopPane(): Pane {
    val hbox = HBox()
    hbox.padding = Insets(15.0)

    val pane = GridPane()
    pane.hgap = 25.0
    pane.vgap = 10.0

    TowerType.values().forEach {
      val button = TowerButton(TowerFactory.getTowerData(it))
      pane.addRow(pane.columnCount, button)
    }

    hbox.children += pane

    return hbox
  }
}