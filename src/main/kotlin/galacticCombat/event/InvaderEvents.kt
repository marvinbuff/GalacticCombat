package galacticCombat.event

import com.almasb.fxgl.entity.Entity
import javafx.event.Event
import javafx.event.EventType

class InvaderEvents(val invader: Entity, val damage: Int) : Event(ANY) {

  companion object {
    val ANY: EventType<InvaderEvents> = EventType(Event.ANY, "EnemyReachedGoalEvent")
  }
}