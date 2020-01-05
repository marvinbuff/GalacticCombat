package galacticCombat.entities.controller

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.entities.EntityType
import galacticCombat.level.json.LevelData
import galacticCombat.utils.toPoint

@Suppress("unused")
class LevelControllerFactory : EntityFactory {

  @Spawns(SPAWN_ID_LEVEL_CONTROLLER)
  fun spawnLevelController(data: SpawnData): Entity {
    require(data.hasKey(ID_LEVEL_DATA)) { "Spawning a LevelController requires LevelData." } //todo create annotation for this
    val levelData = data.get<LevelData>(ID_LEVEL_DATA)
    val controller = LevelControllerComponent(levelData)

    return entityBuilder().type(EntityType.CONTROLLER)
        .from(data)
        .with(controller)
        .build()
  }

  companion object {
    private const val SPAWN_ID_LEVEL_CONTROLLER = "LevelController"
    private const val ID_LEVEL_DATA = "LevelData"
    private val BUTTON_POSITION = (500 to 400).toPoint()

    private fun createSpawnData(levelData: LevelData): SpawnData {
      return SpawnData(BUTTON_POSITION).put(ID_LEVEL_DATA, levelData)
    }

    fun create(levelData: LevelData) =
        getGameWorld().create(
            SPAWN_ID_LEVEL_CONTROLLER,
            createSpawnData(levelData)
        )

  }
}