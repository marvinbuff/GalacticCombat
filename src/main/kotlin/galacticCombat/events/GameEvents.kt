package galacticCombat.events

import com.almasb.fxgl.dsl.getEventBus
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType

class GameEvents(eventType: EventType<GameEvents>) : Event(eventType) {

  companion object {
    val LEVEL_WON: EventType<GameEvents> = EventType(ANY, "LevelFinished")
    val LEVEL_LOST: EventType<GameEvents> = EventType(ANY, "LevelLost")
  }
}

fun <T : Event> EventType<T>.handle(handler: (T) -> Unit) {
  getEventBus().addEventHandler(
      this,
      EventHandler {
        handler(it)
      }
  )
}