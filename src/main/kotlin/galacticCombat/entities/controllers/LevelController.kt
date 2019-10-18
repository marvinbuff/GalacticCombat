package galacticCombat.entities.controllers

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.component.Component
import galacticCombat.configs.LevelDataVar
import galacticCombat.entities.EntityType
import galacticCombat.entities.INVADER_SPAWN_ID
import galacticCombat.entities.LEVEL_CONTROLLER_ID
import galacticCombat.entities.invader.InvaderFactory
import galacticCombat.level.json.LevelData
import galacticCombat.utils.toPoint
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

//todo add component which lets it show as countdown
class LevelControllerComponent(private val levelData: LevelData) : Component() {
  var time = 0.0
  var currentWaveIndex = 0
  var currentInvaderIndex = 0

  override fun onUpdate(tpf: Double) {
    time += tpf
    val wave = levelData.waves[currentWaveIndex] //todo handle multiple waves
    val next = wave.getOrNull(currentInvaderIndex) ?: return //todo handle correctly
    if (next.first < time) {
      currentInvaderIndex++
      getGameWorld().spawn(
          INVADER_SPAWN_ID, SpawnData(levelData.paths.first().first().toPoint())
          .put(InvaderFactory.invaderArgsId, next.second)
      )
    }
  }
}

@Suppress("unused")
class LevelControllerFactory : EntityFactory {

  //todo add documentation
  @Spawns(LEVEL_CONTROLLER_ID)
  fun spawnLevelController(data: SpawnData): Entity {
    val levelData = LevelDataVar.get()

    return entityBuilder().type(EntityType.CONTROLLER)
        .from(data)
        .view(Rectangle(5.0, 5.0, Color.RED))
        .with(LevelControllerComponent(levelData))
        .build()
  }
}