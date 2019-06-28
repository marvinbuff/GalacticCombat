package samples

import kotlinx.coroutines.*
import java.util.*
import kotlin.Comparator
import kotlin.math.absoluteValue

const val threshold = 1024

@UseExperimental(ExperimentalUnsignedTypes::class)

fun main() {
  println("Started ..")

  val ran = Random()
  val values = (1..10).map { ran.nextInt().absoluteValue }.toMutableList()

  val comparator = compareBy<Int> { it }

  println("Unordered list: $values")
  val sorted = runMergeSortInPlace(values, comparator)
  println("Unordered list: $values")
  println("Sorted list: $sorted")
}


// ----------------------------------------- Merge Sort: parallel, in-place -------------------------------------------
fun <T> runMergeSortInPlace(list: MutableList<T>, comparator: Comparator<T>) {
  return runBlocking {
    mergeSortParallelInPlace(list, 0, list.size, comparator)
  }
}

private suspend fun <T> mergeSortParallelInPlace(list: MutableList<T>, start: Int, end: Int, comparator: Comparator<T>) {
  val size = end - start
  if (size <= threshold) {
    mergeSortInPlace(list, start, end, comparator)
    return
  }

  coroutineScope {
    val middle = start + (size / 2)
    launch(Dispatchers.Default) { mergeSortParallelInPlace(list, start, middle, comparator) }
    launch(Dispatchers.Default) { mergeSortParallelInPlace(list, middle, end, comparator) }
  }
  mergeInPlace(list, start, end, comparator)
}

fun <T> mergeSortInPlace(list: MutableList<T>, start: Int, end: Int, comparator: Comparator<T>) {
  val size = end - start
  if (size <= 1) {
    return
  }

  val middle = start + (size / 2)
  mergeSortInPlace(list, start, middle, comparator)
  mergeSortInPlace(list, middle, end, comparator)
  mergeInPlace(list, start, end, comparator)
}

private fun <T> mergeInPlace(list: MutableList<T>, start: Int, end: Int, comparator: Comparator<T>) {
  val size = end - start
  val middle = start + (size / 2)
  var indexLeft = 0
  var indexRight = middle
  var indexInsert = start
  val copyList = list.subList(start, middle).toList() // does this copy?

  while (indexLeft < copyList.size && indexRight < end) {
    if (comparator.compare(copyList[indexLeft], list[indexRight]) <= 0) {
      list[indexInsert] = copyList[indexLeft]
      indexLeft++
    } else {
      list[indexInsert] = list[indexRight]
      indexRight++
    }
    indexInsert++
  }

  while (indexLeft < copyList.size) {
    list[indexInsert] = copyList[indexLeft]
    indexInsert++
    indexLeft++
  }

  while (indexRight < end) {
    list[indexInsert] = list[indexRight]
    indexInsert++
    indexRight++
  }
}

// ----------------------------------------- Merge Sort: linear, copy -------------------------------------------
fun <T> runMergeSort(list: List<T>, comparator: Comparator<T>): List<T> {
  return runBlocking {
    mergeSortParallel(list, comparator)
  }
}

private suspend fun <T> mergeSortParallel(list: List<T>, comparator: Comparator<T>): List<T> {
  if (list.size <= threshold) {
    return mergeSort(list, comparator)
  }

  return coroutineScope {
    val middle = list.size / 2
    val left = async(Dispatchers.Default) { mergeSortParallel(list.subList(0, middle), comparator) }
    val right = async(Dispatchers.Default) { mergeSortParallel(list.subList(middle, list.size), comparator) }

    merge(left.await(), right.await(), comparator)
  }
}

fun <T> mergeSort(list: List<T>, comparator: Comparator<T>): List<T> {
  if (list.size <= 1) {
    return list
  }

  val middle = list.size / 2
  val left = list.subList(0, middle)
  val right = list.subList(middle, list.size)

  return merge(mergeSort(left, comparator), mergeSort(right, comparator), comparator)
}


private fun <T> merge(left: List<T>, right: List<T>, comparator: Comparator<T>): List<T> {
  var indexLeft = 0
  var indexRight = 0
  val newList: MutableList<T> = mutableListOf()

  while (indexLeft < left.count() && indexRight < right.count()) {
    if (comparator.compare(left[indexLeft], right[indexRight]) <= 0) {
      newList.add(left[indexLeft])
      indexLeft++
    } else {
      newList.add(right[indexRight])
      indexRight++
    }
  }

  while (indexLeft < left.size) {
    newList.add(left[indexLeft])
    indexLeft++
  }

  while (indexRight < right.size) {
    newList.add(right[indexRight])
    indexRight++
  }

  return newList
}

