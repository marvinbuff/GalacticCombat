package galacticCombat.utils

import com.almasb.fxgl.dsl.FXGL
import com.almasb.sslogger.Logger
import javafx.event.Event

private val logger = Logger.get("Global Utils")

fun Event.fire() {
  logger.debug("Fire Event: ${this.eventType.name}")
  return FXGL.getEventBus().fireEvent(this)
}