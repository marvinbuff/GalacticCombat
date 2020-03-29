package galacticCombat.entities.bullet

import galacticCombat.configs.AssetConfig
import galacticCombat.entities.tower.TowerType
import galacticCombat.entities.tower.TowerType.CANNON
import galacticCombat.entities.tower.TowerType.CRYONIC
import galacticCombat.entities.tower.TowerType.SPORE
import galacticCombat.entities.tower.TowerType.STORM
import galacticCombat.entities.tower.TowerType.TACTICAL

open class BulletData private constructor(
  val damage: Double,
  val penetration: Double,
  val attackDelay: Double = 1.0,
  val range: Double = 100.0,
  val bulletSpeed: Double = 300.0,
  val effect: BulletEffect = BulletEffect(BulletEffectType.NONE, 0.0, 0.0),
  val texture: String = AssetConfig.get("beeper.png")
) {
  companion object {
    const val id = "BulletData"

    //TODO implement scaling with level and specialization
    fun create(towerType: TowerType, level: Int): BulletData =
      when (towerType) {
        CANNON   -> createCannon(level)
        SPORE    -> createSpore(level)
        TACTICAL -> createTactical(level)
        CRYONIC  -> createCryonic(level)
        STORM    -> createLightning(level)
      }

    private fun createCannon(level: Int): BulletData {
      // todo this function could be provided by moddable script
      val damage = 12.0 + (level - 1) * 3
      return BulletData(damage, 2.0)
    }

    private fun createSpore(level: Int): BulletData {
      val bulletEffect = BulletEffect(BulletEffectType.POISON, 2.5 + (level - 1) * 1.0, 6.0)
      return BulletData(6.0, 1.0, effect = bulletEffect)
    }

    private fun createCryonic(level: Int): BulletData {
      val bulletEffect = BulletEffect(BulletEffectType.SLOW, 0.2 + (level - 1) * 0.05, 6.0)
      return BulletData(4.0, 0.0, effect = bulletEffect)
    }

    private fun createTactical(level: Int): BulletData {
      // 15 + range + pen
      return BulletData(20.0 + (level - 1) * 2, 4.0, 2.5, 150.0 + (level - 1) * 30.0)
    }

    private fun createLightning(level: Int): BulletData {
      val bulletEffect = BulletEffect(BulletEffectType.CHAIN, 0.6, 2.0)
      return BulletData(5.0, 0.0, 1.0)
    }

  }
}