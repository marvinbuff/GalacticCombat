package galacticCombat.entities.controller

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import galacticCombat.level.json.LevelData

// Todo: what is the purpose of this class? I can't remember.. I think it should handle some interaction?
@Required(LevelTimerComponent::class)
class LevelControllerComponent(private val levelData: LevelData) : Component() {
  val timerComponent: LevelTimerComponent by lazy { entity.getComponent(LevelTimerComponent::class.java) }
}

