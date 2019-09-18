package galacticCombat

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.FXGL.Companion.getAppHeight
import com.almasb.fxgl.dsl.FXGL.Companion.getAppWidth
import com.almasb.fxgl.dsl.getEventBus
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.dsl.getGameTimer
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.components.CollidableComponent
import com.almasb.fxgl.input.Input
import com.almasb.fxgl.input.UserAction
import com.almasb.fxgl.physics.CollisionHandler
import galacticCombat.event.EnemyReachedGoalEvent
import galacticCombat.invader.InvadorFactory
import galacticCombat.tower.TowerFactory
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import javafx.util.Duration
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class GalacticCombatApp : GameApplication() {
  private lateinit var player: Entity
  val waypoints = arrayListOf<Point2D>()

  override fun initSettings(settings: GameSettings) {
    settings.width = AppConfig.WIDTH
    settings.height = AppConfig.HEIGHT
    settings.title = AppConfig.TITLE
    settings.version = AppConfig.VERSION
//    settings.appIcon = "icon.png" //TODO enable icon
    settings.isMenuEnabled = false
    settings.isIntroEnabled = false
    settings.applicationMode = AppConfig.MODE
  }

  override fun initGame() {
    getGameWorld().addEntityFactory(TowerFactory())
    getGameWorld().addEntityFactory(InvadorFactory())

    //TODO replace hard-coded level input by file reading
//    val level = getAssetLoader().loadLevel("experiment.level", GalacticCombatLevelLoader())
//    getGameWorld().setLevel(level)
    waypoints.addAll(arrayListOf(Point2D(50.0, 50.0), Point2D(150.0, 350.0), Point2D(350.0, 350.0), Point2D(130.0, 20.0)))

    showPoints(waypoints)

    val enemiesLeft = SimpleBooleanProperty()
    enemiesLeft.bind(getGameState().intProperty(GameVars.ENEMIES_TO_SPAWN.id).greaterThan(0))

    val spawnInvader = Runnable {
      getGameState().increment(GameVars.ENEMIES_TO_SPAWN.id, -1)
      getGameWorld().spawn(
        ENEMY_ID,
        SpawnData(waypoints.first().x - 12.5, waypoints.first().y - 12.5).put("color", Color.BLUE).put("index", 1)
      )
    }

    getGameTimer().runAtIntervalWhile(spawnInvader, Duration.seconds(2.0), enemiesLeft)

    getEventBus().addEventHandler(EnemyReachedGoalEvent.ANY,
      EventHandler { event ->
        event.enemy.removeFromWorld()
      })
  }

  override fun initInput() {
    val input = FXGL.getInput()

    input.addAction(KeyCode.D, "Move Right") { player.rotateBy(5.0) }
    input.addAction(KeyCode.A, "Move Left") { player.rotateBy(-5.0) }

    input.addAction(KeyCode.W, "Move") {
      val velocity = 5
      player.x += cos(player.rotation.toRad()) * velocity
      player.y += sin(player.rotation.toRad()) * velocity
    }

    input.addAction(object : UserAction("Place Tower") {
      private val worldBounds = Rectangle2D(0.0, 0.0, getAppWidth().toDouble(), getAppHeight() - 100.0 - 40)

      override fun onActionBegin() {
        if (worldBounds.contains(input.mousePositionWorld)) {
          getGameWorld().spawn(
            TOWER_ID,
            SpawnData(input.mouseXWorld, input.mouseYWorld).put("color", Color.BLACK).put("index", 1)
          )
        }
      }
    }, MouseButton.PRIMARY)


    //add cheats here by checking:
    //getSettings().getApplicationMode() == ApplicationMode.DEVELOPER
  }

  override fun initUI() {
//    val texture = FXGL.texture("meteor.png")
//    texture.translateX = 50.0
//    texture.translateY = 150.0
//    texture.disableProperty()
//    FXGL.getGameScene().addUINode(texture)

    val toSpawnLabel = Text()
    toSpawnLabel.translateX = 50.0
    toSpawnLabel.translateY = 100.0
    toSpawnLabel.textProperty().bind(getGameState().intProperty(GameVars.ENEMIES_TO_SPAWN.id).asString())
    FXGL.getGameScene().addUINode(toSpawnLabel) // add to the scene graph

    val activeEnemiesLabel = Text()
    activeEnemiesLabel.translateX = 150.0
    activeEnemiesLabel.translateY = 100.0
    activeEnemiesLabel.textProperty().bind(getGameState().intProperty(GameVars.ALIVE_ENEMIES.id).asString())
    FXGL.getGameScene().addUINode(activeEnemiesLabel) // add to the scene graph
  }

  override fun initGameVars(vars: MutableMap<String, Any>) {
    GameVars.values().forEach { gameVar ->
      vars[gameVar.id] = gameVar.initial
    }
  }

  override fun initPhysics() {
    //TODO move physic handler to own method
    FXGL.getPhysicsWorld().addCollisionHandler(object : CollisionHandler(EntityType.TOWER, EntityType.INVADER) {
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

private fun showPoints(waypoints: List<Point2D>) {
  operator fun Point2D.component1(): Double = x
  operator fun Point2D.component2(): Double = y
  waypoints.forEach { (x, y) ->
    FXGL.entityBuilder()
      .at(x, y)
      .type(EntityType.BARRICADE)
      .view(Rectangle(20.0, 20.0, Color.BLUE))
      .with(CollidableComponent(true))
      .buildAndAttach()
    FXGL.entityBuilder()
      .at(x, y)
      .type(EntityType.BARRICADE)
      .view(Rectangle(5.0, 5.0, Color.RED))
      .buildAndAttach()
  }
}

private fun Input.addAction(code: KeyCode, title: String, action: () -> Unit) {
  this.addAction(object : UserAction(title) {
    override fun onAction() = action()
  }, code)
}

private fun Double.toRad() = this * PI / 180
//endregion