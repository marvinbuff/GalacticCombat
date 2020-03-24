package galacticCombat.entities.spawnSlider

import com.almasb.fxgl.dsl.components.DraggableComponent
import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.entities.EntityType
import galacticCombat.entities.INVADER_SPAWN_ID
import galacticCombat.entities.SLIDER_ITEM_SPAWN_ID
import galacticCombat.entities.invader.InvaderComponent
import galacticCombat.entities.invader.InvaderData
import galacticCombat.entities.invader.InvaderFactory.Companion.ID_INVADER_ARGS
import galacticCombat.entities.setTypeAdvanced
import galacticCombat.level.json.InvaderSpawnArgs
import galacticCombat.utils.position
import javafx.geometry.Point2D

@Suppress("unused")
class SpawnSliderFactory : EntityFactory {

  @Spawns(SLIDER_ITEM_SPAWN_ID)
  fun spawnPlaceholder(data: SpawnData): Entity {
    require(data.hasKey(ID_INVADER_ARGS)) //todo create require annotation for this

    val invaderData = InvaderData.createFromArgs(data.get(ID_INVADER_ARGS))
    val spawnItemComponent = SpawnItemComponent(invaderData) //todo only allow dragging when in editor-mode

    return entityBuilder().setTypeAdvanced(EntityType.SLIDER_ITEM)
      .atAnchored(InvaderComponent.center, data.position)
      .with(spawnItemComponent)
      .view(invaderData.texture.toBasicTexture())
      .build()
  }

  companion object {

    fun spawn(spawnArgs: InvaderSpawnArgs, position: Point2D) {
      val data = SpawnData(position).put(ID_INVADER_ARGS, spawnArgs.args)

      getGameWorld().spawn(SLIDER_ITEM_SPAWN_ID, data)
    }
  }
}