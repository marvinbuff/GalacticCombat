package galacticCombat.utils

import com.almasb.fxgl.dsl.FXGL
import javafx.event.Event

//fun Event.fire() = FXGL.getEventBus().fireEvent(this)

fun Event.fire() {
  println("this.eventType.name = ${this.eventType.name}")
  //todo change to logging
  return FXGL.getEventBus().fireEvent(this)
}