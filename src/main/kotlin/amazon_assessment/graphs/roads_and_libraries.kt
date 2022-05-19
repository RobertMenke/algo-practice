package amazon_assessment.graphs

import java.util.*

fun main() {
    val numCities = 9
    val libraryCost = 91
    val roadCost = 84
    val cities = arrayOf(
        arrayOf(8, 2),
        arrayOf(2, 9)
    )

    print(roadsAndLibraries(numCities, libraryCost, roadCost, cities))
}

// https://www.hackerrank.com/challenges/torque-and-development/problem?isFullScreen=true
fun roadsAndLibraries(n: Int, c_lib: Int, c_road: Int, cities: Array<Array<Int>>): Long {
    val planner = CityPlanner(n, c_road.toLong(), c_lib.toLong())
    for (city in cities) {
        planner.addCity(city[0], city[1])
    }

    return planner.computeCost()
}


class CityPlanner(private val numberOfCities: Int, private val costOfRoad: Long, private val costOfLibrary: Long) {
    private val adjacencyList: Array<LinkedList<Int>> = Array(numberOfCities + 1) { LinkedList<Int>() }
    private var numCitiesVisited = 0

    fun addCity(one: Int, two: Int) {
        adjacencyList[one].add(two)
        adjacencyList[two].add(one)
    }

    fun computeCost(): Long {
        if (costOfRoad >= costOfLibrary) {
            return costOfLibrary * numberOfCities.toLong()
        }

        val visited = BooleanArray(numberOfCities + 1) { false }
        var totalCost = 0L
        for (index in adjacencyList.indices) {
            if (adjacencyList[index].isNotEmpty() && !visited[index]) {
                totalCost += computeCostAtConnectedNetwork(index, visited) - costOfRoad
            }
        }

        val numberOfIsolatedCities = numberOfCities - numCitiesVisited
        totalCost += numberOfIsolatedCities * costOfLibrary

        return totalCost
    }

    /**
     * For a given connected network of cities determine whether it's cheaper to build a system of roads
     * or whether it's cheaper to put a library in every city
     */
    private fun computeCostAtConnectedNetwork(vertex: Int, visited: BooleanArray, cost: Long = costOfLibrary): Long {
        visited[vertex] = true
        numCitiesVisited++
        var nextCost = cost + costOfRoad
        val iterator = adjacencyList[vertex].iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (!visited[next]) {
                nextCost = computeCostAtConnectedNetwork(next, visited, nextCost)
            }
        }

        return nextCost
    }
}
