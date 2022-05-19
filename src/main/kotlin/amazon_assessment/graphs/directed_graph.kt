package amazon_assessment.graphs

import java.util.*

// https://www.geeksforgeeks.org/strongly-connected-components/
class DirectedGraph(private val numberOfVertices: Int) {
    private val adjacenyList: Array<LinkedList<Int>> = Array(numberOfVertices) { LinkedList() }

    fun addEdge(beginning: Int, end: Int) {
        adjacenyList[beginning].add(end)
    }

    fun printAdjacencyList() {
        print(adjacenyList)
    }

    private fun getTranspose(): DirectedGraph {
        val graph = DirectedGraph(numberOfVertices)

        for (vertex in 0 until numberOfVertices) {
            val iterator = adjacenyList[vertex].iterator()
            while (iterator.hasNext()) {
                graph.addEdge(iterator.next(), vertex)
            }
        }

        return graph
    }

    private fun depthFirstSearch(vertex: Int, visited: BooleanArray, f: (Int) -> Unit) {
        visited[vertex] = true
        f(vertex)
        val foo = adjacenyList[vertex]
        val adjacencyIterator = adjacenyList[vertex].iterator()
        while (adjacencyIterator.hasNext()) {
            val nextVertex = adjacencyIterator.next()
            if (!visited[nextVertex]) {
                depthFirstSearch(nextVertex, visited, f)
            }
        }
    }

    private fun createVertexSearchStack(vertex: Int, visited: BooleanArray, stack: Stack<Int>) {
        visited[vertex] = true

        val iterator = adjacenyList[vertex].iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (!visited[next]) {
                createVertexSearchStack(next, visited, stack)
            }
        }

        // All vertices reachable from v are processed by now,
        // push v to Stack
        stack.push(vertex)
    }

    fun getStronglyConnectedComponents() {
        val stack = Stack<Int>()

        val visited = BooleanArray(numberOfVertices)
        for (i in 0 until numberOfVertices) {
            visited[i] = false
        }

        for (i in 0 until numberOfVertices) {
            if (!visited[i]) {
                createVertexSearchStack(i, visited, stack)
            }
        }

        val tranposedGraph = getTranspose()

        for (vertex in 0 until numberOfVertices) {
            visited[vertex] = false
        }

        while (!stack.empty()) {
            val vertex = stack.pop()
            if (!visited[vertex]) {
                var connections = 0
                tranposedGraph.depthFirstSearch(vertex, visited) { item ->
                    connections++
                }
                println("num connections $connections")
            }
        }
    }
}
