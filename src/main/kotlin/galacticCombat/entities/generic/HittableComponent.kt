package galacticCombat.entities.generic

import com.almasb.fxgl.entity.component.Component
import galacticCombat.moddable.towerConfig.BulletData

abstract class HittableComponent : Component() {

  abstract fun hitWithBullet(bullet: BulletData)
}
