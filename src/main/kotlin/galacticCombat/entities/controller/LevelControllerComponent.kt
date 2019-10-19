package galacticCombat.entities.controller

import com.almasb.fxgl.entity.component.Component
import galacticCombat.entities.invader.InvaderFactory
import galacticCombat.level.json.InvaderArgs
import galacticCombat.level.json.LevelData
import javafx.beans.property.SimpleDoubleProperty

//todo add super controller component and move wave logic to another one
//todo add clickable component
class LevelControllerComponent(levelData: LevelData) : Component() {
  private val state: LevelState = LevelState.create(levelData)

  override fun onUpdate(tpf: Double) {
    // Handle Wave
    if (state.isFinished) return
    state.updateTime(tpf)
    if (state.isWaveOver()) {
      if (state.isWaveLast()) {
        state.isFinished = true; return
      } else state.updateWave()
    }
    // Spawn Invader
    while (!state.isWaveThrough) {
      val invaderPair = state.getInvader()
      if (invaderPair == null) {
        state.isWaveThrough = true; return
      }
      val (spawnTime, invader) = invaderPair
      if (spawnTime <= state.getTime()) {
        InvaderFactory.spawn(invader); state.updateInvader()
      } else return
    }
  }

  fun getTimeProperty() = state.getTimeProperty()
}

/**
 * Holds the state in which the [LevelControllerComponent] is.
 */
private data class LevelState(
    private val data: LevelData,
    var isWaveThrough: Boolean = false
) {
  // State Variable
  private var time = SimpleDoubleProperty(0.0)

  // Indices
  private var currentWaveIndex: Int = 0
  private var currentInvaderIndex: Int = 0

  // Flags
  var isFinished: Boolean = false

  // Utility functions
  fun updateTime(tpf: Double) {
    time.value += tpf
  }

  fun resetTime() {
    time.value = 0.0
  }

  fun getTime() = time.value
  fun getTimeProperty() = time
  fun updateInvader() {
    currentInvaderIndex++
  }

  fun isWaveOver(): Boolean = time.value >= data.settings.timePerWave
  fun isWaveLast(): Boolean = currentWaveIndex + 1 >= data.waves.size
  fun updateWave() {
    currentWaveIndex++; resetTime()
  }
  fun getInvader(): Pair<Double, InvaderArgs>? = data.waves[currentWaveIndex].getOrNull(currentInvaderIndex)

  companion object {
    fun create(data: LevelData): LevelState {
      val waveThrough = data.waves.first().isEmpty()
      return LevelState(data, waveThrough)
    }
  }
}

