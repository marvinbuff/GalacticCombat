package galacticCombat.event

import galacticCombat.invader.InvaderComponent
import javafx.event.Event
import javafx.event.EventType

class InvaderEvents(eventType: EventType<InvaderEvents>, val invader: InvaderComponent) : Event(eventType) {

  companion object {
    val INVADER_REACHED_GOAL: EventType<InvaderEvents> = EventType(ANY, "InvaderReachedGoalEvent")
    val INVADER_KILLED: EventType<InvaderEvents> = EventType(ANY, "InvaderKilled")
  }
}