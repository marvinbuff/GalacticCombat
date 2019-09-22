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
        CANNON -> createCannon(level)
        SPORE  -> createSpore(level)
        TACTICAL -> createCannon(level)
        CRYONIC -> createCannon(level)
        STORM -> createCannon(level)
      }

    private fun createCannon(level: Int): BulletData {
      //TODO implement scaling with level and specialization
      return BulletData(10.0, 2.0)
    }

    private fun createSpore(level: Int): BulletData {
      //TODO implement scaling with level and specialization
      val bulletEffect = BulletEffect(BulletEffectType.POISON, 10.0, 6.0)
      return BulletData(5.0, 0.0, effect = bulletEffect)
    }

  }
}

data class BulletEffect(val type: BulletEffectType, val amount: Double, val duration: Double)

enum class BulletEffectType{
  NONE,
  POISON,
  SLOW
}