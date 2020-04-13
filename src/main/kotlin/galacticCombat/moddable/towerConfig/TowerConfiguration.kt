package galacticCombat.moddable.towerConfig

import galacticCombat.configs.AssetConfig
import kotlinx.serialization.Serializable

@Serializable
data class TowerConfiguration(
  val towerConfigs: Map<String, TowerData>
)

@Serializable
data class TowerData(
  val name: String,
  val bulletByLevel: Map<UpgradeLevel, BulletData>,
  val textureByLevel: Map<UpgradeLevel, String>,
  val price: Int = 100
) {

  companion object {
    const val id = "TowerData"
  }
}

fun TowerData.getFirstTexture() = textureByLevel.getValue(UpgradeLevel.First)
fun TowerData.getFirstBullet() = bulletByLevel.getValue(UpgradeLevel.First)

@Serializable
data class BulletData(
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
  }
}

/**
 * The BulletEffect holds the number sufficient for an effect.
 * @property type The [BulletEffectType] defines how the invader reacts to it.
 * @property amount The strength of the effect. For poison it is the damage per second.
 * @property duration How long the effect persists.
 */
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
  Fifth(5);

  fun hasNext() = this.ordinal + 1 < values().size
  fun next() = values()[this.ordinal + 1]
}

enum class BulletEffectType {
  NONE,
  POISON,
  SLOW,
  CHAIN
}