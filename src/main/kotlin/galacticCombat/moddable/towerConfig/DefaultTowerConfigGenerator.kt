package galacticCombat.moddable.towerConfig

import galacticCombat.utils.printJson

//todo  Try to populate variable with this file.
// use populated data in tower creation. remove old classes. Clean-up

private val levels = UpgradeLevel.values()

fun main() {
  val towers = listOf(
//    "Bomb Thrower" to createCannonTower(),
    "Cannon Tower" to createCannonTower(),
    "Cryonic Emitter" to createCryonicEmitter(),
    "Spore Launcher" to createSporeLauncher(),
//    "Storm Conjurer" to createSporeLauncher(),
    "Tactical Tower" to createTacticalTower()
  ).toMap()

  val config = TowerConfiguration(towers)
  printJson(config)
}

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
    "cannon_tower_1",
    "cannon_tower_2",
    "cannon_tower_3",
    "cannon_tower_cross_4",
    "cannon_tower_cross_5"
  )
  val textureByLevel = levels.zip(textures).toMap()
  return TowerData("Cannon Tower", bulletsByLevel, textureByLevel)
}

private fun createTacticalTower(): TowerData {
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
    "tactical_tower_1",
    "tactical_tower_2",
    "tactical_tower_3",
    "tactical_tower_cross_4",
    "tactical_tower_cross_5"
  )
  val textureByLevel = levels.zip(textures).toMap()
  return TowerData("Tactical Tower", bulletsByLevel, textureByLevel)
}

private fun createSporeLauncher(): TowerData {
  val bullets = listOf(
    // effect.amount = 2.5 + (level - 1) * 1.0
    BulletData(
      6.0, 1.0,
      effect = BulletEffect(BulletEffectType.POISON, 2.5, 6.0)
    ),
    BulletData(
      6.0, 1.0,
      effect = BulletEffect(BulletEffectType.POISON, 3.5, 6.0)
    ),
    BulletData(
      6.0, 1.0,
      effect = BulletEffect(BulletEffectType.POISON, 4.5, 6.0)
    ),
    BulletData(
      6.0, 1.0,
      effect = BulletEffect(BulletEffectType.POISON, 5.5, 6.0)
    ),
    BulletData(
      6.0, 1.0,
      effect = BulletEffect(BulletEffectType.POISON, 6.5, 6.0)
    )
  )
  val bulletsByLevel = levels.zip(bullets).toMap()
  val textures = listOf(
    "spore_launcher_1",
    "spore_launcher_2",
    "spore_launcher_3",
    "spore_launcher_cross_4",
    "spore_launcher_cross_5"
  )
  val textureByLevel = levels.zip(textures).toMap()
  return TowerData("Spore Launcher", bulletsByLevel, textureByLevel)
}

private fun createCryonicEmitter(): TowerData {
  val bullets = listOf(
    // effect.amount = 0.7 - (level - 1) * 0.05
    BulletData(
      6.0, 0.0,
      effect = BulletEffect(BulletEffectType.SLOW, 0.7, 4.0)
    ),
    BulletData(
      6.0, 0.0,
      effect = BulletEffect(BulletEffectType.SLOW, 0.65, 4.0)
    ),
    BulletData(
      6.0, 0.0,
      effect = BulletEffect(BulletEffectType.SLOW, 0.6, 4.0)
    ),
    BulletData(
      6.0, 1.0,
      effect = BulletEffect(BulletEffectType.SLOW, 0.55, 4.0)
    ),
    BulletData(
      6.0, 1.0,
      effect = BulletEffect(BulletEffectType.SLOW, 0.5, 4.0)
    )
  )
  val bulletsByLevel = levels.zip(bullets).toMap()
  val textures = listOf(
    "cryonic_emitter_1",
    "cryonic_emitter_2",
    "cryonic_emitter_3",
    "cryonic_emitter_cone_4",
    "cryonic_emitter_cone_5"
  )
  val textureByLevel = levels.zip(textures).toMap()
  return TowerData("Cryonic Emitter", bulletsByLevel, textureByLevel)
}