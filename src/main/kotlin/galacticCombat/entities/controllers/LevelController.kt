package galacticCombat.entities.controllers

import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import galacticCombat.entities.INVADER_SPAWN_ID
import galacticCombat.entities.invader.InvaderFactory
import galacticCombat.level.json.InvaderArgs
import galacticCombat.level.json.LevelData
import galacticCombat.utils.toPoint

//todo add component which lets it show as countdown
//todo add super controller component and move wave logic to another one
class LevelControllerComponent(private val levelData: LevelData) : Component() {
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
        spawnInvader(invader); state.updateInvader()
      } else return
    }
  }

  private fun spawnInvader(invader: InvaderArgs) {
    getGameWorld().spawn( //todo improve invader factory
        INVADER_SPAWN_ID, SpawnData(levelData.paths.first().first().toPoint())
        .put(InvaderFactory.invaderArgsId, invader)
    )
  }
}

/**
 * Holds the state in which the [LevelControllerComponent] is.
 */
private data class LevelState(
    private val data: LevelData,
    var isWaveThrough: Boolean = false
) {
  // State Variable
  private var time: Double = 0.0

  // Indices
  private var currentWaveIndex: Int = 0
  private var currentInvaderIndex: Int = 0

  // Flags
  var isFinished: Boolean = false


  fun updateTime(tpf: Double) {
    time += tpf
  }

  fun resetTime() {
    time = 0.0
  }

  fun getTime() = time
  fun updateInvader() {
    currentInvaderIndex++
  }

  fun isWaveOver(): Boolean = time >= data.settings.timePerWave
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

