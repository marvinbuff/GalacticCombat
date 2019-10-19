package galacticCombat.utils

import com.almasb.fxgl.dsl.FXGL
import javafx.event.Event

fun Event.fire() = FXGL.getEventBus().fireEvent(this)