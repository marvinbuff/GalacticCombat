package galacticCombat.level

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.level.Level
import com.almasb.fxgl.entity.level.LevelLoader
import galacticCombat.configs.AppConfig
import galacticCombat.configs.LevelDataVar
import galacticCombat.utils.loadJson
import java.net.URL


class GalacticCombatLevelLoader : LevelLoader {

    override fun load(url: URL, world: GameWorld): Level {
        val data = loadJson<LevelData>(url)
        data.setGameVars()
        LevelDataVar.set(data)

        val entities = listOf<Entity>()
        // Create Timer
        // Add Entities at specified time to Timer
        // Calibrate initial settings: gold, hp, pre-built towers, time per wave, etc

        return Level(AppConfig.WIDTH, AppConfig.HEIGHT, entities)
    }
}