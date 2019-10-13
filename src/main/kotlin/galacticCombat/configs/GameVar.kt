package galacticCombat.configs

import com.almasb.fxgl.dsl.getGameState

interface GameVar<T : Any> {
  val id: String

  fun set(value: T) = getGameState().setValue(id, value)

  fun get(): T = getGameState().getObject(id)
}

interface IntGameVar : GameVar<Int> {
  fun increment(increment: Int) {
    getGameState().increment(id, increment)
  }

  override fun get(): Int = getGameState().getInt(id)
}