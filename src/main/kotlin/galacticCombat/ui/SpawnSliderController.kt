package galacticCombat.ui

import com.almasb.fxgl.dsl.getGameTimer
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.onEvent
import galacticCombat.configs.LevelDataVar
import galacticCombat.entities.EntityType
import galacticCombat.entities.spawnSlider.SpawnSliderFactory
import galacticCombat.events.WaveEvent
import galacticCombat.ui.elements.SpawnSlider
import galacticCombat.utils.removeEntitiesByType
import javafx.event.EventType
import javafx.geometry.Point2D
import javafx.util.Duration

class SpawnSliderController(private val slider: SpawnSlider) {
  private val updater = Runnable { slider.update() }
  private val levelData = LevelDataVar.get()

  init {
    onEvent(WaveEvent.WAVE_STARTED) { event -> updateSliderItems(event) }
    getGameTimer().runAtInterval(updater, Duration.seconds(1.0))
    updateSliderItems(WaveEvent(WaveEvent.WAVE_STARTED, 0))
  }

  private fun updateSliderItems(event: WaveEvent) {
    getGameWorld().removeEntitiesByType(EntityType.SLIDER_ITEM)
    levelData.waves[event.waveIndex].invaders.forEach{args ->
      val position = Point2D(SLIDER_X + args.time*SLIDER_STEP, SLIDER_Y)
      SpawnSliderFactory.spawn(args, position)
    }
  }

  companion object {
    // Hard-Coded coordinates, should be taken from slider directly.
    const val SLIDER_X = 109.0
    const val SLIDER_Y = 455.0
    const val SLIDER_WIDTH = 480.0
    const val SLIDER_STEP = SLIDER_WIDTH/60.0
  }
}