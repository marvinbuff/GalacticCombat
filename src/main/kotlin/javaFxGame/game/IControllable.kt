package javaFxGame.game

interface IControllable {

  fun keysPressed(keys: Collection<String>, controls: Controls) {
    val actions = mutableSetOf<Action>()
    keys.forEach { actions.add(controls.getActionFromKey(it)) }
    executeActions(actions)
  }

  fun executeActions(actions: Set<Action>)

}

enum class Action {
  TURN_LEFT,
  TURN_RIGHT,
  ACCELERATE,
  DECELERATE,
  SHOOT,
  OTHER
}

enum class Controls(
    private val turnLeft: String,
    private val turnRight: String,
    private val accelerate: String,
    private val decelerate: String,
    private val shoot: String
) {
  SOLO_PLAYER("LEFT", "RIGHT", "UP", "DOWN", "SPACE"),
  PLAYER1("A", "D", "W", "S", "T");

  fun getActionFromKey(key: String): Action =
      when (key) {
        turnLeft   -> Action.TURN_LEFT
        turnRight  -> Action.TURN_RIGHT
        accelerate -> Action.ACCELERATE
        decelerate -> Action.DECELERATE
        shoot      -> Action.SHOOT
        else       -> Action.OTHER
      }

}