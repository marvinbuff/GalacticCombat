package galacticCombat

import com.almasb.fxgl.app.ApplicationMode
import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getAppHeight
import com.almasb.fxgl.dsl.getAppWidth
import com.almasb.fxgl.dsl.getAssetLoader
import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.dsl.getGameTimer
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.getPhysicsWorld
import com.almasb.fxgl.dsl.getSettings
import com.almasb.fxgl.dsl.onEvent
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.input.Input
import com.almasb.fxgl.input.UserAction
import com.almasb.fxgl.physics.CollisionHandler
import com.almasb.fxgl.saving.DataFile
import galacticCombat.configs.AppConfig
import galacticCombat.configs.AssetConfig
import galacticCombat.configs.GameVarsBoolean
import galacticCombat.configs.GameVarsInt
import galacticCombat.configs.LevelDataVar
import galacticCombat.configs.LevelGameVars
import galacticCombat.entities.EntityType
import galacticCombat.entities.bullet.BulletFactory
import galacticCombat.entities.controller.LevelControllerFactory
import galacticCombat.entities.invader.InvaderFactory
import galacticCombat.entities.tower.PlaceholderFactory
import galacticCombat.entities.tower.TowerFactory
import galacticCombat.events.GameEvents
import galacticCombat.events.InvaderEvents
import galacticCombat.handlers.gameLost
import galacticCombat.handlers.gameWon
import galacticCombat.level.GalacticCombatLevelLoader
import galacticCombat.level.json.Path
import galacticCombat.ui.GameViewController
import galacticCombat.utils.fire
import galacticCombat.utils.toPoint
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.util.Duration
import kotlinx.coroutines.Runnable
import java.io.Serializable
import kotlin.collections.set

fun main(args: Array<String>) {
  GameApplication.launch(GalacticCombatApp::class.java, args)
}

val backgroundColor = Color.web("133a19", 1.0)
val pathColor = Color.web("5d665f")

class GalacticCombatApp : GameApplication() {

  override fun initSettings(settings: GameSettings) {
    settings.width = AppConfig.WIDTH
    settings.height = AppConfig.HEIGHT
    settings.title = AppConfig.TITLE
    settings.version = AppConfig.VERSION
    settings.appIcon = AssetConfig.getUI("logo.png")
    settings.isMenuEnabled = false
    settings.isIntroEnabled = false
    settings.applicationMode = AppConfig.MODE
  }

  override fun initGame() {
    listOf(TowerFactory(), InvaderFactory(), BulletFactory(), PlaceholderFactory(), LevelControllerFactory())
        .forEach(getGameWorld()::addEntityFactory)

    val level = getAssetLoader().loadLevel("level_one.level", GalacticCombatLevelLoader())
    getGameWorld().setLevel(level)
    val levelData = LevelDataVar.get()

    //Draw Path
    levelData.paths.forEach { showPath(it) }

    LevelGameVars.HEALTH.property().addListener { _, _, newValue ->
      if (newValue.toInt() <= 0) GameEvents(GameEvents.LEVEL_LOST).fire()
    }

    val trickle = Runnable {
      LevelGameVars.GOLD.increment(levelData.settings.trickleGold)
      GameVarsInt.SCORE.increment(levelData.settings.trickleScore)
    }

    getGameTimer().runAtInterval(trickle, Duration.seconds(AppConfig.TRICKLE_RATE))

    // Invader Events
    onEvent(InvaderEvents.INVADER_REACHED_GOAL) { event ->
      LevelGameVars.HEALTH.increment(-event.invader.data.damage)
      event.invader.entity.removeFromWorld()
    }

    onEvent(InvaderEvents.INVADER_KILLED) { event ->
      LevelGameVars.EXPERIENCE.increment(event.invader.data.xp)
      GameVarsInt.SCORE.increment(event.invader.data.bounty)
      GameVarsInt.ALIVE_INVADERS.increment(-1)
      event.invader.entity.removeFromWorld()

      //check if game won
      if (GameVarsBoolean.ALL_ENEMIES_SPAWNED.get() && GameVarsInt.ALIVE_INVADERS.get() == 0) {
        GameEvents(GameEvents.LEVEL_WON).fire()
      }
    }

    onEvent(GameEvents.LEVEL_WON) { gameWon() }
    onEvent(GameEvents.LEVEL_LOST) { gameLost() }

  }

  override fun initInput() {
    val input = FXGL.getInput()

    // Developer Commands
    if (getSettings().applicationMode == ApplicationMode.DEVELOPER) {

      input.addAction(object : UserAction("Check Coordinate") {
        private val worldBounds = Rectangle2D(0.0, 0.0, getAppWidth().toDouble(), getAppHeight().toDouble())

        override fun onActionBegin() {
          if (worldBounds.contains(input.mousePositionWorld)) {
            println("Clicked on (${input.mouseXWorld}/${input.mouseYWorld})")
          }
        }
      }, MouseButton.PRIMARY)
    }
  }

  override fun initUI() {
    val controller = GameViewController()
    val ui = getAssetLoader().loadUI("GameView.fxml", controller)

    ui.root.stylesheets += getAssetLoader().loadCSS("galacticCombatStyle.css").externalForm

    getGameScene().addUI(ui)
    getGameScene().setBackgroundColor(backgroundColor)

    ui.root.isPickOnBounds = false
  }

  /**
   * Automatically initializes all games variables as defined in [GameVarsInt] and [GameVarsBoolean].
   */
  override fun initGameVars(vars: MutableMap<String, Any>) {
    GameVarsInt.values().forEach { gameVar ->
      vars[gameVar.id] = gameVar.initial
    }
    GameVarsBoolean.values().forEach { gameVar ->
      vars[gameVar.id] = gameVar.initial
    }
  }

  override fun initPhysics() {
    //TODO move physic handler to own method
    getPhysicsWorld().addCollisionHandler(object : CollisionHandler(EntityType.TOWER, EntityType.INVADER) {
      // order of types is the same as passed into the constructor of the CollisionHandler
      override fun onCollisionBegin(tower: Entity?, ship: Entity?) {
        tower!!.removeFromWorld()
      }
    })
  }

  override fun saveState(): DataFile {
    // possibly not correct and not used!
    val data = getGameState().getInt(GameVarsInt.ENEMIES_TO_SPAWN.id)
    println("Saved $data")
    return DataFile(SaveData(data))
  }

  override fun loadState(dataFile: DataFile?) {
    if (dataFile == null) return
    val data = dataFile.data as SaveData
    println("Loaded ${data.scores}")
    getGameState().increment(GameVarsInt.ENEMIES_TO_SPAWN.id, data.scores)
  }
}

data class SaveData(val scores: Int) : Serializable


//region ---------------- Private Helpers ---------------

private fun showPath(waypoints: Path) {
  waypoints.forEach { addWayVertex(it.toPoint()) }
  waypoints.zipWithNext().forEach { (first, second) ->
    addWayEdge(first.toPoint(), second.toPoint())
  }
}

private fun addWayEdge(first: Point2D, second: Point2D, width: Double = 30.0) {
  val entity = FXGL.entityBuilder()
      .at(first)
      .type(EntityType.PATH)
      .view(Rectangle(first.distance(second), width, pathColor))
      .build()

  entity.translate(0.0, -width / 2)
  entity.transformComponent.rotationOrigin = Point2D(0.0, width / 2)
  entity.rotateToVector(second.subtract(first))

  FXGL.getGameWorld().addEntity(entity)
}

private fun addWayVertex(vertex: Point2D, width: Double = 30.0) {
  FXGL.entityBuilder()
      .at(vertex.x, vertex.y)
      .type(EntityType.PATH)
      .view(Circle(width / 2, pathColor))//Draws the circle around the left upper corner
      .buildAndAttach()
}

private fun Input.addAction(code: KeyCode, title: String, action: () -> Unit) {
  this.addAction(object : UserAction(title) {
    override fun onAction() = action()
  }, code)
}

//endregion