package amazon_assessment.avl_tree

import java.lang.IllegalStateException
import kotlin.math.max

data class Node<Data: Comparable<Data>>(
    var value: Data,
    var height: Int = 0,
    var left: Node<Data>? = null,
    var right: Node<Data>? = null
) {
    fun getBalance() = getLeftHeight() - getRightHeight()
    fun getLeftHeight() = left?.height ?: 0
    fun getRightHeight() = right?.height ?: 0

    fun leftRotate(): Node<Data> {
        val currentRightChild = right
        val currentRightChildLeft = right?.left

        if (currentRightChild == null) {
            return this
        }

        currentRightChild.left = this
        right = currentRightChildLeft

        height = computeHeight(this)
        currentRightChild.height = computeHeight(currentRightChild)

        return currentRightChild
    }

    fun rightRotate(): Node<Data> {
        val currentLeftNode = left
        val currentLeftChildRight = left?.right

        if (currentLeftNode == null) {
            return this
        }

        currentLeftNode.right = this
        left = currentLeftChildRight

        height = computeHeight(this)
        currentLeftNode.height = computeHeight(currentLeftNode)

        return currentLeftNode
    }
}

fun <T: Comparable<T>> computeHeight(node: Node<T>?): Int {
    if (node == null) {
        return -1
    }

    return max(getHeight(node.left), getHeight(node.right)) + 1
}

/**
 * Node height is equal to max(height(left), height(right))
 */
fun <T: Comparable<T>> getHeight(node: Node<T>?): Int {
    if (node == null) {
        return -1
    }

    return node.height
}

fun <T: Comparable<T>> insert(node: Node<T>?, value: T): Node<T> {
    if (node == null) {
        return Node(value)
    }

    // Recursively perform the insertion. Keep in mind as the call stack unwinds it will resume
    // 1 node up from the new leaf node.
    if (value < node.value) {
        node.left = insert(node.left, value)
    } else {
        node.right = insert(node.right, value)
    }

    // Next up is computing the height and the balance
    node.height = computeHeight(node)
    val balance = node.getBalance()
    val left = node.left
    val right = node.right

    // There are now 4 possible rotation cases to handle
    if (right != null) {
        // left rotation case
        if (balance < -1 && value > right.value) {
            return node.leftRotate()
        }

        // right-left rotation case
        if (balance < -1 && value < right.value) {
            node.right = node.right?.rightRotate()
            return node.leftRotate()
        }
    }

    if (left != null) {
        // right rotation case
        if (balance > 1 && value < left.value) {
            return node.rightRotate()
        }

        // left-right rotation case
        if (balance > 1 && value > left.value) {
            node.left = node.left?.leftRotate()
            return node.rightRotate()
        }
    }

    return node
}

fun <T: Comparable<T>> delete(parentNode: Node<T>?, node: Node<T>?, valueToDelete: T): Node<T>? {
    if (node == null) {
        return node
    }

    if (valueToDelete < node.value) {
        node.left = delete(node, node.left, valueToDelete)
    }
    else if (valueToDelete > node.value) {
        node.right = delete(node, node.right, valueToDelete)
    }
    else {
        // First case: Node with only 1 or no children
        if (node.left == null || node.right == null) {
            val child = if (node.left != null) node.left else node.right
            // no child case
            if (child == null) {
                deleteImmediateChild(parentNode, valueToDelete)
            }
            // 1-child case
            else {
                node.value = child.value
                deleteImmediateChild(node, child.value)
            }
        }
        // Second case: node to delete has 2 children. Replace the current node with the lowest value on the right hand side and then
        // delete the lowest node on the right hand side.
        else {
            val right = node.right ?: throw IllegalStateException("Tried to delete a node with 2 children but did not find a right hand side node")
            val successor = getMinimumValueNode(right)
            node.value = successor.value
            node.right = delete(node, node.right, successor.value)
        }
    }

    node.height = computeHeight(node)
    val balance = node.getBalance()

    // If this node becomes unbalanced, then there are 4 cases
    // Key difference here is that if the we have to check the balance of the nodes below us
    // rather than relying on the value of the current node like we do in the insertion case
    // Left Left Case
    if (balance > 1 && getBalance(node.left) >= 0)
        return node.rightRotate()

    // Left Right Case
    if (balance > 1 && getBalance(node.left) < 0) {
        node.left =  node.left?.leftRotate()
        return node.rightRotate()
    }

    // Right Right Case
    if (balance < -1 && getBalance(node.right) <= 0)
        return node.leftRotate()

    // Right Left Case
    if (balance < -1 && getBalance(node.right) > 0) {
        node.right = node.right?.rightRotate()
        return node.leftRotate()
    }

    return null
}

fun <T: Comparable<T>> deleteImmediateChild(parentNode: Node<T>?, value: T) {
    parentNode?.let { parent ->
        if (parent.left?.value == value) {
            parent.left = null
        }

        if (parent.right?.value == value) {
            parent.right = null
        }
    }
}

fun <T: Comparable<T>> getMinimumValueNode(node: Node<T>): Node<T> {
    var current = node

    while (current.left != null) {
        val left = current.left
        if (left != null) {
            current = left
        }
    }

    return current
}

fun <T: Comparable<T>> getBalance(node: Node<T>?): Int {
    return getHeight(node?.left) - getHeight(node?.right)
}
