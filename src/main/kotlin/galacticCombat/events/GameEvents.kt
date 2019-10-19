package galacticCombat.events

import javafx.event.Event
import javafx.event.EventType

class GameEvents(eventType: EventType<GameEvents>) : Event(eventType) {

  companion object {
    val LEVEL_FINISHED: EventType<GameEvents> = EventType(ANY, "LevelFinished")
  }
}