package galacticCombat.entities.tower

import galacticCombat.entities.generic.HittableComponent
import galacticCombat.moddable.towerConfig.BulletData

class HittableTowerComponent : HittableComponent() {
  override fun hitWithBullet(bullet: BulletData) {
    if (!entity.isActive) return
    println("Ou no I was hit, how dreadful!")
    //todo implement logic here!
  }

}