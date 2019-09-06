package galacticCombat

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.input.Input
import com.almasb.fxgl.input.UserAction
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import kotlin.math.cos
import kotlin.math.sin


class GalacticCombatApp : GameApplication() {
  private lateinit var player: Entity

  override fun initSettings(settings: GameSettings) {
    settings.width = 800
    settings.height = 600
    settings.title = "Galactic Combat"
    settings.version = "0.1"
  }

  override fun initGame() {
    player = FXGL.entityBuilder()
      .at(150.0, 150.0)
      .view(Rectangle(20.0, 20.0, Color.BLUE))
      .buildAndAttach()
  }

  override fun initInput() {
    val input = FXGL.getInput()

    input.addAction(object : UserAction("Move Right") {
      //TODO replace with syntactic sugar
      override fun onAction() {
        player.rotateBy(5.0)
      }
    }, KeyCode.D)

    input.addAction(KeyCode.A, "Move Left") { player.rotateBy(-5.0) }

    input.addAction(KeyCode.W, "Move") {
      val velocity = 5
      player.x += cos(player.rotation) * velocity
      player.y += sin(player.rotation) * velocity
      FXGL.getGameState().increment("pixelsMoved", +velocity)
    }
  }

  override fun initUI() {
    val textPixels = Text()
    textPixels.translateX = 50.0
    textPixels.translateY = 100.0
    textPixels.textProperty().bind(FXGL.getGameState().intProperty("pixelsMoved").asString())

    FXGL.getGameScene().addUINode(textPixels) // add to the scene graph
  }

  override fun initGameVars(vars: MutableMap<String, Any>) {
    vars["pixelsMoved"] = 0
  }
}

fun main(args: Array<String>) {
  GameApplication.launch(GalacticCombatApp::class.java, args)
}

//region ---------------- Private Helpers ---------------
private fun Input.addAction(code: KeyCode, title: String, action: () -> Unit) {
  this.addAction(object : UserAction(title) {
    override fun onAction() = action()
  }, code)
}
//endregion