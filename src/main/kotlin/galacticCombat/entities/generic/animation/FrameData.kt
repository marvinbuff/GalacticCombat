package galacticCombat.entities.generic.animation

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.fxgl.texture.AnimationChannel
import javafx.scene.image.Image
import javafx.util.Duration

data class FrameData(
    private val frames: List<Image>,
    private val frameDuration: Double = 0.1
) {

  fun toAnimatedTexture() = AnimatedTexture(
      AnimationChannel(this.frames, Duration(1000.0 * this.frameDuration * this.frames.size))
  ).loop()

  companion object {
    fun create(frames: List<String>) = FrameData(frames.map { FXGL.image(it) })
  }
}