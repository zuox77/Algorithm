package DataStructure.PrefixTree;

import java.util.HashMap;
import java.util.Map;

/*
刷题次数: 1

Coupang面经

A trie (pronounced as "try") or prefix tree is a tree data structure used to efficiently store and retrieve keys in a dataset of strings.
There are various applications of this data structure, such as autocomplete and spellchecker.
A dot ('.') can match any single character.
An asterisk（'*'）can match at least one or more characters.

Implement the Trie class:
Trie() Initializes the trie object.
void addWords(String word) Inserts the string word into the trie.
boolean search(String word) Returns true if the string word is in the trie (i.e., was inserted before), and false otherwise.

Example 1:

Trie trie = new Trie();

// Add words
trie.addWord("hello");
trie.addWord("hell");
trie.addWord("he");
trie.addWord("heat");
trie.addWord("hat");
trie.addWord("cat");
trie.addWord("car");
trie.addWord("card");

System.out.println("Trie structure:");
trie.printTrie();
System.out.println();

// Test cases
System.out.println("Search 'hello': " + trie.search("hello")); // true
System.out.println("Search 'hell': " + trie.search("hell"));   // true
System.out.println("Search 'he': " + trie.search("he"));       // true

// Test with '.' wildcard
System.out.println("Search 'h.llo': " + trie.search("h.llo")); // true (matches hello)
System.out.println("Search 'he.t': " + trie.search("he.t"));   // true (matches heat)
System.out.println("Search 'c.t': " + trie.search("c.t"));     // true (matches cat)
System.out.println("Search 'c.r': " + trie.search("c.r"));     // true (matches car)

// Test with '*' wildcard
System.out.println("Search 'he*': " + trie.search("he*"));     // true (matches he, hell, hello, heat)
System.out.println("Search 'h*': " + trie.search("h*"));       // true (matches he, hell, hello, heat, hat)
System.out.println("Search 'ca*': " + trie.search("ca*"));     // true (matches cat, car, card)
System.out.println("Search 'car*': " + trie.search("car*"));   // true (matches car, card)

// Test combinations
System.out.println("Search 'h.*': " + trie.search("h.*"));     // true
System.out.println("Search 'c.*d': " + trie.search("c.*d"));   // true (matches card)

// Test leading dot
System.out.println("Search '..t': " + trie.search("..t"));     // true

// Test leading asterisk
System.out.println("Search '*': " + trie.search("*"));     // true

// Test all dots
System.out.println("Search '...': " + trie.search("..."));   // true (matches card)

// Test all asterisks
System.out.println("Search '***': " + trie.search("***"));     // true

// Test negative cases
System.out.println("Search 'xyz': " + trie.search("xyz"));     // false
System.out.println("Search 'helo': " + trie.search("helo"));   // false
System.out.println("Search 'c.*x': " + trie.search("c.*x"));   // false

思路：
1. 主要是要知道，利用新建class TreeNode，让其每个node实例自带一个map，然后通过map去找
2. 需要一个全局变量或者说实例变量，TreeNode root，去代表根部节点，这样才能每次从根部开始搜索
 */

class ImplementTrieWithWildcards {

    static class TreeNode {
        Map<Character, TreeNode> children;
        boolean isEndOfWord;

        public TreeNode() {
            this.children = new HashMap<>();
            this.isEndOfWord = false;
        }
    }

    public static class Trie {
        private TreeNode root;

        public Trie() {
            root = new TreeNode();
        }

        public void addWord(String word) {
            TreeNode current = root;

            for (char c : word.toCharArray()) {
                current.children.putIfAbsent(c, new TreeNode());
                current = current.children.get(c);
            }
            current.isEndOfWord = true;
        }

        public boolean search(String word) {
            return searchHelper(word, 0, root);
        }

        private boolean searchHelper(String word, int index, TreeNode node) {
            // If we've reached the end of the word
            if (index == word.length()) {
                return node.isEndOfWord;
            }

            char currentChar = word.charAt(index);

            // Handle regular character
            if (currentChar != '.' && currentChar != '*') {
                if (!node.children.containsKey(currentChar)) {
                    return false;
                }
                return searchHelper(word, index + 1, node.children.get(currentChar));
            }

            // Handle '.' wildcard - matches any single character
            if (currentChar == '.') {
                for (TreeNode child : node.children.values()) {
                    if (searchHelper(word, index + 1, child)) {
                        return true;
                    }
                }
                return false;
            }

            // Handle '*' wildcard - matches one or more characters
            if (currentChar == '*') {
                // Case 1: Skip the '*' (match zero characters) - but since * must match at least
                // one,
                // we don't allow skipping. The requirement says * must match at least one
                // character.

                // Case 2: Match one or more characters
                // Try matching different numbers of characters with the current node
                for (TreeNode child : node.children.values()) {
                    // Try matching exactly one character with this child
                    if (searchHelper(word, index + 1, child)) {
                        return true;
                    }
                    // Try matching more characters by staying at current position
                    // but moving down the trie (this allows * to match multiple characters)
                    if (searchHelperWithStar(word, index, child)) {
                        return true;
                    }
                }
                return false;
            }

            return false;
        }

        private boolean searchHelperWithStar(String word, int index, TreeNode node) {
            // This helper is specifically for handling the * wildcard matching multiple characters

            // If we can complete the search from current node (after consuming characters with *)
            if (searchHelper(word, index + 1, node)) {
                return true;
            }

            // Continue matching more characters with the *
            for (TreeNode child : node.children.values()) {
                if (searchHelperWithStar(word, index, child)) {
                    return true;
                }
            }

            return false;
        }

        // Optional: Helper method for debugging
        public void printTrie() {
            printTrieHelper(root, "", 0);
        }

        private void printTrieHelper(TreeNode node, String prefix, int level) {
            if (node.isEndOfWord) {
                System.out.println("  ".repeat(level) + prefix + " (end)");
            }

            for (Map.Entry<Character, TreeNode> entry : node.children.entrySet()) {
                printTrieHelper(entry.getValue(), prefix + entry.getKey(), level + 1);
            }
        }
    }
}
