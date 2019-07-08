package samples

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.application.Application.launch
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.stage.Stage
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random


fun main(args: Array<String>) {
  launch(JavaFXExample::class.java)
}

class JavaFXExample : Application() {
  private val input = ArrayList<String>()
  private val folder = "images/"
  private val earth = Image("${folder}earth.png")
  private val sun = Image("${folder}sun.png")
  private val space = Image("${folder}space.png")
  private val bullseye = Image("${folder}bullseye.png")
  var score = 0

  override fun start(stage: Stage) {
    stage.title = "Space War"

    val root = Group()
    val scene = Scene(root)
    stage.scene = scene

    val canvasSize = 512.0
    val canvas = Canvas(canvasSize, canvasSize)
    root.children.add(canvas)

    val target = Circle(100.0, 100.0, 32.0)

// Event Handlers
    scene.onKeyPressed = EventHandler {
      val code = it.code.toString()
      if (!input.contains(code)) input.add(code)
    }

    scene.onKeyReleased = EventHandler {
      val code = it.code.toString()
      if (input.contains(code)) input.remove(code)
    }

    scene.onMouseClicked = EventHandler { event ->
      if (target.contains(event.x, event.y)) {
        score ++
        target.centerX = 50.0 + Random.nextInt(0,400)
        target.centerY = 50.0 + Random.nextInt(0,400)
      } else {
        score = 0
      }
    }

    val gc = canvas.graphicsContext2D
    gc.font = Font.font("Helvetica", FontWeight.BOLD, 24.0)
    gc.fill = Color.BLACK
    gc.stroke = Color.BLUE

    val startNanoTime = System.nanoTime()
    val ufo = Actor(AnimatedImage((0..5).map { "${folder}ufo_$it.png" }.map { Image(it) }, 0.100), 100.0, 25.0)

    object : AnimationTimer() {
      override fun handle(currentNanoTime: Long) {
        val t = (currentNanoTime - startNanoTime) / 1000000000.0

        val x = 232 + 128 * cos(t)
        val y = 232 + 128 * sin(t)
        controlUfo(input, ufo)
        updateActors(listOf(ufo))

        // background image clears canvas
        gc.drawImage(space, 0.0, 0.0)

        // static or automatically animated
        gc.drawImage(earth, x, y)
        gc.drawImage(sun, 196.0, 196.0)

        // keyboard controlled
        gc.drawImage( ufo.image.getFrame(t), ufo.x, ufo.y )

        // mouse controlled
        gc.drawImage(bullseye, target.centerX - target.radius, target.centerY - target.radius)
        val pointsText = "Points: $score"

        // label
        gc.fillText(pointsText, 360.0, 36.0)
        gc.strokeText(pointsText, 360.0, 36.0)
        gc.fillText("Points: $score", 100.0, 100.0)
      }
    }.start()

    stage.show()
  }

  fun controlUfo(input: List<String>, ufo: Actor) {
    if (input.contains("TURN_LEFT")) ufo.rotate(-Actor.MAX_ROTATION)
    if (input.contains("TURN_RIGHT")) ufo.rotate(Actor.MAX_ROTATION)
    if (input.contains("UP")) ufo.acceleration = Actor.MAX_ACCELERATION
    if (input.contains("DOWN")) ufo.acceleration = -Actor.MAX_ACCELERATION
  }

  fun updateActors(actors: List<Actor>) {
    actors.forEach { it.update(time = 1.0) }
  }
}

class Actor (
    val image: AnimatedImage,
    var x: Double = 0.0,
    var y: Double = 0.0,
    private var rotation: Double = 0.0 //0 - 2 PI
) {
  private var velocity: Double = 0.0
  var acceleration: Double = 0.0

  fun update(time: Double) {
    velocity = min(max(acceleration + velocity, MIN_VELOCITY), MAX_VELOCITY)
    x += cos(rotation) * velocity * time
    y += sin(rotation) * velocity * time
  }

  fun rotate(angle: Double) {
    rotation += (angle * PI) / 180
  }

  companion object {
    const val MAX_ACCELERATION = 0.2
    const val MAX_VELOCITY = 6.0
    const val MIN_VELOCITY = 0.1
    const val MAX_ROTATION = 3.0
  }
}

class AnimatedImage (
    private val frames: List<Image>,
    private val duration: Double
) {

  fun getFrame(time: Double): Image {
    val index = (time % (frames.size * duration) / duration).toInt()
    return frames[index]
  }
}