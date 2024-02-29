# Java Binary Search Tree

A simple Java implementation of a BST. Example of usage:

```java
// Create a new BST using `Long` as key, and `String` as content
BinarySearchTree<Long, String> binarySearchTree = new BinarySearchTree<>(Long::compare);
binarySearchTree.insert(10L, "A string");

// Search for key 10L in the tree
Optional<String> maybeAString = binarySearchTree.search(10L);

// Delete element from BST
binarySearchTree.delete(10L);
```
