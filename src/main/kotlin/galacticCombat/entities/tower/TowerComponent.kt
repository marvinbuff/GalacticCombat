package galacticCombat.entities.tower

import com.almasb.fxgl.dsl.components.ProjectileComponent
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import galacticCombat.configs.loadImage
import galacticCombat.entities.BULLET_SPAWN_ID
import galacticCombat.entities.EntityType
import galacticCombat.entities.bullet.BulletData
import galacticCombat.entities.invader.InvaderComponent
import galacticCombat.ui.HasInfo
import galacticCombat.utils.toPoint
import javafx.beans.binding.Bindings
import javafx.beans.binding.StringBinding
import javafx.scene.image.Image
import javafx.scene.transform.Rotate

@Required(ProjectileComponent::class)
open class TowerComponent(private val towerData: TowerData) : Component(), HasInfo {
  lateinit var projectile: ProjectileComponent
  private var reloadingTime: Double = 0.0
  private val bullet = towerData.bulletData

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

  private fun shoot(target: Entity) {
    getGameWorld().spawn(
        BULLET_SPAWN_ID,
        SpawnData(entity.anchoredPosition)
            .put("target", target).put(BulletData.id, bullet)
    )
  }

  //region -------------------- HasInfo ------------------------

  override fun getTitle(): String = towerData.towerType.title

  override fun getInformation(): StringBinding {
    //todo show also effect
    val info = "Damage: \t\t${bullet.damage}\n" +
        "Reloading: \t${bullet.attackDelay}\n" +
        "Penetration: \t${bullet.penetration}\n"
    return Bindings.createStringBinding({ info }, null)
  }

  override fun getTexture(): Image = towerData.texture.loadImage()

  //endregion

  companion object {
    val center = (38 / 2.0).toPoint()
  }
}