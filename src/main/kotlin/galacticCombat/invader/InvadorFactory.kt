package galacticCombat.invader

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.entity.components.CollidableComponent
import galacticCombat.AssetsConfig
import galacticCombat.EntityType
import galacticCombat.INVADER_ID

@Suppress("unused")
class InvadorFactory : EntityFactory {


  @Spawns(INVADER_ID)
  fun spawnEnemy(data: SpawnData): Entity {
    val invader = InvaderComponent(getInvaderData(InvaderType.COMMON))
    val healthBar = HealthComponent(invader)

    return entityBuilder().type(EntityType.INVADER)
      .from(data)
      .viewWithBBox(AssetsConfig.getEnemy("1.1.gif"))
      .with(CollidableComponent(true))
      .with(invader)
      .with(healthBar)
      .build()
  }

  private fun getInvaderData(type: InvaderType): InvaderData {
    //TODO make dynamic
    return when (type) {
      InvaderType.COMMON -> InvaderData(100.0, 60.0, 20.0)
    }
  }


}

enum class InvaderType(val title: String) {
  COMMON("Cannon Tower");

  companion object {
    const val id = "InvaderType"
  }
}