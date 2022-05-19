package problem_of_the_week.trees

class IntNode(
    val value: Int,
    val level: Int,
    var left: IntNode? = null,
    var right: IntNode? = null
)

// https://www.hackerrank.com/challenges/swap-nodes-algo/problem
fun main() {
    val indexes: Array<Array<Int>> = arrayOf(
        arrayOf(2, 3),
        arrayOf(-1, 4),
        arrayOf(-1, 5),
        arrayOf(-1, -1),
        arrayOf(-1, -1)
    )
    val queries: Array<Int> = arrayOf(2)
    val nodes = swapNodes(indexes, queries)

    print(nodes)
}

fun swapNodes(indexes: Array<Array<Int>>, queries: Array<Int>): Array<Array<Int>> {
    val binaryTree = buildBinaryTree(indexes)
    val finalList: ArrayList<Array<Int>> = arrayListOf()
    queries.forEach { query ->
        swapNodes(binaryTree, query)
        val list = collectTreeInOrder(binaryTree).map { it.value }
        finalList.add(list.toTypedArray())
    }

    return finalList.toTypedArray()
}

fun swapNodes(node: IntNode? = null, k: Int) {
    if (node == null) {
        return
    }

    if (node.left != null) {
        swapNodes(node.left, k)
    }

    if (node.right != null) {
        swapNodes(node.right, k)
    }

    // Any multiple of K needs to be swapped
    if (node.level % k == 0) {
        performSwap(node)
    }
}

private fun performSwap(node: IntNode) {
    val left = node.left
    val right = node.right

    node.left = right
    node.right = left
}

fun buildBinaryTree(indexes: Array<Array<Int>>): IntNode {
    // Tree is always rooted with a 1
    val nodeList = mutableListOf(IntNode(1, 1))

    indexes.forEachIndexed { index, values ->
        val parent = nodeList[index]
        val left = if (values[0] > -1) IntNode(values[0], parent.level + 1) else null
        val right = if (values[1] > -1) IntNode(values[1], parent.level + 1) else null

        if (left != null) {
            parent.left = left
            nodeList.add(left)
        }

        if (right != null) {
            parent.right = right
            nodeList.add(right)
        }
    }

    return nodeList[0]
}

fun collectTreeInOrder(node: IntNode?, list: ArrayList<IntNode> = arrayListOf()): ArrayList<IntNode> {
    if (node == null) {
        return list
    }

    if (node.left != null) {
        collectTreeInOrder(node.left, list)
    }

    list.add(node)

    if (node.right != null) {
        collectTreeInOrder(node.right, list)
    }

    return list
}

fun printTreeInOrder(node: IntNode?) {
    if (node == null) {
        return
    }

    if (node.left != null) {
        printTreeInOrder(node.left)
    }

    print(node.value)

    if (node.right != null) {
        printTreeInOrder(node.right)
    }
}
