package javaFxGame.game

import javafx.scene.image.Image
import javafx.scene.shape.Circle
import javafx.scene.shape.Shape
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

abstract class Actor(
    val image: AnimatedImage,
    var x: Double = 0.0,
    var y: Double = 0.0,
    var rotation: Double = 0.0,
    val shape: Shape = Circle(x, y, image.getFrame(0.0).width / 2.0)
) {
  private var velocity: Double = 0.0
  open val worldOffset = 10.0
  val rotationDegree: Double
    get() = rotation * (180 / PI)
  var markedForRemoval = false
    private set

  /**
   * Sets [markedForRemoval] to true and returns whether this actually changed the variable from false.
   */
  fun markForRemoval(): Boolean {
    val result = !markedForRemoval
    markedForRemoval = true
    return result
  }

  fun rotate(angle: Double) {
    rotation += (angle * PI) / 180
  }

  fun accelerate(amount: Double) {
    velocity = min(max(amount + velocity, MIN_VELOCITY), MAX_VELOCITY)
  }

  abstract fun intersectWithBorder(border: Direction)

  open fun act(time: Double) {
    move(time)
  }

  open fun updateShape() {
    shape as Circle
    shape.centerX = x
    shape.centerY = y
  }

  open fun whenRemoved() {}

  internal fun intersectWith(other: Actor): Boolean {
    //alternative: (shape.boundsInParent.intersects(actor.shape.boundsInParent))
    return Shape.intersect(shape, other.shape).boundsInLocal.width != -1.0
  }

  private fun move(time: Double) {
    x += cos(rotation) * velocity * time
    y += sin(rotation) * velocity * time
    var direction: Direction? = null
    if (x < -worldOffset) direction = Direction.WEST
    if (x > worldOffset + World.WORLD_WIDTH) direction = Direction.EAST
    if (y < -worldOffset) direction = Direction.NORTH
    if (y > worldOffset + World.WORLD_HEIGHT) direction = Direction.SOUTH
    if (direction != null) intersectWithBorder(direction)
    updateShape()
  }

  companion object {
    const val MAX_VELOCITY = 6.0
    const val MIN_VELOCITY = 0.1
  }
}

class AnimatedImage(
    private val frames: List<Image>,
    private val duration: Double
) {

  fun getFrame(time: Double): Image {
    val index = (time % (frames.size * duration) / duration).toInt()
    return frames[index]
  }
}

enum class Direction {
  NORTH,
  EAST,
  SOUTH,
  WEST
}