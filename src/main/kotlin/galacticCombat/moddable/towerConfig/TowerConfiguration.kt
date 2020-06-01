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
  val targetingMode: TargetingMode = TargetingMode(TargetingEntity.INVADER, TargetingStrategy.FOREMOST, true),
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

/**
 * The [TargetingMode] defines what entities are affected from the associated [BulletData].
 * @property targetingEntity Whether towers or invaders are targeted.
 * @property targetingStrategy Which targets are to be prioritized.
 * @property tracking Whether the bullet follows the target.
 * @property areaOfEffect The impact radius. -1 denotes the single-target mode, where only the target is being hit.
 */
@Serializable
data class TargetingMode(
  val targetingEntity: TargetingEntity,
  val targetingStrategy: TargetingStrategy,
  val tracking: Boolean,
  val areaOfEffect: Int = -1
) {
  init {
    check(tracking || areaOfEffect != -1) { "Tracking must be activated for single-target targeting mode." }
    if (targetingEntity != targetingStrategy.validTarget) invalidConfiguration()
  }

  fun invalidConfiguration(): Nothing = error("TargetingStrategy $targetingStrategy is not applicable for target $targetingEntity.")
}

enum class UpgradeLevel {
  First,
  Second,
  Third,
  Fourth,
  Fifth;

  fun hasNext() = this.ordinal + 1 < values().size
  fun next() = values()[this.ordinal + 1]
}

enum class BulletEffectType {
  NONE,
  POISON,
  SLOW,
  CHAIN
}

enum class TargetingEntity {
  INVADER,
  TOWER;
}

enum class TargetingStrategy(val validTarget: TargetingEntity = TargetingEntity.INVADER) {
  // TargetingEntity.Invader
  FOREMOST,
  UNTAINTED,
  HIGHEST_HEALTH,

  // TargetingEntity.Tower
  CLOSEST(TargetingEntity.TOWER)
  ;

}