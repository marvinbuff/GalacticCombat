package galacticCombat.moddable.towerConfig

import galacticCombat.entities.tower.TowerType
import galacticCombat.utils.printJson

fun main() {
  val towers = listOf(
    TowerType.CANNON.title to createCannonTower(),
    TowerType.CRYONIC.title to createCryonicEmitter(),
    TowerType.SPORE.title to createSporeLauncher(),
    TowerType.BOMBER.title to createBomberThrower(),
//    "Storm Conjurer" to createSporeLauncher(),
    TowerType.RAY.title to createRayBlaster()
  ).toMap()

  val config = TowerConfiguration(towers)
  printJson(config)
}

private val levels = UpgradeLevel.values()

private fun createCannonTower(): TowerData {
  val bullets = listOf(
    // damage = 12.0 + (level - 1) * 3
    BulletData(12.0, 2.0),
    BulletData(15.0, 2.0),
    BulletData(18.0, 2.0),
    BulletData(18.0, 4.0, range = 110.0),
    BulletData(21.0, 6.0, range = 120.0)
  )
  val bulletsByLevel = levels.zip(bullets).toMap()
  val textures = listOf(
    "towers/cannon_tower_1.gif",
    "towers/cannon_tower_2.gif",
    "towers/cannon_tower_3.gif",
    "towers/cannon_tower_cross_4.gif",
    "towers/cannon_tower_cross_5.gif"
  )
  val textureByLevel = levels.zip(textures).toMap()
  return TowerData(TowerType.CANNON.title, bulletsByLevel, textureByLevel)
}

private fun createRayBlaster(): TowerData {
  val bullets = listOf(
    // damage = 20.0 + (level - 1) * 2
    // range = 150.0 + (level - 1) * 30.0
    BulletData(20.0, 4.0, 2.5, 150.0),
    BulletData(22.0, 4.0, 2.5, 180.0),
    BulletData(24.0, 4.0, 2.5, 210.0),
    BulletData(24.0, 6.0, 2.5, 240.0),
    BulletData(26.0, 8.0, 2.5, 240.0)
  )
  val bulletsByLevel = levels.zip(bullets).toMap()
  val textures = listOf(
    "towers/ray_blaster_1.gif",
    "towers/ray_blaster_2.gif",
    "towers/ray_blaster_3.gif",
    "towers/ray_blaster_cross_4.gif",
    "towers/ray_blaster_cross_5.gif"
  )
  val textureByLevel = levels.zip(textures).toMap()
  return TowerData(TowerType.RAY.title, bulletsByLevel, textureByLevel)
}

private fun createSporeLauncher(): TowerData {
  val targetingMode = TargetingMode(TargetingEntity.INVADER, TargetingStrategy.UNTAINTED, true, -1)
  val bullets = listOf(
    // effect.amount = 2.5 + (level - 1) * 1.0
    BulletData(
      6.0, 1.0,
      effect = BulletEffect(BulletEffectType.POISON, 2.5, 6.0),
      targetingMode = targetingMode
    ),
    BulletData(
      6.0, 1.0,
      effect = BulletEffect(BulletEffectType.POISON, 3.5, 6.0),
      targetingMode = targetingMode
    ),
    BulletData(
      6.0, 1.0,
      effect = BulletEffect(BulletEffectType.POISON, 4.5, 6.0),
      targetingMode = targetingMode
    ),
    BulletData(
      6.0, 1.0,
      effect = BulletEffect(BulletEffectType.POISON, 5.5, 6.0),
      targetingMode = targetingMode
    ),
    BulletData(
      6.0, 1.0,
      effect = BulletEffect(BulletEffectType.POISON, 6.5, 6.0),
      targetingMode = targetingMode
    )
  )
  val bulletsByLevel = levels.zip(bullets).toMap()
  val textures = listOf(
    "towers/spore_launcher_1.gif",
    "towers/spore_launcher_2.gif",
    "towers/spore_launcher_3.gif",
    "towers/spore_launcher_cross_4.gif",
    "towers/spore_launcher_cross_5.gif"
  )
  val textureByLevel = levels.zip(textures).toMap()
  return TowerData(TowerType.SPORE.title, bulletsByLevel, textureByLevel)
}

private fun createCryonicEmitter(): TowerData {
  val createBaseBullet = { level: Int ->
    BulletData(
      6.0, 0.0,
      effect = BulletEffect(BulletEffectType.SLOW, 0.7 - (level - 1) * 0.05, 4.0),
      targetingMode = TargetingMode(TargetingEntity.INVADER, TargetingStrategy.UNTAINTED, true, -1)
    )
  }

  val bullets = (1..5).map(createBaseBullet)
  val bulletsByLevel = levels.zip(bullets).toMap()
  val textures = listOf(
    "towers/cryonic_emitter_1.gif",
    "towers/cryonic_emitter_2.gif",
    "towers/cryonic_emitter_3.gif",
    "towers/cryonic_emitter_cone_4.gif",
    "towers/cryonic_emitter_cone_5.gif"
  )
  val textureByLevel = levels.zip(textures).toMap()
  return TowerData(TowerType.CRYONIC.title, bulletsByLevel, textureByLevel)
}

private fun createBomberThrower(): TowerData {
  val targetingMode = TargetingMode(TargetingEntity.INVADER, TargetingStrategy.FOREMOST, false, 100)
  val bullets = listOf(
    // damage = 6.0 + (level - 1) * 1.5
    BulletData(6.0, 2.0, targetingMode = targetingMode),
    BulletData(7.5, 2.0, targetingMode = targetingMode),
    BulletData(9.0, 2.0, targetingMode = targetingMode),
    BulletData(10.5, 4.0, range = 110.0, targetingMode = targetingMode),
    BulletData(12.0, 4.0, range = 120.0, targetingMode = targetingMode)
  )
  val bulletsByLevel = levels.zip(bullets).toMap()
  val textures = listOf(
    "towers/bomb_thrower_1.gif",
    "towers/bomb_thrower_2.gif",
    "towers/bomb_thrower_3.gif",
    "towers/bomb_thrower_cross_4.gif",
    "towers/bomb_thrower_cross_5.gif"
  )
  val textureByLevel = levels.zip(textures).toMap()
  return TowerData(TowerType.BOMBER.title, bulletsByLevel, textureByLevel)
}