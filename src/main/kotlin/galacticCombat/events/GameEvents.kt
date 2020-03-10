package galacticCombat.events

import javafx.event.Event
import javafx.event.EventType

class GameEvents(eventType: EventType<GameEvents>) : Event(eventType) {

  companion object {
    val LEVEL_WON: EventType<GameEvents> = EventType(ANY, "LevelFinished")
    val LEVEL_LOST: EventType<GameEvents> = EventType(ANY, "LevelLost")
  }
}

class WaveEvents(eventType: EventType<WaveEvents>, val waveIndex: Int) : Event(eventType) {

  companion object {
    val WAVE_STARTED: EventType<WaveEvents> = EventType(ANY, "WaveStarted")
  }
}