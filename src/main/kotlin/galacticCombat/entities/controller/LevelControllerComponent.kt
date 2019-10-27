package galacticCombat.entities.controller

import com.almasb.fxgl.entity.component.Component
import galacticCombat.level.json.LevelData

class LevelControllerComponent(private val levelData: LevelData) : Component() {

  override fun onAdded() {
    val timerComponent = LevelTimerComponent(levelData)
    val viewComponent = NumberDisplayComponent(timerComponent)

    entity.addComponent(timerComponent)
    entity.addComponent(viewComponent)
  }
}

