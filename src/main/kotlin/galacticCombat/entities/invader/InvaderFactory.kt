package galacticCombat.entities.invader

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.configs.LevelDataVar
import galacticCombat.entities.EntityType
import galacticCombat.entities.generic.InfoComponent
import galacticCombat.level.json.InvaderArgs
import galacticCombat.utils.position
import galacticCombat.utils.toPoint

@Suppress("unused")
class InvaderFactory : EntityFactory {

  @Spawns(SPAWN_ID_INVADER)
  fun spawnEnemy(data: SpawnData): Entity {
    require(data.hasKey(ID_INVADER_ARGS))

    val invaderData = InvaderData.createFromArgs(data.get(ID_INVADER_ARGS))
    val invader = InvaderComponent(invaderData)
    val healthBar = HealthComponent(invader)
    val infoHandler = InfoComponent(invader)

    return entityBuilder().type(EntityType.INVADER)
        .atAnchored(InvaderComponent.center, data.position)
        .view(invaderData.texture.toAnimatedTexture())
        .with(invader)
        .with(healthBar)
        .with(infoHandler)
        .build()
  }


  companion object {
    const val ID_LEVEL = "level"
    const val ID_INVADER_ARGS = "InvaderArgs"
    const val SPAWN_ID_INVADER = "Invader"

    fun spawn(args: InvaderArgs) {
      val position = LevelDataVar.get().getPathById(args.pathId).wayPoints.first()
      val data = SpawnData(position.toPoint()).put(ID_INVADER_ARGS, args)

      getGameWorld().spawn(SPAWN_ID_INVADER, data)
    }
  }
}