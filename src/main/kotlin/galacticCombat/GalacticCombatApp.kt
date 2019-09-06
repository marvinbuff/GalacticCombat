package galacticCombat

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.FXGL
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle


class GalacticCombatApp : GameApplication() {

  override fun initSettings(settings: GameSettings) {
    settings.width = 800
    settings.height = 600
    settings.title = "Galactic Combat"
    settings.version = "0.1"
  }

  override fun initGame() {
    FXGL.entityBuilder()
      .at(150.0, 150.0)
      .view(Rectangle(40.0, 40.0, Color.BLUE))
      .buildAndAttach()
  }
}

fun main(args: Array<String>) {
  GameApplication.launch(GalacticCombatApp::class.java, args)
}