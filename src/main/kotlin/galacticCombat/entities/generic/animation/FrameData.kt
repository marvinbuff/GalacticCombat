package galacticCombat.entities.generic.animation

import com.almasb.fxgl.dsl.FXGL
import javafx.scene.image.Image

data class FrameData(
    private val frames: List<Image>,
    val frameDuration: Double = 0.1
) {

  fun getNextFrameIndex(index: Int) = (index + 1) % frames.size
  fun getFrame(index: Int) = frames[index]

  companion object {
    fun create(frames: List<String>) = FrameData(frames.map { FXGL.image(it) })
  }
}