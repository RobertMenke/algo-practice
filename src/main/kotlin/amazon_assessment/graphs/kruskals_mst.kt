package amazon_assessment.graphs

import java.util.*
import kotlin.collections.ArrayList

data class Edge(val source: Int, val destination: Int, val weight: Int): Comparable<Edge> {
    override fun compareTo(other: Edge): Int {
        return weight - other.weight
    }
}

class Subset(var parent: Int, var rank: Int)

class KruskalsGraph(private val numberOfVertices: Int) {
    private val edges: PriorityQueue<Edge> = PriorityQueue()
    // Each vertex starts out as its own subset in the set
    private val subsets: Array<Subset> = Array(numberOfVertices + 1) { index -> Subset(index, 0)}

    fun addEdge(edge: Edge) {
        edges.add(edge)
    }

    fun createMinimumSpanningTree(): ArrayList<Edge> {
        val minimumSpanningTree: ArrayList<Edge> = arrayListOf()

        while (edges.size > 0) {
            val smallestEdge = edges.poll()
            val smallestSourceRoot = findParentVertex(smallestEdge.source)
            val smallestDestinationRoot = findParentVertex(smallestEdge.destination)

            // If the roots are the same, this means that this edge would create a cycle
            if (smallestSourceRoot != smallestDestinationRoot) {
                minimumSpanningTree.add(smallestEdge)
                // Update our subsets based on this new connection
                union(smallestSourceRoot, smallestDestinationRoot)
            }
        }

        return minimumSpanningTree
    }

    /**
     * Find the parent of the given vertex
     */
    private fun findParentVertex(vertex: Int): Int {
        if (subsets[vertex].parent != vertex) {
            subsets[vertex].parent = findParentVertex(subsets[vertex].parent)
        }

        return subsets[vertex].parent
    }

    private fun union(source: Int, destination: Int) {
        val sourceRoot = findParentVertex(source)
        val destinationRoot = findParentVertex(destination)

        if (subsets[sourceRoot].rank > subsets[destinationRoot].rank) {
            subsets[destinationRoot].parent = sourceRoot
        } else if (subsets[destinationRoot].rank > subsets[sourceRoot].rank) {
            subsets[sourceRoot].parent = destinationRoot
        } else {
            subsets[destinationRoot].parent = sourceRoot
            subsets[sourceRoot].rank++
        }
    }
}
