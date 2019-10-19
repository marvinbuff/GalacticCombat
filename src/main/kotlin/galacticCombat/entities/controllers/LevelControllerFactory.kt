package galacticCombat.entities.controllers

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.entities.EntityType
import galacticCombat.level.json.LevelData
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

@Suppress("unused")
class LevelControllerFactory : EntityFactory {

  @Spawns(SPAWN_ID_LEVEL_CONTROLLER)
  fun spawnLevelController(data: SpawnData): Entity {
    val levelData = data.get<LevelData>(ID_LEVEL_DATA)

    return entityBuilder().type(EntityType.CONTROLLER)
        .from(data)
        .view(Rectangle(5.0, 5.0, Color.RED))
        .with(LevelControllerComponent(levelData))
        .build()
  }

  companion object {
    private const val SPAWN_ID_LEVEL_CONTROLLER = "Level Controller"
    private const val ID_LEVEL_DATA = "LevelData"

    private fun createSpawnData(levelData: LevelData): SpawnData {
      return SpawnData(0.0, 0.0).put(ID_LEVEL_DATA, levelData)
    }

    fun create(levelData: LevelData) =
        getGameWorld().create(
            SPAWN_ID_LEVEL_CONTROLLER,
            createSpawnData(levelData)
        )

  }
}