package galacticCombat.ui

import com.almasb.fxgl.app.GameScene
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.ui.InGamePanel
import com.almasb.sslogger.Logger
import galacticCombat.GameVars
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.TitledPane
import javafx.scene.layout.*
import javafx.scene.paint.Color

class TopBar(private val scene: GameScene) {

    private val log = Logger.get(javaClass)

    private val panel = InGamePanel(350.0, scene.height)

    val isOpen: Boolean
        get() = panel.isOpen

    init {
        panel.styleClass.add("dev-pane")

        val pane = TitledPane("xx", createGameVarsPane())
        pane.prefWidth = scene.width
        pane.prefHeight = 100.0

        pane.isMouseTransparent = true
        pane.background = Background(BackgroundFill(Color.color(0.7, 0.6, 0.7, 0.6), null, null))

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
        val vbox = VBox()
        vbox.padding = Insets(15.0)
        vbox.alignment = Pos.TOP_CENTER

        val pane = GridPane()
        pane.hgap = 25.0
        pane.vgap = 10.0

        GameVars.getVarsInTopBar().forEachIndexed { index, key ->
            val textKey = FXGL.getUIFactory().newText(key.id, Color.BLUE, 18.0)

            val value = FXGL.getGameState().properties.getValueObservable(key.id)
            val textValue = FXGL.getUIFactory().newText("", Color.WHITE, 18.0)

            textValue.textProperty().bind((value as SimpleIntegerProperty).asString())

            pane.addRow(index, textKey, textValue)
        }

        vbox.children += pane

        return vbox
    }
}