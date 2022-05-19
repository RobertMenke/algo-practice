package amazon_assessment.huffman_coding

import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.HashMap

fun main() {
    val text = "ABRACADABRA"
    val queue = enqueue(text)
    val tree = buildHuffmanTree(queue)
    printHuffmanEncoding(tree)
}

class HuffmanNode(
    val frequency: Int,
    val character: Char?,
    var left: HuffmanNode? = null,
    var right: HuffmanNode? = null
): Comparable<HuffmanNode> {
    override fun compareTo(other: HuffmanNode): Int {
        val difference = frequency - other.frequency

        if (difference != 0) {
            return difference
        }

        if (character != null && other.character != null) {
            return character.compareTo(other.character)
        }

        if (character == null) {
            return -1
        }

        return 1
    }
}

fun HuffmanNode.isLeaf() = left == null && right == null && character != null

fun decode(root: HuffmanNode, encoded: String): String {
    var output = ""
    var currentNode: HuffmanNode? = root
    encoded.forEachIndexed { index, c ->
        currentNode = if (c == '0') {
            currentNode?.left
        } else {
            currentNode?.right
        }

        currentNode?.character?.let { character ->
            output += character
            currentNode = root
        }
    }

    return output
}

fun printHuffmanEncoding(rootNode: HuffmanNode, encoding: String = "") {
    if (rootNode.left == null && rootNode.right == null && rootNode.character != null) {
        print("Char ${rootNode.character} encoded as $encoding \n")
        return
    }

    // traverse left
    rootNode.left?.let { left -> printHuffmanEncoding(left, "${encoding}0")}
    //traverse right
    rootNode.right?.let { right -> printHuffmanEncoding(right, "${encoding}1")}
}

private data class NodeEncoding(val node: HuffmanNode, val encoding: String)

fun buildEncodingTable(tree: HuffmanNode): Map<Char, String> {
    val queue = LinkedList<NodeEncoding>()
    queue.add(NodeEncoding(tree, ""))
    val map: HashMap<Char, String> = HashMap()

    while (!queue.isEmpty()) {
        val (node, encoding) = queue.poll()

        if (node.left == null && node.right == null && node.character != null) {
            map[node.character] = encoding
            continue
        }

        val leftEncoding = "${encoding}0"
        val rightEncoding = "${encoding}1"
        node.left?.let { left -> queue.add(NodeEncoding(left, leftEncoding)) }
        node.right?.let { right -> queue.add(NodeEncoding(right, rightEncoding)) }
    }

    return map
}

@Throws(IllegalArgumentException::class)
fun buildHuffmanTree(characters: PriorityQueue<HuffmanNode>): HuffmanNode {
    var rootNode: HuffmanNode? = null

    while (characters.size > 1) {
        val leastFrequent = characters.poll()
        val secondLeastFrequent = characters.poll()
        rootNode = HuffmanNode(leastFrequent.frequency + secondLeastFrequent.frequency, null, leastFrequent, secondLeastFrequent)
        characters.add(rootNode)
    }

    if (rootNode == null) {
        throw IllegalArgumentException("Invalid priority queue sent to encode method for Huffman Encoding")
    }

    return rootNode
}

// Converts a sequence of text to a PriorityQueue of HuffmanCoding leaf nodes
fun enqueue(text: String): PriorityQueue<HuffmanNode> {
    val map: HashMap<Char, Int> = HashMap()
    // Set up the frequency mapping O(n)
    text.toCharArray().forEach { char ->
        map[char] = map[char]?.plus(1) ?: 1
    }

    // Create leaf nodes and add them to the priority queue
    val queue = PriorityQueue<HuffmanNode>()
    for ((key, value) in map) {
        queue.add(HuffmanNode(value, key))
    }

    return queue
}
