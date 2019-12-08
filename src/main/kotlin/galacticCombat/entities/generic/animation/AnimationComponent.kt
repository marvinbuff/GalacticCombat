package galacticCombat.entities.generic.animation

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.texture.Texture

/**
 * A simple animation component which loops over given frames.
 */
class AnimationComponent(private val frames: FrameData) : Component() {

  override fun onAdded() {
    entity.viewComponent.addChild(LoopedTexture(frames))
  }
}

private class LoopedTexture(private val frameProvider: FrameData) : Texture() {
  var time = 0.0

  var nextFrameAt = 0.0
  var nextFrameIndex = 0

  override fun onUpdate(tpf: Double) {
    time += tpf

    if (time > nextFrameAt) updateFrame()
  }

  private fun updateFrame() {
    image = frameProvider.getFrame(nextFrameIndex)
    nextFrameIndex = frameProvider.getNextFrameIndex(nextFrameIndex)
    nextFrameAt = time + frameProvider.frameDuration
  }
}