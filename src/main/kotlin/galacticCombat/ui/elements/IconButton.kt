package galacticCombat.ui.elements

import com.almasb.fxgl.texture.Texture
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button

open class IconButton(icon: Texture, handler: EventHandler<ActionEvent>) : Button() {

    init {
        onAction = handler
        isDisable = false
        graphic = icon
        style = STYLE_NORMAL

        setOnMousePressed { style = STYLE_PRESSED }
        setOnMouseReleased { style = STYLE_NORMAL }
    }

}

// TODO eventually move to css file
private val STYLE_NORMAL = "-fx-padding: 2, 2, 2, 2;"
private val STYLE_PRESSED = "-fx-padding: 3 1 1 3;"