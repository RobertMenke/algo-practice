package amazon_assessment.graphs

import java.util.LinkedList




// https://www.hackerrank.com/challenges/components-in-graph/problem
/*
 * Complete the 'componentsInGraph' function below.
 *
 * The function is expected to return an INTEGER_ARRAY.
 * The function accepts 2D_INTEGER_ARRAY gb as parameter.
 */

fun main() {
    val input = arrayOf(
        arrayOf(1, 6),
        arrayOf(2, 7),
        arrayOf(3, 8),
        arrayOf(4, 9),
        arrayOf(2, 6)
    )
//    val input = arrayOf(
//        arrayOf(1, 17),
//        arrayOf(5, 13),
//        arrayOf(7, 12),
//        arrayOf(5, 17),
//        arrayOf(5, 12),
//        arrayOf(2, 17),
//        arrayOf(1, 18),
//        arrayOf(8, 13),
//        arrayOf(2, 15),
//        arrayOf(5, 20),
//    )

    val graph = UnDirectedGraph((2 * input.size) + 1)
    input.forEach { list ->
        graph.addEdge(list[0], list[1])
    }

    val (min, max) = graph.findMinAndMax()
//    val answer = componentsInGraph(input)
    print("$min $max")
}

class UnDirectedGraph(private val numberOfVertices: Int) {
    private val adjacenyList: Array<LinkedList<Int>> = Array(numberOfVertices) { LinkedList() }

    fun addEdge(beginning: Int, end: Int) {
        adjacenyList[beginning].add(end)
        adjacenyList[end].add(beginning)
    }

    private fun depthFirstSearch(vertex: Int, visited: BooleanArray, f: (Int) -> Unit) {
        visited[vertex] = true
        f(vertex)
        val adjacencyIterator = adjacenyList[vertex].iterator()
        while (adjacencyIterator.hasNext()) {
            val nextVertex = adjacencyIterator.next()
            if (!visited[nextVertex]) {
                depthFirstSearch(nextVertex, visited, f)
            }
        }
    }

    fun findMinAndMax(): Array<Int> {
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE
        val visited = BooleanArray(numberOfVertices) { false }

        for (vertex in 0 until numberOfVertices) {
            if (!visited[vertex]) {
                var connections = 0
                depthFirstSearch(vertex, visited) {
                    connections++
                }

                if (connections < min && connections > 1) {
                    min = connections
                }

                if (connections > max && connections > 1) {
                    max = connections
                }
            }
        }

        return arrayOf(min, max)
    }
}



////////////////////////////////////////////////
// Initial solution (correct but too slow)
////////////////////////////////////////////////

fun componentsInGraph(gb: Array<Array<Int>>): Array<Int> {
    // Write your code here
    val cache = hashMapOf<Int, Node<Int>>()
    val rootNodes = mutableListOf<Node<Int>>()
    // Build the graph
    gb.forEach { list ->
        val first = cache[list[0]] ?: Node(list[0])
        val second = cache[list[1]] ?: Node(list[1])
        first.addConnection(second)
        second.addConnection(first)
        cache[list[0]] = first
        cache[list[1]] = second
        rootNodes.add(first)
    }

    var fewestConnections = Integer.MAX_VALUE
    var mostConnections = 0

    for (value in rootNodes) {
        val numberOfConnections = findNumberOfConnections(value)

        if (numberOfConnections < fewestConnections) {
            fewestConnections = numberOfConnections
        }

        if (numberOfConnections > mostConnections) {
            mostConnections = numberOfConnections
        }
    }

    return arrayOf(fewestConnections, mostConnections)
}

fun <T> findNumberOfConnections(node: Node<T>): Int {
    val queue = LinkedList<Node<T>>()
    queue.add(node)
    var numberOfConnections = 0
    val visited = hashMapOf<T, Int>()

    while (queue.size > 0) {
        val node = queue.poll()

        if (visited.containsKey(node.getValue())) {
            continue
        } else {
            visited[node.getValue()] = 1
        }

        numberOfConnections++

        node.getConnections().forEach { connectedNode ->
            queue.add(connectedNode)
        }
    }

    return numberOfConnections
}

data class Node<T>(private val value: T) {
    private val connections = hashMapOf<T, Node<T>>()

    fun getValue() = value
    fun isConnected(to: T) = connections.containsKey(to)
    fun addConnection(node: Node<T>) {
        connections[node.getValue()] = node
    }

    fun getNumberOfConnections() = connections.keys.size
    fun getConnections() = connections.values
}
