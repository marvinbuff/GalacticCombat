package galacticCombat

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.FXGL.Companion.getAppHeight
import com.almasb.fxgl.dsl.FXGL.Companion.getAppWidth
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.components.CollidableComponent
import com.almasb.fxgl.input.Input
import com.almasb.fxgl.input.UserAction
import com.almasb.fxgl.physics.CollisionHandler
import galacticCombat.tower.TowerFactory
import javafx.geometry.Point2D
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import javafx.geometry.Rectangle2D
import javafx.scene.input.MouseButton


class GalacticCombatApp : GameApplication() {
  private lateinit var player: Entity
  private val waypoints = arrayListOf<Point2D>()

  override fun initSettings(settings: GameSettings) {
    settings.width = 800
    settings.height = 600
    settings.title = "Galactic Combat"
    settings.version = "0.1"
    settings.isMenuEnabled = false
    settings.isIntroEnabled = false
  }

  override fun initGame() {
    getGameWorld().addEntityFactory(TowerFactory())

    waypoints.addAll(arrayListOf(Point2D(50.0, 50.0), Point2D(150.0, 350.0), Point2D(350.0, 350.0)))

    player = FXGL.entityBuilder()
      .type(EntityType.SHIP)
      .at(150.0, 150.0)
      .viewWithBBox("playership_dd_1.gif")
      .with(CollidableComponent(true))
//      .with(ProjectileComponent(Point2D(0.0, 1.0),2.0))
      .buildAndAttach()


    FXGL.entityBuilder()
      .at(250.0, 150.0)
      .type(EntityType.TOWER)
      .view(Rectangle(20.0, 20.0, Color.BLUE))
      .with(CollidableComponent(true))
      .buildAndAttach()
  }

  override fun initInput() {
    val input = FXGL.getInput()

    input.addAction(KeyCode.D, "Move Right") { player.rotateBy(5.0) }
    input.addAction(KeyCode.A, "Move Left") { player.rotateBy(-5.0) }

    input.addAction(KeyCode.W, "Move") {
      val velocity = 5
      player.x += cos(player.rotation.toRad()) * velocity
      player.y += sin(player.rotation.toRad()) * velocity
      FXGL.getGameState().increment("pixelsMoved", +velocity)
    }

    input.addAction(object : UserAction("Place Tower") {
      private val worldBounds = Rectangle2D(0.0, 0.0, getAppWidth().toDouble(), getAppHeight() - 100.0 - 40)

      override fun onActionBegin() {
        if (worldBounds.contains(input.mousePositionWorld)) {
          getGameWorld().spawn("Tower",
            SpawnData(input.mouseXWorld, input.mouseYWorld).put("color", Color.BLACK).put("index", 1)
          )
        }
      }
    }, MouseButton.PRIMARY)
  }

  override fun initUI() {
    val texture = FXGL.texture("meteor.png")
    texture.translateX = 50.0
    texture.translateY = 150.0
    texture.disableProperty()
    FXGL.getGameScene().addUINode(texture)

    val textPixels = Text()
    textPixels.translateX = 50.0
    textPixels.translateY = 100.0
    textPixels.textProperty().bind(FXGL.getGameState().intProperty("pixelsMoved").asString())
    FXGL.getGameScene().addUINode(textPixels) // add to the scene graph
  }

  override fun initGameVars(vars: MutableMap<String, Any>) {
    vars["pixelsMoved"] = 0
    vars["gold"] = 0 //used to buy towers
    vars["mana"] = 0 //used to cast magic
    vars["health"] = 10 //game over on 0
    vars["score"] = 0 //points for your prowess
  }

  override fun initPhysics() {
    FXGL.getPhysicsWorld().addCollisionHandler(object : CollisionHandler(EntityType.TOWER, EntityType.SHIP) {

      // order of types is the same as passed into the constructor of the CollisionHandler
      override fun onCollisionBegin(tower: Entity?, ship: Entity?) {
        tower!!.removeFromWorld()
      }
    })
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

private fun Double.toRad() = this * PI / 180
//endregion