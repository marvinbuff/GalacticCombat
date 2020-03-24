package galacticCombat.ui

import com.almasb.fxgl.ui.UIController
import galacticCombat.configs.GameVarsInt
import galacticCombat.configs.InfoPanelVar
import galacticCombat.configs.IntGameVar
import galacticCombat.configs.LevelController
import galacticCombat.configs.LevelGameVars
import galacticCombat.ui.elements.SpawnSlider
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.geometry.Point2D
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.image.ImageView

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
  @FXML lateinit var infoPanelTitle: Label
  @FXML lateinit var infoPanelText: TextArea
  @FXML lateinit var infoPanelImage: ImageView

  // Spawn Slider
  @FXML lateinit var spawnSlider: SpawnSlider


  override fun init() {

    bindInfoLabels()
    bindWaveLabel()
    bindTimerButton()
    bindInformationPanel()
    initializeSpawnSlider()
  }

  private fun initializeSpawnSlider() {
    SpawnSliderController(spawnSlider)
  }

  private fun bindInformationPanel() {
    val provider = InfoPanelVar.get()
    infoPanelText.textProperty().bind(provider.textProperty)
    infoPanelTitle.textProperty().bind(provider.titleProperty)
    infoPanelImage.imageProperty().bind(provider.imageProperty)
  }

  private fun bindTimerButton() {
    val timerComponent = LevelController.get().timerComponent
    timerButton.textProperty().bind(timerComponent.getTimePropertyConverted().asString("Next Wave: %.0f"))
    timerButton.onAction = EventHandler { timerComponent.skipToNextWave() }
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