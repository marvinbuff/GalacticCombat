package galacticCombat.utils

import com.almasb.fxgl.dsl.EntityBuilder
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.logging.Logger
import javafx.event.Event

private val logger = Logger.get("Global Utils")

fun Event.fire() {
  logger.debug("Fire Event: ${this.eventType.name}")
  return FXGL.getEventBus().fireEvent(this)
}

fun GameWorld.removeEntitiesByType(vararg types: Enum<*>) {
  removeEntities(getEntitiesByType(*types))
}

private fun EntityBuilder.conditionalWith(vararg comps: Component?) = this.also {
  this.with(*comps.filterNotNull().toTypedArray())
}

private fun EntityBuilder.conditionalWith(comps: Component, condition: Boolean) = this.also {
  if (condition) {
    this.with(comps)
  }
}