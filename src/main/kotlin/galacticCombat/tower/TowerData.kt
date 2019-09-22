package galacticCombat.tower

import galacticCombat.bullet.BulletData

data class TowerData(
    val towerType: TowerType,
    val bulletData: BulletData,
    val texture: String,
    val price: Int = 100
) {
  companion object {
    const val id = "TowerData"
  }
}