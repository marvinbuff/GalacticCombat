package galacticCombat.ui

import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.ui.UIController
import galacticCombat.configs.GameVarsInt
import galacticCombat.configs.IntGameVar
import galacticCombat.configs.LevelController
import galacticCombat.configs.LevelGameVars
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label

class GameViewController : UIController {

  @FXML lateinit var goldLabel: Label

  @FXML lateinit var healthLabel: Label

  @FXML lateinit var experienceLabel: Label

  @FXML lateinit var scoreLabel: Label

  @FXML lateinit var waveTitle: Label

  @FXML lateinit var timerButton: Button

//    @FXML
//    fun buttonClicked(event: ActionEvent) = println("Hi: ${label.text}")

  override fun init() {
    bindInfoLabels()
    bindWaveLabel()
    bindTimerButton()
  }

  private fun bindTimerButton() {
    val timerComponent = LevelController.get().timerComponent
    timerButton.textProperty().bind(timerComponent.getTimePropertyConverted().asString("Next Wave: %.0f"))
    timerButton.onAction = EventHandler<ActionEvent> { timerComponent.skipToNextWave() }
  }

  private fun bindWaveLabel() {
    waveTitle.textProperty().bind(LevelController.get().timerComponent.getWaveProperty().asString("Wave: %d"))
  }

  private fun bindInfoLabels() {
    listOf<Triple<IntGameVar, Label, String>>(
        Triple(LevelGameVars.GOLD, goldLabel, "Gold"),
        Triple(LevelGameVars.HEALTH, healthLabel, "Health"),
        Triple(LevelGameVars.EXPERIENCE, experienceLabel, "Exp"),
        Triple(GameVarsInt.SCORE, scoreLabel, "Score")
    )
        .forEach { (gameVar, label, title) ->
          val value = getGameState().properties.intProperty(gameVar.id)
          val binding = value.asString("$title: %d")
          label.textProperty().bind(binding)
        }
  }

}