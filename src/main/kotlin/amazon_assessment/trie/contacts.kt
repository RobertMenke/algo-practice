package amazon_assessment.trie

import java.util.*

// https://www.hackerrank.com/challenges/contacts/problem
fun main() {
    val scanner = Scanner(System.`in`)
    val numQueries = Integer.parseInt(scanner.nextLine())
    var numLinesRead = 0
    val trieNode = TrieNode('_')
    while (scanner.hasNextLine() && numLinesRead <= numQueries) {
        val (operation, word) = scanner.nextLine().split(" ")
        if (operation == "add") {
            trieNode.addWord(word)
        } else {
            val count = trieNode.findCountOfPartialMatches(word)
            println(count)
        }
        numLinesRead++
    }
}

class TrieNode(private val character: Char) {

    private var numberOfMatchingWordsInPath: Int = 0
    private val children = hashMapOf<Char, TrieNode>()

    fun addWord(text: String, index: Int = 0) {
        numberOfMatchingWordsInPath++
        if (index == text.length) {
            return
        }

        val currentCharacter = text[index]
        var child = getChild(currentCharacter)

        if (child == null) {
            child = TrieNode(currentCharacter)
            addChild(currentCharacter, child)
        }

        child.addWord(text, index + 1)
    }

    fun findCountOfPartialMatches(text: String, index: Int = 0): Int {
        if (index == text.length) {
            return numberOfMatchingWordsInPath
        }

        val child = getChild(text[index])

        if (child == null) {
            return 0
        }

        return child.findCountOfPartialMatches(text, index + 1)
    }

    private fun addChild(char: Char, node: TrieNode) {
        if (children[char] == null) {
            children[char] = node
        }
    }

    private fun getChild(char: Char) = children[char]
}
