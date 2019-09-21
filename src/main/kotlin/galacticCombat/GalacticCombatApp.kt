package galacticCombat

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.FXGL.Companion.getAppHeight
import com.almasb.fxgl.dsl.FXGL.Companion.getAppWidth
import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameTimer
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.components.CollidableComponent
import com.almasb.fxgl.input.Input
import com.almasb.fxgl.input.UserAction
import com.almasb.fxgl.physics.CollisionHandler
import com.almasb.fxgl.saving.DataFile
import galacticCombat.bullet.BulletFactory
import galacticCombat.event.EnemyReachedGoalEvent
import galacticCombat.invader.InvadorFactory
import galacticCombat.tower.TowerFactory
import galacticCombat.ui.TopBar
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration
import java.io.Serializable
import kotlin.collections.set

fun main(args: Array<String>) {
  GameApplication.launch(GalacticCombatApp::class.java, args)
}

class GalacticCombatApp : GameApplication() {
  val waypoints = arrayListOf<Point2D>()
  val enemiesLeft = SimpleBooleanProperty()

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
    getGameWorld().addEntityFactory(BulletFactory())

    //TODO replace hard-coded level input by file reading
//    val level = getAssetLoader().loadLevel("experiment.level", GalacticCombatLevelLoader())
//    getGameWorld().setLevel(level)
    waypoints.addAll(arrayListOf(Point2D(50.0, 150.0), Point2D(150.0, 350.0), Point2D(550.0, 350.0), Point2D(130.0, 120.0)))

    showPoints(waypoints)

    enemiesLeft.bind(FXGL.getGameState().intProperty(GameVars.ENEMIES_TO_SPAWN.id).greaterThan(0))

    val spawnInvader = Runnable {
      FXGL.getGameState().increment(GameVars.ENEMIES_TO_SPAWN.id, -1)
      getGameWorld().spawn(
        INVADER_ID,
        SpawnData(waypoints.first().x - 12.5, waypoints.first().y - 12.5).put("color", Color.BLUE).put("index", 1)
      )
    }

    getGameTimer().runAtIntervalWhile(spawnInvader, Duration.seconds(2.0), enemiesLeft)

    FXGL.getEventBus().addEventHandler(EnemyReachedGoalEvent.ANY,
      EventHandler { event ->
        event.enemy.removeFromWorld()
      })
  }

  override fun initInput() {
    val input = FXGL.getInput()

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
    // Background
    getGameScene().setBackgroundColor(Color.LIGHTGRAY)

    // initialize upper stats
    val topBar = TopBar(getGameScene())
    topBar.open()

//    val activeEnemiesLabel = Text()
//    activeEnemiesLabel.translateX = 150.0
//    activeEnemiesLabel.translateY = 100.0
//    activeEnemiesLabel.textProperty().bind(getGameState().intProperty(GameVars.ALIVE_ENEMIES.id).asString())
//    FXGL.getGameScene().addUINode(activeEnemiesLabel) // add to the scene graph
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

  override fun saveState(): DataFile {
    val data = FXGL.getGameState().getInt(GameVars.ENEMIES_TO_SPAWN.id)
    println("Saved $data")
    return DataFile(SaveData(data))
  }

  override fun loadState(dataFile: DataFile?) {
    if (dataFile == null) return
    val data = dataFile.data as SaveData
    println("Loaded ${data.scores}")
    FXGL.getGameState().increment(GameVars.ENEMIES_TO_SPAWN.id, data.scores)
  }

}

data class SaveData(val scores: Int) : Serializable


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

//endregion