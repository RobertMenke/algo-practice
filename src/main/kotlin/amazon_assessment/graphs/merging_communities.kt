package amazon_assessment.graphs

import java.lang.IllegalArgumentException
import java.util.*

// https://www.hackerrank.com/challenges/merging-communities/problem

fun main(args: Array<String>) {
//    val scanner = Scanner(System.`in`)
//    val line = scanner.nextLine().split(" ")
//    val graph = CommunityGraph(line[0].toInt() + 1)
//
//    while (scanner.hasNextLine()) {
//        val line = scanner.nextLine().split(" ")
//        val operation = Operation.fromChar(line[0].toCharArray()[0])
//
//        when (operation) {
//            Operation.Print -> graph.printSizeOfCommunity(line[1].toInt())
//            Operation.Merge -> graph.mergeCommunities(line[1].toInt(), line[2].toInt())
//        }
//    }

    val scanner = Scanner(System.`in`)
    val line = scanner.nextLine().split(" ")
    val graph = Community(line[0].toInt() + 1)

    while (scanner.hasNextLine()) {
        val line = scanner.nextLine().split(" ")
        val operation = Operation.fromChar(line[0].toCharArray()[0])

        when (operation) {
            Operation.Print -> graph.printSizeOfCommunity(line[1].toInt())
            Operation.Merge -> graph.mergeCommunities(line[1].toInt(), line[2].toInt())
        }
    }
}

private enum class Operation {
    Print,
    Merge;

    companion object {
        fun fromChar(char: Char): Operation {
            return when (char) {
                'Q' -> Print
                'M' -> Merge
                else -> throw IllegalArgumentException("Bad operation passed to community graph")
            }
        }
    }
}

class Person {
    private var parentIndex = -1
    // Connection to own community = 1
    private var numberOfConnections = 1

    fun incrementNumConnectionsBy(value: Int) {
        numberOfConnections += value
    }

    fun getNumberOfConnections() = numberOfConnections

    fun setParentIndex(index: Int) {
        parentIndex = index
    }

    fun getParentIndex() = parentIndex
}

class Community(private val numberOfPeople: Int) {
    private val people = Array(numberOfPeople) { Person() }

    fun printSizeOfCommunity(personVertex: Int) {
        val root = findRoot(personVertex)
        println(people[root].getNumberOfConnections())
    }

    fun mergeCommunities(first: Int, second: Int) {
        val firstRoot = findRoot(first)
        val secondRoot = findRoot(second)

        // If they don't share a root already we should merge the two sets. In this case it doesn't matter
        // which node becomes the parent node because that particular node will become the parent of every element
        // in each set.
        if (firstRoot != secondRoot) {
            people[secondRoot].setParentIndex(firstRoot)
            people[firstRoot].incrementNumConnectionsBy(people[secondRoot].getNumberOfConnections())
        }
    }

    /**
     * Find the highest ancestor and make sure all other ancestors point to the highest ancestor in the process
     */
    private fun findRoot(index: Int): Int {
        var root = index
        // First, find the highest parent node
        while (people[root].getParentIndex() != -1) {
            root = people[root].getParentIndex()
        }

        // Now, have all ancestor nodes in this chain point to the same parent
        var personIndex = index
        while (personIndex != root) {
            val nextParent = people[personIndex]
            val currentParent = nextParent.getParentIndex()
            nextParent.setParentIndex(root)
            personIndex = currentParent
        }

        return root
    }
}

////////////////////////////////////////////
// This solution was correct, but too slow
////////////////////////////////////////////

// Number of people and number of communities are initially the same
class CommunityGraph(private val numberOfPeople: Int) {
    // communities
    private val adjacencyList = Array(numberOfPeople) { LinkedList<Int>() }

    fun printSizeOfCommunity(personVertex: Int) {
        var sizeOfCommunity = 0
        val visited = BooleanArray(numberOfPeople) { false }
        depthFirstSearch(personVertex, visited) {
            sizeOfCommunity++
        }

        println(sizeOfCommunity)
    }

    // Add edges
    fun mergeCommunities(firstVertex: Int, secondVertex: Int) {
        adjacencyList[firstVertex].add(secondVertex)
        adjacencyList[secondVertex].add(firstVertex)
    }

    private fun depthFirstSearch(vertex: Int, visited: BooleanArray, f: (Int) -> Unit) {
        visited[vertex] = true
        f(vertex)

        val iterator = adjacencyList[vertex].iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (!visited[next]) {
                depthFirstSearch(next, visited, f)
            }
        }
    }
}
