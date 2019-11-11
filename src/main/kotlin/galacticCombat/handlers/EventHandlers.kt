package galacticCombat.handlers

import com.almasb.fxgl.dsl.getDisplay
import com.almasb.fxgl.dsl.getGameController
import com.almasb.fxgl.dsl.getGameWorld

fun gameWon() {
  getDisplay().showMessageBox("Game Won!") {
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
  getGameWorld().clear()
  getGameController().gotoMainMenu()
}