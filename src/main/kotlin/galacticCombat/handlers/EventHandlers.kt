package galacticCombat.handlers

import com.almasb.fxgl.dsl.getDisplay
import com.almasb.fxgl.dsl.getGameController
import com.almasb.fxgl.dsl.getSettings
import galacticCombat.configs.GameVarsInt
import kotlin.system.exitProcess

fun gameWon() {
  val x = GameVarsInt.SCORE.get()
  getDisplay().showMessageBox("Game Won!\n Score: $x") {
    gameEnd()
  }
}

fun gameLost() {
  getDisplay().showMessageBox("Game Over!") {
    gameEnd()
  }
}

fun gameEnd() {
  //todo add stats to profile
  //todo this does not compile anymore: getGameWorld().clear()
  if (getSettings().isMainMenuEnabled) getGameController().gotoMainMenu()
  else exitProcess(0)
}