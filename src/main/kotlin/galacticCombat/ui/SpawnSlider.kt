package galacticCombat.ui

import com.almasb.fxgl.dsl.getGameTimer
import com.almasb.fxgl.dsl.onEvent
import galacticCombat.events.WaveEvents
import javafx.scene.canvas.Canvas
import javafx.util.Duration

class SpawnSlider : Canvas() {
  private val updater = Runnable { updateTimeIndicator() }

  init {
    onEvent(WaveEvents.WAVE_STARTED) { event -> updateToNewWave(event) }
    getGameTimer().runAtInterval(updater, Duration.seconds(1.0))
  }

  private fun updateToNewWave(event: WaveEvents) {
    //todo draw slider and all invaders, then put timer on init
  }

  private fun updateTimeIndicator() {
    //todo move time indicator an inch
  }
}