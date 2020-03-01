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
import com.almasb.sslogger.Logger
import galacticCombat.configs.AppConfig
import galacticCombat.configs.AssetConfig
import galacticCombat.configs.GameVarsBoolean
import galacticCombat.configs.GameVarsInt
import galacticCombat.configs.InfoPanelVar
import galacticCombat.configs.LevelDataVar
import galacticCombat.configs.LevelGameVars
import galacticCombat.configs.UIConfig.LEVEL_COLOR
import galacticCombat.entities.EntityType
import galacticCombat.entities.bullet.BulletFactory
import galacticCombat.entities.controller.LevelControllerFactory
import galacticCombat.entities.invader.InvaderFactory
import galacticCombat.entities.path.PathFactory
import galacticCombat.entities.tower.PlaceholderFactory
import galacticCombat.entities.tower.TowerFactory
import galacticCombat.events.GameEvents
import galacticCombat.events.InvaderEvents
import galacticCombat.handlers.gameLost
import galacticCombat.handlers.gameWon
import galacticCombat.level.GalacticCombatLevelLoader
import galacticCombat.ui.GameViewController
import galacticCombat.utils.fire
import javafx.geometry.Rectangle2D
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.util.Duration
import kotlinx.coroutines.Runnable
import java.io.Serializable
import kotlin.collections.set

fun main(args: Array<String>) {
  GameApplication.launch(GalacticCombatApp::class.java, args)
}

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
    listOf(
        TowerFactory(), InvaderFactory(), BulletFactory(),
        PlaceholderFactory(), LevelControllerFactory(), PathFactory()
    ).forEach(getGameWorld()::addEntityFactory)

    val level = getAssetLoader().loadLevel("level_one.level", GalacticCombatLevelLoader())
    getGameWorld().setLevel(level)
    val levelData = LevelDataVar.get()

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

    // Mouse Handling
    input.addMouseAction(MouseButton.SECONDARY, "Discard Information") {
      InfoPanelVar.get().reset()
    }

    input.addAction(object : UserAction("Add New Waypoint") {
      override fun onActionEnd() {
        LevelDataVar.get().paths.first().wayPoints.add(300 to 300)
        PathFactory.reload()
        log.info("Added a new waypoint.")
      }
    }, KeyCode.K)

    input.addAction(object : UserAction("Remove last Waypoint") {
      override fun onActionEnd() {
        val waypoints = LevelDataVar.get().paths.first().wayPoints
        waypoints.removeAt(waypoints.size - 1)
        PathFactory.reload()
        log.info("Removed last waypoint.")
      }
    }, KeyCode.L)

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
    getGameScene().setBackgroundColor(LEVEL_COLOR)

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

  companion object {
    val log = Logger.get("Galactic Combat App")
  }
}

data class SaveData(val scores: Int) : Serializable


//region ---------------- Private Helpers ---------------

private fun Input.addKeyAction(code: KeyCode, title: String, action: () -> Unit) {
  this.addAction(object : UserAction(title) {
    override fun onAction() = action()
  }, code)
}

private fun Input.addMouseAction(code: MouseButton, title: String, action: () -> Unit) {
  this.addAction(object : UserAction(title) {
    override fun onAction() = action()
  }, code)
}

//endregion