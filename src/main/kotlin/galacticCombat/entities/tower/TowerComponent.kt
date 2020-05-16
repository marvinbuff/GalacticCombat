package galacticCombat.entities.tower

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import galacticCombat.configs.InfoPanelVar
import galacticCombat.configs.LevelGameVars
import galacticCombat.configs.loadImage
import galacticCombat.entities.BULLET_SPAWN_ID
import galacticCombat.entities.EntityType
import galacticCombat.entities.generic.RangeIndicatorComponent
import galacticCombat.entities.invader.InvaderComponent
import galacticCombat.moddable.towerConfig.BulletData
import galacticCombat.moddable.towerConfig.TowerData
import galacticCombat.moddable.towerConfig.UpgradeLevel
import galacticCombat.ui.HasInfo
import galacticCombat.ui.InfoPanel
import galacticCombat.utils.toPoint
import javafx.beans.binding.Bindings
import javafx.beans.binding.StringBinding
import javafx.geometry.Point2D
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.transform.Rotate

@Required(ProjectileComponent::class)
class TowerComponent(private val towerData: TowerData) : Component(), HasInfo {
  private lateinit var projectile: ProjectileComponent
  private var reloadingTime: Double = 0.0
  private val bullet: BulletData
    get() = towerData.bulletByLevel.getValue(level)
  var level: UpgradeLevel = UpgradeLevel.First
  private val btn by lazy(LazyThreadSafetyMode.NONE) { initializeUpgradeButton() }

  override fun onUpdate(tpf: Double) {
    val closestInvader = getGameWorld()
      .getEntitiesByType(EntityType.INVADER)
      .filter { other -> entity.anchoredPosition.distance(other.anchoredPosition) < bullet.range }
      .maxBy { it.getComponent(InvaderComponent::class.java).getProgress() }
    closestInvader?.let {
      projectile.direction = Rotate.rotate(-45.0, 0.0, 0.0).transform(it.anchoredPosition.subtract(entity.anchoredPosition))
      if (reloadingTime <= 0.0) {
        shoot(closestInvader)
        reloadingTime = bullet.attackDelay
      } else {
        reloadingTime -= tpf
      }
    }
  }

  override fun onRemoved() {
    super.onRemoved()
    btn.disableProperty().unbind()
  }

  fun upgrade() {
    //todo implement specialization logic
    check(level.hasNext()) { "Cannot upgrade tower higher then level 5: $level!" }
    val exp = LevelGameVars.EXPERIENCE.get()
    if (exp < EXPERIENCE_COST) return
    else LevelGameVars.EXPERIENCE.set(exp - EXPERIENCE_COST)
    level = level.next()
    updateView()
  }

  private fun updateView() {
    entity.viewComponent.clearChildren()
    entity.viewComponent.addChild(FXGL.Companion.texture(towerData.textureByLevel.getValue(level)))
    InfoPanelVar.get().update(this)
  }

  private fun shoot(target: Entity) {
    getGameWorld().spawn(
      BULLET_SPAWN_ID,
      SpawnData(entity.anchoredPosition)
        .put("target", target).put(BulletData.id, bullet)
    )
  }

  //region -------------------- HasInfo ------------------------

  override fun getTitle(): String = towerData.name

  override fun getInformation(): StringBinding {
    //todo show also effect
    val info = "Damage: \t\t${bullet.damage}\n" +
        "Reloading: \t${bullet.attackDelay}\n" +
        "Penetration: \t${bullet.penetration}\n"
    return Bindings.createStringBinding({ info }, null)
  }

  override fun getTexture(): Image = towerData.textureByLevel.getValue(level).loadImage()

  private fun initializeUpgradeButton(): Button {
    val btn = Button("Upgrade")
    btn.setOnAction {
      upgrade()
    }
    btn.styleClass += "button_green"
    return btn
  }

  override fun activate(panel: InfoPanel) {
    entity.addComponent(RangeIndicatorComponent(bullet.range, center))
    if (level.hasNext()) {
      btn.disableProperty().bind(LevelGameVars.EXPERIENCE.property().lessThan(EXPERIENCE_COST))
      panel.bottomChildrenProperty.add(btn)
    }
  }

  override fun deactivate(panel: InfoPanel) {
    entity.removeComponent(RangeIndicatorComponent::class.java)
    panel.bottomChildrenProperty.clear()
    btn.disableProperty().unbind()
  }

  //endregion

  companion object {
    val center: Point2D = (38 / 2.0).toPoint()
    const val EXPERIENCE_COST = 100
  }
}