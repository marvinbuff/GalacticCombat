package galacticCombat.entities.spawnSlider

import com.almasb.fxgl.dsl.entityBuilder
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import galacticCombat.entities.EntityType
import galacticCombat.entities.SLIDER_ITEM_SPAWN_ID
import galacticCombat.entities.SLIDER_PIN_SPAWN_ITEM
import galacticCombat.entities.invader.InvaderComponent
import galacticCombat.entities.invader.InvaderData
import galacticCombat.entities.setTypeAdvanced
import galacticCombat.level.json.InvaderSpawnArgs
import galacticCombat.ui.SpawnSliderController
import galacticCombat.utils.position
import galacticCombat.utils.toPoint
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

@Suppress("unused")
class SpawnSliderFactory : EntityFactory {

  @Spawns(SLIDER_ITEM_SPAWN_ID)
  fun spawnPlaceholder(data: SpawnData): Entity {
    require(data.hasKey(ID_SPAWN_ARGS)) //todo create require annotation for this
    require(data.hasKey(ID_WAVE_INDEX))

    val spawnArgs: InvaderSpawnArgs = data.get(ID_SPAWN_ARGS)
    val invaderData = InvaderData.createFromArgs(spawnArgs.args)
    val waveIndex: Int = data.get(ID_WAVE_INDEX)
    val spawnItemComponent = SpawnItemComponent(waveIndex, spawnArgs) //todo only allow dragging when in editor-mode

    return entityBuilder().setTypeAdvanced(EntityType.SLIDER_ITEM)
      .atAnchored(InvaderComponent.center, data.position)
      .with(spawnItemComponent)
      .view(invaderData.texture.toBasicTexture())
      .build()
  }

  @Spawns(SLIDER_PIN_SPAWN_ITEM)
  fun spawnSliderPin(data: SpawnData): Entity {
    val gap = 10.0
    val anchor = ((SpawnSliderController.SLIDER_HEIGHT - gap) / 2).toPoint()

    val edge = SpawnSliderController.SLIDER_HEIGHT - gap
    val rect = Rectangle(edge, edge, Color.web("000000", 0.0))
    rect.stroke = Color.BLACK
    rect.strokeWidth = 2.0
    rect.arcHeight = 10.0
    rect.arcWidth = 10.0

    return entityBuilder().setTypeAdvanced(EntityType.SLIDER_PIN)
      .atAnchored(anchor, data.position)
      .viewWithBBox(rect)
      .with(SpawnPinComponent())
      .build()
  }

  companion object {
    private const val ID_WAVE_INDEX = "WaveIndex"
    private const val ID_SPAWN_ARGS = "InvaderSpawnArgs"

    fun spawnItem(waveIndex: Int, spawnArgs: InvaderSpawnArgs, position: Point2D) {
      val data = SpawnData(position)
        .put(ID_SPAWN_ARGS, spawnArgs)
        .put(ID_WAVE_INDEX, waveIndex)

      getGameWorld().spawn(SLIDER_ITEM_SPAWN_ID, data)
    }

    fun spawnPin(position: Point2D) {
      val data = SpawnData(position)

      getGameWorld().spawn(SLIDER_PIN_SPAWN_ITEM, data)
    }
  }
}