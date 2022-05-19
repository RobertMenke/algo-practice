package amazon_assessment.search

import java.util.*

//https://www.hackerrank.com/challenges/hackerland-radio-transmitters/problem
fun main() {
    val houses = arrayOf(1, 2, 3, 5, 9)
    val range = 1
    print(hackerlandRadioTransmitters(houses, range))
}

// Complete the hackerlandRadioTransmitters function below.
fun hackerlandRadioTransmitters(houses: Array<Int>, range: Int): Int {
    Arrays.sort(houses)

    return findNextInterval(houses, range)
}

tailrec fun findNextInterval(houses: Array<Int>, range: Int, index: Int = 0, numberOfTransmitters: Int = 0): Int {
    // Base case, we've exhausted the list
    if (index == houses.size) {
        return numberOfTransmitters
    }


    var currentHouseIndex = index
    // Find out the house farthest to the right that is in range of the current house
    val maximumRangeFromFirstHouse = houses[currentHouseIndex] + range
    while (currentHouseIndex < houses.size && houses[currentHouseIndex] <= maximumRangeFromFirstHouse) {
        currentHouseIndex++
    }

    // Place the transmitter (kotlin args immutable so have to redeclare)
    val totalTransmittersPlaced = numberOfTransmitters + 1

    // Find out the farthest house to the right that our new transmitter supports
    val maximumRangeRight = houses[currentHouseIndex - 1] + range
    while (currentHouseIndex < houses.size && houses[currentHouseIndex] <= maximumRangeRight) {
        currentHouseIndex++
    }

    // Place the next transmitter
    return findNextInterval(houses, range, currentHouseIndex, totalTransmittersPlaced)
}
