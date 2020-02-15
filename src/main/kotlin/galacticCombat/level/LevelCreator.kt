package galacticCombat.level

import galacticCombat.entities.invader.InvaderType
import galacticCombat.level.json.InvaderArgs
import galacticCombat.level.json.LevelData
import galacticCombat.level.json.Path
import galacticCombat.level.json.Settings
import galacticCombat.level.json.Wave
import galacticCombat.utils.jsonSerializer
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.stringify
import kotlin.random.Random

val random = Random(System.currentTimeMillis())

@UnstableDefault
@ImplicitReflectionSerializer
fun main() {
  println("Experiment Level File:")

  val data = LevelData(
      "Experiment Level",
      settings = createSettings(),
      waves = listOf(createWave(10), createWave(4)),
      paths = mutableListOf(createPath("default"))
  )

  val json = jsonSerializer.stringify(data)
  println(json)
}

//region -------------------------- Private Utils -----------------------------

private fun createWave(invaders: Int) = Wave(createSpawnTimes(invaders).zip(createInvaders(invaders)))

private fun createSettings() = Settings(500, 5, 10, 5, 200, 60.0)

private fun createPath(title: String) = Path(title, mutableListOf(50 to 150, 150 to 350, 550 to 350, 130 to 120))

private fun createSpawnTimes(number: Int): DoubleArray {
  val times = mutableListOf<Double>()
  var lastTime = 0.0
  repeat(number) {
    lastTime += random.nextDouble(0.1, 2.0)
    times.add(lastTime)
  }
  return times.toDoubleArray()
}

private fun createInvaders(number: Int) = (0..number).map(::createInvader)

private fun createInvader(key: Int): InvaderArgs {
  val types = InvaderType.values()
  val type = types[key % types.size]
  val level = random.nextInt(1, 4)
  return InvaderArgs(type, level)
}
//endregion