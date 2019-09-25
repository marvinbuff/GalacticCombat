package galacticCombat.bullet

import galacticCombat.AssetsConfig
import galacticCombat.tower.TowerType
import galacticCombat.tower.TowerType.CANNON
import galacticCombat.tower.TowerType.CRYONIC
import galacticCombat.tower.TowerType.SPORE
import galacticCombat.tower.TowerType.STORM
import galacticCombat.tower.TowerType.TACTICAL

open class BulletData private constructor(
    val damage: Double,
    val penetration: Double, //TODO implement penetration and armour
    val attackDelay: Double = 2.0,
    val range: Double = 300.0,
    val bulletSpeed: Double = 300.0,
    val effect: BulletEffect = BulletEffect(BulletEffectType.NONE, 0.0, 0.0), //TODO implement effects
    val texture: String = AssetsConfig.get("beeper.png")
){
  companion object{
    const val id = "BulletData"

    fun create(towerType: TowerType, level: Int): BulletData =
      when(towerType){
        CANNON   -> createCannon(level)
        SPORE    -> createSpore(level)
        TACTICAL -> createTactical(level)
        CRYONIC  -> createCryonic(level)
        STORM    -> createCannon(level)
      }

    private fun createCannon(level: Int): BulletData {
      //TODO implement scaling with level and specialization
      return BulletData(10.0, 2.0)
    }

    private fun createSpore(level: Int): BulletData {
      //TODO implement scaling with level and specialization
      val bulletEffect = BulletEffect(BulletEffectType.POISON, 1.5, 6.0)
      return BulletData(4.0, 0.0, effect = bulletEffect)
    }

    private fun createCryonic(level: Int): BulletData {
      //TODO implement scaling with level and specialization
      val bulletEffect = BulletEffect(BulletEffectType.SLOW, 0.5, 6.0)
      return BulletData(4.0, 0.0, effect = bulletEffect)
    }

    private fun createTactical(level: Int): BulletData {
      //TODO implement scaling with level and specialization
      return BulletData(15.0, 8.0, 4.0)
    }

  }
}

data class BulletEffect(val type: BulletEffectType, val amount: Double, var duration: Double)

enum class BulletEffectType{
  NONE,
  POISON, //amount: dps
  SLOW
}