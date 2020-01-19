package galacticCombat.ui

import com.almasb.fxgl.ui.UIController
import galacticCombat.configs.GameVarsInt
import galacticCombat.configs.InfoPanelVar
import galacticCombat.configs.IntGameVar
import galacticCombat.configs.LevelController
import galacticCombat.configs.LevelGameVars
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea

class GameViewController : UIController {

  // Level Property UI
  @FXML lateinit var goldLabel: Label
  @FXML lateinit var healthLabel: Label
  @FXML lateinit var experienceLabel: Label
  @FXML lateinit var scoreLabel: Label

  // Wave Dependant UI
  @FXML lateinit var waveTitle: Label
  @FXML lateinit var timerButton: Button

  // Information UI
  @FXML lateinit var informationArea: TextArea


  override fun init() {
    bindInfoLabels()
    bindWaveLabel()
    bindTimerButton()
    bindInformationPanel()
  }

  private fun bindInformationPanel() {
    informationArea.textProperty().bind(InfoPanelVar.property())
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
          val value = gameVar.property()
          val binding = value.asString("$title: %d")
          label.textProperty().bind(binding)
        }
  }

}