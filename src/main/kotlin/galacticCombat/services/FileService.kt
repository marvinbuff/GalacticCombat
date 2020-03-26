package galacticCombat.services

import java.io.File
import java.net.URL

//todo move this into some sort of serviceProvider
private val boundFileService = DefaultFileService()
fun getFileService(): FileService = boundFileService

interface FileService {
  fun getLevelURL(level: String): URL
}

private const val LEVEL_SUFFIX = ".level"

private class DefaultFileService : FileService {
  private val homeFolder = File(System.getProperty("user.home"))
  private val gcFolder = homeFolder.resolve(".galacticCombat")
  private val levelFolder = gcFolder.resolve("levels")

  init {
    if (!gcFolder.exists()) gcFolder.mkdir()
    if (!levelFolder.exists()) levelFolder.mkdir()
  }

  override fun getLevelURL(level: String): URL {
    return levelFolder.resolve("$level$LEVEL_SUFFIX").toURI().toURL()
  }

}