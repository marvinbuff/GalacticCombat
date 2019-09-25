package galacticCombat.invader

import com.almasb.fxgl.dsl.components.view.ChildViewComponent
import com.almasb.fxgl.ui.Position
import com.almasb.fxgl.ui.ProgressBar
import javafx.scene.paint.Color

class HealthComponent(invaderComponent: InvaderComponent) : ChildViewComponent(-5.0, -15.0, false) {

  private val hpBar = ProgressBar(false)

  init {
    hpBar.setMinValue(0.0)
    hpBar.setMaxValue(invaderComponent.data.maxHealth)
    hpBar.setWidth(35.0)
    hpBar.setHeight(8.0)
    hpBar.setLabelPosition(Position.TOP)
    hpBar.isLabelVisible = false
    hpBar.setLabelFill(Color.RED)
    hpBar.setFill(Color.GREEN)

    hpBar.currentValueProperty().bind(
        invaderComponent.health.divide(invaderComponent.data.maxHealth * 1.0).multiply(100)
    )

    viewRoot.children.addAll(hpBar)
  }
}