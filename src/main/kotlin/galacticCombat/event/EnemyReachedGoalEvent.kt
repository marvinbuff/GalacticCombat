package galacticCombat.event

import com.almasb.fxgl.entity.Entity
import javafx.event.Event
import javafx.event.EventType

class EnemyReachedGoalEvent(val enemy: Entity) : Event(ANY) {

  companion object {
    val ANY: EventType<EnemyReachedGoalEvent> = EventType(Event.ANY, "EnemyReachedGoalEvent")
  }
}