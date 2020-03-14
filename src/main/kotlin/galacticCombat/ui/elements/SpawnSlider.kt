package galacticCombat.ui.elements

import com.almasb.fxgl.dsl.getGameTimer
import com.almasb.fxgl.dsl.onEvent
import galacticCombat.events.WaveEvents
import javafx.beans.NamedArg
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.util.Duration

class SpawnSlider(@NamedArg("width") width: Double, @NamedArg("height") height: Double) : Canvas(width, height) {
  private val updater = Runnable { updateTimeIndicator() }

  init {
    onEvent(WaveEvents.WAVE_STARTED) { event -> updateToNewWave(event) }
    getGameTimer().runAtInterval(updater, Duration.seconds(1.0))
    updateToNewWave(null)
  }

  private fun updateToNewWave(event: WaveEvents?) {
    println("executed $width, $height")
    //todo draw slider and all invaders, then put timer on init
    val gc: GraphicsContext = graphicsContext2D

    gc.fill = Color.BLACK
    gc.fillRoundRect(0.0, 0.0, width, height, height, height)
    gc.fill = Color.GREEN
    val offset = 2.0
    gc.fillRoundRect(0.0 + offset, 0.0 + offset, width - 2 * offset, height - 2 * offset, height - 2 * offset, height - 2 * offset)
  }

  private fun updateTimeIndicator() {
    //todo move time indicator an inch
  }
}