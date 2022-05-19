package amazon_assessment.graphs

import java.util.*

// https://www.hackerrank.com/challenges/kundu-and-tree/problem
fun main() {
    val max = 1_000_000_007L
    val scanner = Scanner(System.`in`)
    val numberOfSets = scanner.nextLine().toInt()
    val tree = KundusTree(numberOfSets)

    while (scanner.hasNextLine()) {
        val (one, two, color) = scanner.nextLine().split(" ")
        // ignore red edges
        if (color == "r") {
            continue
        }

        tree.addSet(one.toInt(), two.toInt())
    }
    var possibleTriplets = tree.findAllTuples(3)
    if (possibleTriplets > max) {
        possibleTriplets %= max
    }

    println(possibleTriplets)
}

class KundusTree(private val numberOfSets: Int) {
    private val sets: Array<DisjointSet?> = Array(numberOfSets + 1) { null }

    fun addSet(vertexOne: Int, vertexTwo: Int) {
        val setOne = sets[vertexOne] ?: DisjointSet()
        val setTwo = sets[vertexTwo] ?: DisjointSet()

        setOne.union(setTwo)
        sets[vertexOne] = setOne
        sets[vertexTwo] = setTwo
    }

    fun findAllTuples(tupleSize: Int): Long {
        // Maximum possible tuples
        var validTuples = binomial(numberOfSets, tupleSize)
        val uniqueParents = findUniqueParents()

        for (parent in uniqueParents) {
            // Subtract the max # of tuples from this set
            val maxTripletsInSet = binomial(parent.getSizeOfSet(), tupleSize)
            validTuples -= maxTripletsInSet
            val maxPairsInSet = binomial(parent.getSizeOfSet(), tupleSize - 1) * (numberOfSets - parent.getSizeOfSet())
            // Subtract all tuples built from n - 1 of the same vertices + 1 unique vertex
            validTuples -= maxPairsInSet
        }

        return validTuples
    }

    private fun findUniqueParents(): HashSet<DisjointSet> {
        return sets
            .mapNotNull { it?.findRoot() }
            .toHashSet()
    }

    private fun binomial(n: Int, k: Int) = when {
        n < 0 || k < 0 -> throw IllegalArgumentException("negative numbers not allowed")
        n == k         -> 1L
        n < k          -> 0
        else           -> {
            val kReduced = Math.min(k, n - k)    // minimize number of steps
            var result = 1L
            var numerator = n
            var denominator = 1
            while (denominator <= kReduced)
                result = result * numerator-- / denominator++
            result
        }
    }
}

class DisjointSet {
    private var parent = this
    private var sizeOfSet = 1

    fun getSizeOfSet() = sizeOfSet

    fun findRoot(): DisjointSet {
        if (parent == this) {
            return this
        }

        return parent.findRoot()
    }

    fun union(otherSet: DisjointSet) {
        val root = findRoot()
        val otherRoot = otherSet.findRoot()

        if (root == otherRoot) {
            return
        }

        if (root.sizeOfSet >= otherRoot.sizeOfSet) {
            otherRoot.parent = root
            root.sizeOfSet += otherRoot.sizeOfSet
        } else {
            root.parent = otherRoot
            otherRoot.sizeOfSet += root.sizeOfSet
        }
    }
}
