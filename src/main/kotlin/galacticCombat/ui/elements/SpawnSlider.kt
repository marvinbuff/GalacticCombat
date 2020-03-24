package galacticCombat.ui.elements

import javafx.beans.NamedArg
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class SpawnSlider(@NamedArg("width") width: Double, @NamedArg("height") height: Double) : Canvas(width, height) {

  init {
    paint()
  }

  private fun paint() {
    val gc: GraphicsContext = graphicsContext2D
    gc.fill = Color.BLACK
    gc.fillRoundRect(0.0, 0.0, width, height, height, height)
    gc.fill = Color.GREEN
    val offset = 2.0
    gc.fillRoundRect(0.0 + offset, 0.0 + offset, width - 2 * offset, height - 2 * offset, height - 2 * offset, height - 2 * offset)
  }

  fun update() {
    //todo move time indicator an inch
  }
}