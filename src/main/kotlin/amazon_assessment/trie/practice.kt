package amazon_assessment.trie

import kotlin.collections.HashMap

class TrieNodePractice(private val character: Char) {

    private var size: Int = 0
    private val children: HashMap<Char, TrieNodePractice> = hashMapOf()

    fun addChild(char: Char, node: TrieNodePractice) {
        if (children[char] == null) {
            children[char] = node
        }
    }

    fun getChild(char: Char) = children[char]

    fun add(text: String, index: Int = 0) {
        size++
        if (index == text.length) {
            return
        }

        val currentCharacter = text[index]
        var child = getChild(currentCharacter)

        if (child == null) {
            child = TrieNodePractice(currentCharacter)
            addChild(currentCharacter, child)
        }

        child.add(text, index + 1)
    }

    fun findCountOfPartialMatches(text: String, index: Int): Int {
        if (index == text.length) {
            return size
        }

        val child = getChild(text[index])

        if (child == null) {
            return 0
        }

        return child.findCountOfPartialMatches(text, index + 1)
    }
}
