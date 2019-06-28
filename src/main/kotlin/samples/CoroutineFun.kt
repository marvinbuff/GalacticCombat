package samples

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
  val time = measureTimeMillis {
    val v1 = async { retry { f1(it) } }
    val v2 = async { retry { f2(it) } }
    println("Result = ${v1.await() + v2.await()}")
  }
  println("Completed in $time ms")
}

suspend fun f1(i: Int): Int {
  println("f1 attempt $i")
  delay(if (i != 3) 2000 else 200)
  return 1
}

suspend fun f2(i: Int): Int {
  println("f2 attempt $i")
  delay(if (i != 3) 2000 else 200)
  return 2
}

suspend fun <T> retry(block: suspend (Int) -> T): T {
  for (i in 1..5) { // try 5 times
    try {
      return withTimeout(500) { // with timeout
        block(i)
      }
    } catch (e: TimeoutCancellationException) { /* retry */ }
  }
  return block(0) // last time just invoke without timeout
}