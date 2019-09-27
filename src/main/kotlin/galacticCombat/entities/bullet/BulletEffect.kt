package galacticCombat.entities.bullet

/**
 * The BulletEffect holds the number sufficient for an effect.
 * @property type The [BulletEffectType] defines how the invader reacts to it.
 * @property amount The strength of the effect. For poison it is the damage per second.
 * @property duration How long the effect persists.
 */
data class BulletEffect(val type: BulletEffectType, val amount: Double, var duration: Double)

enum class BulletEffectType {
  NONE,
  POISON,
  SLOW,
  CHAIN
}