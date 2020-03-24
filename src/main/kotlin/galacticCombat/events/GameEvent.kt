package galacticCombat.events

import javafx.event.Event
import javafx.event.EventType

class GameEvent(eventType: EventType<GameEvent>) : Event(eventType) {

  companion object {
    val LEVEL_WON: EventType<GameEvent> = EventType(ANY, "LevelFinished")
    val LEVEL_LOST: EventType<GameEvent> = EventType(ANY, "LevelLost")
  }
}

class WaveEvent(eventType: EventType<WaveEvent>, val waveIndex: Int) : Event(eventType) {

  companion object {
    val WAVE_STARTED: EventType<WaveEvent> = EventType(ANY, "WaveStarted")
  }
}