package javaFxGame

import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.stage.Stage
import javafx.animation.AnimationTimer
import javafx.event.EventHandler
import javafx.scene.input.KeyEvent
import javafx.scene.shape.Circle
import kotlin.math.cos
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

    val target = Circle(100.0,100.0,64.0)

// Event Handlers
    scene.onKeyPressed = EventHandler {
      val code = it.code.toString()
      if (!input.contains(code)) input.add(code)
    }

    scene.onKeyReleased = EventHandler {
      val code = it.code.toString()
      if (input.contains(code)) input.remove(code)
    }

    scene.onMouseClicked = EventHandler {
      if (target.contains(it.x, it.y)){
        score ++
        target.centerX = 50.0 + Random.nextInt(0,400)
        target.centerY = 50.0 + Random.nextInt(0,400)
      } else {
        score = 0
      }
    }

    val gc = canvas.graphicsContext2D

    val startNanoTime = System.nanoTime()

    val ufo = Actor(AnimatedImage((0..5).map { "${folder}ufo_$it.png" }.map { Image(it) }, 0.100), 100.0, 25.0)

    object : AnimationTimer() {
      override fun handle(currentNanoTime: Long) {
        val t = (currentNanoTime - startNanoTime) / 1000000000.0

        val x = 232 + 128 * cos(t)
        val y = 232 + 128 * sin(t)
        moveActors(input, listOf(ufo))

        // background image clears canvas
        gc.drawImage(space, 0.0, 0.0)
        gc.drawImage(earth, x, y)
        gc.drawImage(sun, 196.0, 196.0)

        gc.drawImage( ufo.image.getFrame(t), ufo.x, ufo.y )

        gc.drawImage( bullseye, target.centerY - target.radius, target.centerY - target.radius)
      }
    }.start()

    stage.show()
  }

  fun moveActors(input: List<String>, actors: List<Actor>) {
    val speed = 1.0
    if (input.contains("LEFT")) actors.forEach { it.move(-speed) }
    if (input.contains("RIGHT")) actors.forEach { it.move(speed) }
  }
}

class Actor (
    val image: AnimatedImage,
    var x: Double = 0.0,
    var y: Double = 0.0
){

  fun move(speed : Double) {
    x += speed
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