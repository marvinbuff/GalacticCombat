package galacticCombat.level

import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.level.Level
import com.almasb.fxgl.entity.level.LevelLoader
import galacticCombat.configs.AppConfig
import galacticCombat.configs.LevelDataVar
import galacticCombat.entities.LEVEL_CONTROLLER_ID
import galacticCombat.utils.loadJson
import java.net.URL


class GalacticCombatLevelLoader : LevelLoader {

    override fun load(url: URL, world: GameWorld): Level {
        val data = loadJson<LevelData>(url)
        //todo sanity check of read data
        data.setGameVars()
        LevelDataVar.set(data)
        val controller = world.create(LEVEL_CONTROLLER_ID, SpawnData(0.0, 0.0))

        val entities = listOf(controller)
        // Create Timer
        // Add Entities at specified time to Timer
        // Calibrate initial settings: gold, hp, pre-built towers, time per wave, etc

        return Level(AppConfig.WIDTH, AppConfig.HEIGHT, entities)
    }
}