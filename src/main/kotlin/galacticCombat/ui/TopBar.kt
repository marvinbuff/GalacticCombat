package galacticCombat.ui

import com.almasb.fxgl.app.GameScene
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.ui.InGamePanel
import com.almasb.sslogger.Logger
import galacticCombat.GameVars
import galacticCombat.tower.TowerFactory
import galacticCombat.tower.TowerType
import galacticCombat.ui.elements.TowerButton
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.paint.Color

class TopBar(private val scene: GameScene) {

  private val log = Logger.get(javaClass)

  private val panel = InGamePanel(350.0, scene.height)

  val isOpen: Boolean
    get() = panel.isOpen

  init {
    panel.styleClass.add("dev-pane")

    val pane = Pane(createGameVarsPane())
    pane.prefWidth = scene.width
    pane.prefHeight = 100.0

    pane.isMouseTransparent = false
//        pane.background = Background(BackgroundFill(Color.color(0.7, 0.6, 0.7, 0.6), null, null))

    panel.children += pane
    scene.addUINodes(panel)
  }

  fun open() {
    panel.open()
  }

  fun close() {
    panel.close()
  }

  private fun createGameVarsPane(): Pane {
    val vbox = HBox()
    vbox.padding = Insets(15.0)

    val pane = GridPane()
    pane.hgap = 25.0
    pane.vgap = 10.0

    GameVars.getVarsInTopBar().forEachIndexed { index, key ->
      val color = Color.BLUE
      val fontSize = 12.0
      val textKey = FXGL.getUIFactory().newText(key.id, color, fontSize)

      val value = FXGL.getGameState().properties.getValueObservable(key.id)
      val textValue = FXGL.getUIFactory().newText("", color, fontSize)

      textValue.textProperty().bind((value as SimpleIntegerProperty).asString())

      pane.addColumn(index, textKey, textValue)
    }

    //button
    //TODO go over all towers and create buttons for them
    val button = TowerButton(TowerFactory.getTowerData(TowerType.CANNON))
    pane.addColumn(pane.columnCount, button)

    vbox.children += pane

    return vbox
  }
}