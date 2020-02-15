package galacticCombat.configs

import com.almasb.fxgl.dsl.getGameState
import javafx.beans.property.BooleanProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.StringProperty

interface GameVar<T : Any> {
  val id: String

  fun set(value: T) = getGameState().setValue(id, value)

  fun get(): T = getGameState().getObject(id)
}

interface IntGameVar : GameVar<Int> {
  fun increment(increment: Int = 1) {
    getGameState().increment(id, increment)
  }

  override fun get(): Int = getGameState().getInt(id)

  fun property(): IntegerProperty = getGameState().intProperty(id)
}

interface BooleanGameVar : GameVar<Boolean> {
  override fun get(): Boolean = getGameState().getBoolean(id)

  fun property(): BooleanProperty = getGameState().booleanProperty(id)
}

interface StringGameVar : GameVar<String> {
  override fun get(): String = getGameState().getString(id)

  fun property(): StringProperty = getGameState().stringProperty(id)
}