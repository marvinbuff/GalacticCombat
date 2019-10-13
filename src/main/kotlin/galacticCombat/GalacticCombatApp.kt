package galacticCombat

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getAssetLoader
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
import galacticCombat.configs.AppConfig
import galacticCombat.configs.GameVars
import galacticCombat.configs.LevelDataVar
import galacticCombat.configs.LevelGameVars
import galacticCombat.entities.EntityType
import galacticCombat.entities.INVADER_ID
import galacticCombat.entities.bullet.BulletFactory
import galacticCombat.entities.invader.InvaderFactory
import galacticCombat.entities.invader.InvaderType
import galacticCombat.entities.tower.PlaceholderFactory
import galacticCombat.entities.tower.TowerFactory
import galacticCombat.events.InvaderEvents
import galacticCombat.level.GalacticCombatLevelLoader
import galacticCombat.level.Path
import galacticCombat.ui.SideBar
import galacticCombat.ui.TopBar
import galacticCombat.utils.toPoint
import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.util.Duration
import java.io.Serializable
import kotlin.collections.set

fun main(args: Array<String>) {
  GameApplication.launch(GalacticCombatApp::class.java, args)
}

class GalacticCombatApp : GameApplication() {
  private val enemiesLeft = SimpleBooleanProperty()

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
    listOf(TowerFactory(), InvaderFactory(), BulletFactory(), PlaceholderFactory())
      .forEach(getGameWorld()::addEntityFactory)

    val level = getAssetLoader().loadLevel("experiment.level", GalacticCombatLevelLoader())
    getGameWorld().setLevel(level)
    val levelData = LevelDataVar.get()
    val mainPath = levelData.paths.first()

    showPoints(mainPath)

    enemiesLeft.bind(FXGL.getGameState().intProperty(GameVars.ENEMIES_TO_SPAWN.id).greaterThan(0))

    val spawnInvader = Runnable {
      GameVars.ENEMIES_TO_SPAWN.increment(-1)
      val index = GameVars.ENEMIES_TO_SPAWN.get()
      getGameWorld().spawn(
        INVADER_ID,
          SpawnData(mainPath.first().toPoint()).put(InvaderType.id, InvaderType.values()[index])
      )
    }

    val trickle = Runnable {
      LevelGameVars.GOLD.increment(levelData.trickleGold)
      GameVars.SCORE.increment(levelData.trickleScore)
    }

    getGameTimer().runAtIntervalWhile(spawnInvader, Duration.seconds(2.0), enemiesLeft)
    getGameTimer().runAtInterval(trickle, Duration.seconds(AppConfig.TRICKLE_RATE))

    // Invader Events
    FXGL.getEventBus().apply {
      addEventHandler(
        InvaderEvents.INVADER_REACHED_GOAL,
        EventHandler { event ->
          LevelGameVars.HEALTH.increment(-event.invader.data.damage)
          event.invader.entity.removeFromWorld()
        })

      addEventHandler(
        InvaderEvents.INVADER_KILLED,
        EventHandler { event ->
          LevelGameVars.EXPERIENCE.increment(event.invader.data.xp)
          GameVars.SCORE.increment(event.invader.data.bounty)
          event.invader.entity.removeFromWorld()
        })
    }

  }

  override fun initInput() {
    val input = FXGL.getInput()

//    input.addAction(object : UserAction("Tower") {
//      override fun onAction() {
//        getGameWorld().spawn(
//            PLACEHOLDER_SPAWN_ID,
//            SpawnData(input.mousePositionWorld).put(TowerData.id, TowerFactory.getTowerData(TowerType.CANNON))
//        )
//      }
//    }, KeyCode.K)


//    input.addAction(object : UserAction("Place Tower") {
//      private val worldBounds = Rectangle2D(0.0, 0.0, getAppWidth().toDouble(), getAppHeight() - 100.0 - 40)
//
//      override fun onActionBegin() {
//        if (worldBounds.contains(input.mousePositionWorld)) {
//          getGameWorld().spawn(
//              TOWER_SPAWN_ID,
//            SpawnData(input.mouseXWorld, input.mouseYWorld).put(TowerType.id, TowerType.CANNON)
//          )
//        }
//      }
//    }, MouseButton.PRIMARY)


    //add cheats here by checking:
    //getSettings().getApplicationMode() == ApplicationMode.DEVELOPER
  }

  override fun initUI() {
    // Background
    val scene = getGameScene()
    scene.setBackgroundColor(Color.LIGHTGRAY)

    // initialize upper stats
    val topBar = TopBar(scene)
    topBar.open()
    val sideBar = SideBar(scene)
    sideBar.open()

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
    // possibly not correct and not used!
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

private fun showPoints(waypoints: Path) {
  operator fun Point2D.component1(): Double = x
  operator fun Point2D.component2(): Double = y
  waypoints.forEach { (_x, _y) ->
    val x = _x.toDouble()
    val y = _y.toDouble()
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