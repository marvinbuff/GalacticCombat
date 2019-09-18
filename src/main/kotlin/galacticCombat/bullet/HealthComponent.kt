package galacticCombat.bullet

import com.almasb.fxgl.dsl.components.view.ChildViewComponent
import com.almasb.fxgl.ui.Position
import com.almasb.fxgl.ui.ProgressBar
import galacticCombat.enemy.EnemyComponent
import javafx.scene.paint.Color

class HealthComponent(private val invaderComponent: EnemyComponent) : ChildViewComponent(0.0, 0.0, false) {

    private val hpBar = ProgressBar()

    init {
        hpBar.setMinValue(0.0)
        hpBar.setMaxValue(invaderComponent.maxHealth)
        hpBar.setWidth(100.0)
        hpBar.isLabelVisible = false
        hpBar.setLabelPosition(Position.TOP)
        hpBar.setFill(Color.GREEN)

        hpBar.currentValueProperty().bind(
            invaderComponent.health.divide(invaderComponent.maxHealth*1.0).multiply(100)
        )

        viewRoot.children.addAll(hpBar)
    }
}