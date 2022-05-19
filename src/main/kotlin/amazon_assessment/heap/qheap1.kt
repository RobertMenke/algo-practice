package amazon_assessment.heap

import java.lang.Error
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList

class InvalidOperationError(message: String): Error(message) {}

enum class Operation(int: Int) {
    Add(1),
    Delete(2),
    Print(3);

    companion object {
        @Throws(InvalidOperationError::class)
        fun fromInt(int: Int): Operation {
            if (int == 1) {
                return Add
            }

            if (int == 2) {
                return Delete
            }

            if (int == 3) {
                return Print
            }

            throw InvalidOperationError("Invalid integer $int for operation")
        }
    }
}

// https://www.hackerrank.com/challenges/qheap1/problem
fun main(args: Array<String>) {
    try {
        val scanner = Scanner(System.`in`)
        val numQueries = Integer.parseInt(scanner.nextLine())
        val minHeap = MinHeap(arrayListOf<Int>())
        var numLinesRead = 0

        while (scanner.hasNextLine() && numLinesRead <= numQueries) {
            val line = scanner.nextLine()
            val items = line.split(" ")
            val operation = Operation.fromInt(items[0].toInt())

            when (operation) {
                Operation.Add -> minHeap.insert(items[1].toInt())
                Operation.Delete -> minHeap.delete(items[1].toInt())
                Operation.Print -> println(minHeap.peek())
            }

            numLinesRead++
        }
    } catch (e: Error) {
        e.printStackTrace()
    }
}

fun <T> ArrayList<T>.removeLast() = this.removeAt(size - 1)

class MinHeap<T: Comparable<T>>(private val list: ArrayList<T>) {

    init {
        convertToMinHeap()
    }

    fun print() {
        print(list)
        print("\n")
    }

    fun insert(item: T) {
        list.add(item)
        var current = list.size - 1

        while (list[current] < list[getParentIndex(current)]) {
            val parent = getParentIndex(current)
            swap(current, parent)
            current = parent
        }
    }

    fun size() = list.size

    fun peek(): T {
        return list[0]
    }

    fun poll(): T {
        val lastItem = list.removeLast()

        if (list.size == 0) {
            return lastItem
        }

        val first = list.first()

        list[0] = lastItem
        siftDown(0)

        return first
    }

    fun delete(item: T): T? {
        val index = list.indexOf(item)

        if (index == -1) {
            return null
        }

        if (list.last() == item) {
            list.removeLast()
            return item
        }

        list[index] = list.last()
        list.removeLast()

        val parent = getParentIndex(index)
        if (index == 0 || list[parent] < list[index]) {
            siftDown(index)
        } else {
            siftUp(index)
        }

        return item
    }

    private fun convertToMinHeap() {
        for (i in list.size / 2 - 1 downTo 0) {
            siftDown(i)
        }
    }

    fun isMinHeap(): Boolean {
        for (i in 0 until list.size / 2) {
            val parent = list[i]
            val leftChild = list.getOrNull(i * 2 + 1)
            val rightChild = list.getOrNull(i * 2 + 2)

            if (leftChild != null && leftChild < parent || rightChild != null && rightChild < parent) {
                return false
            }
        }

        return true
    }

    private fun siftUp(i: Int) {
        var index = i
        while (getParentIndex(index) >= 0 && list[getParentIndex(index)] > list[index]) {
            swap(getParentIndex(index), index)
            index = getParentIndex(index)
        }
    }

    private fun siftDown(i: Int) {
        var index = i
        var leftChild = getLeftChild(index)
        while (leftChild != null) {
            var smallerChildIndex = leftHeapIndex(index)
            val rightChild = getRightChild(index)
            if (rightChild != null && rightChild < leftChild) {
                smallerChildIndex = rightHeapIndex(index)
            }

            if (list.get(index) < list.get(smallerChildIndex)) {
                break
            } else {
                swap(index, smallerChildIndex)
            }

            index = smallerChildIndex
            leftChild = getLeftChild(index)
        }
    }

    private fun swap(first: Int, second: Int) {
        val tmp = list[first]
        list[first] = list[second]
        list[second] = tmp
    }

    private fun getParentIndex(i: Int) = Integer.max((i - 1 ) / 2, 0)

    /**
     * Helper for left child of a given root node
     */
    private fun getLeftChild(rootIndex: Int): T? = list.getOrNull(leftHeapIndex(rootIndex))

    /**
     * Helper for right child of a given root node
     */
    private fun getRightChild(rootIndex: Int): T? = list.getOrNull(rightHeapIndex(rootIndex))

    /**
     * Helper for the left child index of a given root index
     */
    private fun leftHeapIndex(rootIndex: Int): Int = rootIndex * 2 + 1

    /**
     * Helper for the right child index of a given root index
     */
    private fun rightHeapIndex(rootIndex: Int): Int = rootIndex * 2 + 2
}

// Actually like this implementation better
class MinHeapPractice<T: Comparable<T>>(private val list: ArrayList<T>) {

    // amazon_assessment.heap invariants
    private fun getLeftChildIndex(index: Int) = index * 2 + 1
    private fun getRightChildIndex(index: Int) = index * 2 + 2
    private fun getParentIndex(index: Int) = (index - 1) / 2
    private fun getLeftChild(index: Int) = list[getLeftChildIndex(index)]
    private fun getRightChild(index: Int) = list[getRightChildIndex(index)]
    private fun getParent(index: Int) = list[getParentIndex(index)]
    private fun hasParent(index: Int) = getParentIndex(index) >= 0
    private fun hasLeftChild(index: Int) = getLeftChildIndex(index) < list.size
    private fun hasRightChild(index: Int) = getRightChildIndex(index) < list.size
    private fun lessThanRightChild(index: Int) = hasRightChild(index) && list[index] < getRightChild(index)
    private fun lessThanLeftChild(index: Int) = hasLeftChild(index) && list[index] < getLeftChild(index)

    /**
     * Remove the first item from the amazon_assessment.heap.
     * Methodology: place the last item in the array in the first position and sift down until
     * the amazon_assessment.heap invariant is restored.
     */
    @Throws(IllegalStateException::class)
    fun poll(): T {
        if (list.size == 0) {
            throw IllegalStateException("Queue is empty")
        }

        if (list.size == 1) {
            return list.removeLast()
        }

        val first = list[0]
        val last = list.last()

        list[0] = last
        list.removeLast()

        siftDown(0)

        return first
    }

    @Throws(IllegalArgumentException::class)
    fun deleteItem(item: T): T {
        val index = list.indexOf(item)

        if (index == -1) {
            throw IllegalArgumentException("Item does not exist in amazon_assessment.heap")
        }

        val last = list.last()
        if (item == last) {
            return list.removeLast()
        }

        val current = list[index]
        list[index] = last
        list.removeLast()

        if (hasParent(index) && getParent(index) > list[index]) {
            siftUp(index)
        } else {
            siftDown(index)
        }

        return current
    }

    fun insert(item: T) {
        list.add(item)

        siftUp(list.size - 1)
    }

    fun siftDown(index: Int) {
        var currentIndex = index

        while (!lessThanLeftChild(currentIndex) || !lessThanRightChild(currentIndex)) {
            var smallest = getLeftChildIndex(currentIndex)

            if (hasRightChild(currentIndex) && getRightChild(currentIndex) < getLeftChild(currentIndex)) {
                smallest = getRightChildIndex(currentIndex)
            }

            swap(currentIndex, smallest)
            currentIndex = smallest
        }
    }

    fun siftUp(index: Int) {
        var currentIndex = index

        while (hasParent(currentIndex) && getParent(currentIndex) > list[currentIndex]) {
            swap(currentIndex, getParentIndex(currentIndex))
            currentIndex = getParentIndex(currentIndex)
        }
    }

    fun swap(first: Int, second: Int) {
        val tmp = list[first]
        list[first] = list[second]
        list[second] = tmp
    }
}
