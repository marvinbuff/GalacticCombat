package galacticCombat.services

import java.io.File
import java.io.FileNotFoundException
import java.net.URL

//todo move this into some sort of serviceProvider, maybe Koin.
private val boundFileService = DefaultFileService()
fun getFileService(): FileService = boundFileService

interface FileService {
  fun getLevelURL(level: String): URL
  fun getLocalFileContent(path: String): String?
}

private const val LEVEL_SUFFIX = ".level"

private class DefaultFileService : FileService {
  //todo this and Folders/Files classes need rework
  private val homeFolder = File(System.getProperty("user.home"))
  private val gcFolder = homeFolder.resolve(Folders.GC_HOME.path)
  private val levelFolder = gcFolder.resolve(Folders.LEVELS.path)
  private val modConfigFolder = gcFolder.resolve(Folders.MODDED_CONFIG_FOLDER.path)

  init {
    listOf(gcFolder, levelFolder, modConfigFolder)
      .forEach { folder ->
        if (!folder.exists()) folder.mkdir()
      }
  }

  override fun getLevelURL(level: String): URL = levelFolder.resolve("$level$LEVEL_SUFFIX").toURI().toURL()

  override fun getLocalFileContent(path: String): String? {
    return try {
      gcFolder.resolve(path).toURI().toURL().readText()
    } catch (e: FileNotFoundException) {
      null
    }
  }

}

enum class Folders(val path: String) {
  // Local Folders
  GC_HOME(".galacticCombat"),
  LEVELS("levels"),
  MODDED_CONFIG_FOLDER("moddedConfig");
}

enum class Files(val path: String) {
  // Configs
  TOWER_CONFIG_FILE("/assets/config/towerConfiguration.json"),
  LOCAL_TOWER_CONFIG_FILE("moddedConfig/towerConfiguration.json")
  ;
}