package galacticCombat.moddable.towerConfig

import galacticCombat.configs.AssetConfig
import kotlinx.serialization.Serializable

@Serializable
data class TowerConfiguration(
  val towerConfigs: Map<String, TowerData>
) {
  fun getTowerIdentifiers() = towerConfigs.keys
}

@Serializable
data class TowerData(
  val name: String,
  val bulletByLevel: Map<UpgradeLevel, BulletData>,
  val textureByLevel: Map<UpgradeLevel, String>,
  val price: Int = 100
)

@Serializable
data class BulletData(
  val damage: Double,
  val penetration: Double,
  val attackDelay: Double = 1.0,
  val range: Double = 100.0,
  val bulletSpeed: Double = 300.0,
  val effect: BulletEffect = BulletEffect(BulletEffectType.NONE, 0.0, 0.0),
  val texture: String = AssetConfig.get("beeper.png")
)

@Serializable
data class BulletEffect(
  val type: BulletEffectType,
  val amount: Double,
  var duration: Double
)

enum class UpgradeLevel(val level: Int) {
  First(1),
  Second(2),
  Third(3),
  Fourth(4),
  Fifth(5)
}

enum class BulletEffectType {
  NONE,
  POISON,
  SLOW,
  CHAIN
}