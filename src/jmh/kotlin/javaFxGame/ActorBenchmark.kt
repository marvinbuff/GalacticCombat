package javaFxGame

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(time = 10, timeUnit = TimeUnit.MILLISECONDS, iterations = 1)
@Measurement(time = 20, timeUnit = TimeUnit.MILLISECONDS, iterations = 1)
open class ActorBenchmark {

    @Benchmark
    fun deleteActorsByDeathMark() {
        val sum = (0..1).map { Random.nextInt() }.sum()
        println("Sum is $sum")
    }
}