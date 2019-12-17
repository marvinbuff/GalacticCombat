package galacticCombat.ui

import com.almasb.fxgl.app.GameScene
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.ui.InGamePanel
import galacticCombat.configs.GameVarsInt
import galacticCombat.configs.IntGameVar
import galacticCombat.configs.LevelGameVars
import galacticCombat.ui.Style.hboxStyle
import galacticCombat.ui.Style.labelStyle
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane

val varsInTopBar: List<IntGameVar> = listOf(LevelGameVars.GOLD, LevelGameVars.EXPERIENCE, LevelGameVars.HEALTH, GameVarsInt.SCORE)

class TopBar(private val scene: GameScene) {

  private val panel = InGamePanel(350.0, scene.height)

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

  private fun createGameVarsPane(): Pane {
    val hbox = HBox()
    hbox.style = hboxStyle


    varsInTopBar.forEachIndexed { _, key ->
      val value = getGameState().properties.getValueObservable(key.id)

      val binded = (value as SimpleIntegerProperty).asString("${key.id}: %d")

      val label = Label()
      label.textProperty().bind(binded)

      label.style = labelStyle

      hbox.children += label
    }

    return hbox
  }
}

object Style {
  const val hboxStyle =
      "-fx-padding: 15;" +
          "-fx-spacing: 10;"

  const val labelStyle = "-fx-background-color: green;" +
      "-fx-font-size: 15;" +
      "-fx-font-family: Times New Roman;" +
      "-fx-border-color: black;" +
      "-fx-border-width: 2;" +
      "-fx-padding: 3;" +
      "-fx-border-style: solid;" +
      "-fx-font-weight: bold;" +
      "-fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 );"
}