package galacticCombat.bullet

import galacticCombat.tower.TowerType
import galacticCombat.tower.TowerType.*

class BulletData private constructor(
    val damage: Double,
    val penetration: Double,
    val attackDelay: Double = 2.0,
    val range: Double = 300.0,
    val effect: BulletEffect = BulletEffect(BulletEffectType.NONE, 0.0, 0.0)
){
  companion object{
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