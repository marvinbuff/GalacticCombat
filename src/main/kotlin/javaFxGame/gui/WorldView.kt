package javaFxGame.gui

import javaFxGame.game.Actor
import javaFxGame.game.World
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.stage.Stage

class WorldView : Application() {
  lateinit var scene: Scene
  lateinit var gc: GraphicsContext
  private val keyInput = mutableSetOf<String>()

  override fun init() {
    val world = World()
    world.spawnSoloPlayer()
    world.spawnMeteor()
  }

  override fun start(stage: Stage) {
    setup(stage)

    addListeners()

    startTimer()

    stage.show()
  }

  private fun setup(stage: Stage) {
    stage.title = "Space War"

    val root = Group()
    scene = Scene(root)
    stage.scene = scene

    val canvas = Canvas(World.WORLD_WIDTH, World.WORLD_HEIGHT)
    root.children.add(canvas)

    gc = canvas.graphicsContext2D
    gc.font = Font.font("Helvetica", FontWeight.BOLD, 24.0)
    gc.fill = Color.BLACK
    gc.stroke = Color.BLUE
  }

  private fun addListeners() {
    scene.onKeyPressed = EventHandler {
      val code = it.code.toString()
      keyInput.add(code)
    }

    scene.onKeyReleased = EventHandler {
      val code = it.code.toString()
      keyInput.remove(code)
    }
  }

  private fun startTimer() {
    val startNanoTime = System.nanoTime()
    object : AnimationTimer() {
      override fun handle(currentNanoTime: Long) {
        val t = (currentNanoTime - startNanoTime) / 1000000000.0

        gc.drawImage(Image("images/space.png"), 0.0, 0.0)
        gc.fillText("Time $t", 36.0, 36.0)
        gc.strokeText("Time $t", 36.0, 36.0)

        World.world.let { world ->
          world.players.forEach { it.input(keyInput) }
          world.actors.forEach { actor ->
            actor.act(time = 1.0)
            renderActor(actor, t)
          }
        }

      }
    }.start()
  }

  fun renderActor(actor: Actor, time: Double) {
    val image = actor.image.getFrame(time)
    gc.save()
    gc.translate(actor.x, actor.y)
    gc.rotate(actor.rotationDegree)
    gc.drawImage(image, -(image.width / 2), -(image.height / 2))
    gc.restore()
  }
}