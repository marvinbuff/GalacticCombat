package galacticCombat.entities.controller

import com.almasb.fxgl.entity.component.Component
import galacticCombat.level.json.LevelData

class LevelControllerComponent(private val levelData: LevelData) : Component() {
  lateinit var timerComponent: LevelTimerComponent

  override fun onAdded() {
    timerComponent = LevelTimerComponent(levelData)

    entity.addComponent(timerComponent)
  }
}

