package amazon_assessment.heap

import java.lang.Integer.max
import java.util.*

val sampleMaxHeap = arrayListOf(16, 14, 10, 8, 7, 9, 3, 2, 4, 1)
val sampleNonMaxHeap = sampleMaxHeap.reversed() as ArrayList<Int>

class MaxHeap<T: Comparable<T>>(private val list: ArrayList<T>) {

    init {
        print()
        convertToMaxHeap()
    }

    fun print() {
        print(list)
        print("\n")
    }

    fun insert(item: T) {
        list.add(item)
        var current = list.size - 1

        while (list[current] > list[getParentIndex(current)]) {
            val parent = getParentIndex(current)
            swap(current, parent)
            current = parent
        }
    }

    fun size() = list.size

    fun poll(): T {
        val lastItem = list.removeLast()

        if (list.size == 0) {
            return lastItem
        }

        val first = list.first()

        list[0] = lastItem
        heapify(0)

        return first
    }

    fun delete(item: T) {
        val index = list.indexOf(item)

        if (index == -1) {
            return
        }

        val lastItem = list.removeLast()

        if (list.size == 0) {
            return
        }

        list[index] = lastItem
        heapify(index)
    }

    private fun convertToMaxHeap() {
        for (i in list.size / 2 - 1 downTo 0) {
            heapify(i)
        }
    }

    // Converts a single node to a max amazon_assessment.heap assuming sub-nodes are themselves max heaps
    private fun heapify(index: Int = 0) {
        var largest = index
        val leftChildIndex = leftHeapIndex(index)
        val rightChildIndex = rightHeapIndex(index)
        val leftChild = getLeftChild(index)
        val rightChild = getRightChild(index)

        if (leftChild != null && leftChild > list[largest]) {
            largest = leftChildIndex
        }

        if (rightChild != null && rightChild > list[largest]) {
            largest = rightChildIndex
        }

        if (largest != index) {
            val swap = list[index]
            list[index] = list[largest]
            list[largest] = swap
            heapify(largest)
        }
    }

    fun isMaxHeap(): Boolean {
        for (i in 0 until list.size / 2) {
            val parent = list[i]
            val leftChild = list.getOrNull(i * 2 + 1)
            val rightChild = list.getOrNull(i * 2 + 2)

            if (leftChild != null && leftChild > parent || rightChild != null && rightChild > parent) {
                return false
            }
        }

        return true
    }

    private fun swap(first: Int, second: Int) {
        val tmp = list[first]
        list[first] = list[second]
        list[second] = tmp
    }

    private fun getParentIndex(i: Int) = max(i / 2 - 1, 0)

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


fun main() {


    val maxHeap = MaxHeap(sampleNonMaxHeap)
    maxHeap.print()
//    print(sampleMaxHeap.reversed())
//    print("\n")
//    MaxHeap(sampleMaxHeap).print()
    if (maxHeap.isMaxHeap()) {
        print("Is max amazon_assessment.heap \n")
    } else {
        print("Is not max amazon_assessment.heap \n")
    }

    maxHeap.insert(20)
    maxHeap.print()

    while (maxHeap.size() > 0) {
        maxHeap.poll()
        maxHeap.print()
        if (maxHeap.isMaxHeap()) {
            print("Is max amazon_assessment.heap \n")
        } else {
            print("Is not max amazon_assessment.heap \n")
        }
    }
}
