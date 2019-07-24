package javaFxGame

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit
import kotlin.random.Random

private const val ACTORS_SIZE = 10000

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(time = 20, timeUnit = TimeUnit.MILLISECONDS, iterations = 10) //Call benchmark method as often as possible within 20 milliseconds, repeat 10 times.
@Measurement(time = 40, timeUnit = TimeUnit.MILLISECONDS, iterations = 20)
open class ActorBenchmark {

  @State(Scope.Thread)//Thread, Group, Benchmark
  open class WorldDummy {
    var actors: MutableList<String> = mutableListOf()

    @Setup(Level.Invocation)
    fun populateActors() {
      (0..ACTORS_SIZE).map { "Actor $it" }.toCollection(actors)
    }
  }

  /*Step 1: mark who is to be removed
      - create boolean array
      - iterate over and call markToKill per element
    Step 2: remove marked from actors
      - iterate over boolean array and remove
   */
  @Benchmark
  fun deleteActorsByKillMark(state: WorldDummy, bh: Blackhole) {
    val markedOnes = BooleanArray(state.actors.size, ::markToKill)
    markedOnes.withIndex().reversed().filter { it.value }.forEach { state.actors.removeAt(it.index) }
    bh.consume(state.actors)
    println("Called benchmark")
  }

  /*Step 1: Copy actors
    Step 2: Remove from copy
    Step 3: Set actors to modified copy
   */
  @Benchmark
  fun deleteActorsByCopyingList(state: WorldDummy, bh: Blackhole) {
    val actorsCopy = state.actors.toMutableList()
    state.actors.withIndex().reversed().forEach {
      if (markToKill(it.index)) actorsCopy.removeAt(it.index)
    }
    state.actors = actorsCopy
    bh.consume(state.actors)
    println("Called other")
  }

  private fun markToKill(index: Int): Boolean {
    return Random.nextBoolean()
  }
}

/**== State ==
 * Thread - Use one state per thread
 * Group - Use one state per thread group
 * Benchmark - Use one state over all
 *
 * == Setup and TearDown ==
 * Trial - Once per fork
 * Iteration - Once per iteration
 * Invocation - One per benchmark
 */

/**== JVM Optimization Dangers ==
 * Dead Code Elimination - Use black hole or return
 * Constant Folding - Take constants from the benchmark to the state
 */