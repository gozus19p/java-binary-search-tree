package com.manuelgozzi.bst;

import com.manuelgozzi.bst.policy.BinarySearchTreePolicy;
import com.manuelgozzi.bst.policy.Unlimited;

import java.util.Comparator;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * @param <T> is the content
 * @author Manuel Gozzi
 */
@SuppressWarnings("unused")
public class BinarySearchTree<K, T> {

    /**
     * It's the root node where all the hierarchy starts from.
     */
    private TreeNode<K, T> root;

    /**
     * The comparator used to compare keys in order to allow binary search.
     */
    private final Comparator<K> comparator;

    /**
     * Is the policy to use in order to handle eviction.
     */
    private final BinarySearchTreePolicy<K> policy;

    /**
     * It initializes a new {@link BinarySearchTree} containing an instance of generic type {@link T}.
     * It uses {@link Unlimited} policy as default.
     *
     * @param comparator is the comparator to use in order to work on keys
     */
    public BinarySearchTree(Comparator<K> comparator) {
        this(comparator, new Unlimited<>());
    }

    /**
     * It initializes a new {@link BinarySearchTree} containing an instance of generic type {@link T}.
     *
     * @param comparator is the comparator to use in order to work on keys
     * @param policy     is the policy to use in order to handle eviction
     */
    public BinarySearchTree(Comparator<K> comparator, BinarySearchTreePolicy<K> policy) {
        this.comparator = requireNonNull(comparator, "No `comparator` has been provided");
        this.policy = requireNonNull(policy, "No `policy` has been provided");
        this.root = null;
    }

    /**
     * It checks that the given key is contained in the tree structure.
     *
     * @param key is the key to check
     * @return <code>true</code> if the key is contained in the tree
     */
    public boolean containsKey(K key) {
        return this.search(requireNonNull(key, "No `key` provided for `containsKey` method"))
                .isPresent();
    }

    /**
     * It inserts a new element in the tree by receiving its key and the content.
     *
     * @param key     is the key
     * @param content is the content to store inside the node
     */
    public void insert(K key, T content) {

        // Inserting
        this.root = this.insertRec(this.root, key, content);

        // Increment size of tree and ensure capacity, first
        this.policy.trackInsertionAndUpdateTree(key, this);
    }

    /**
     * Recursive insertion of a new node.
     *
     * @param current is the current node to evaluate
     * @param key     is the node key to use in the insertion
     * @param content is the content to put inside the node
     * @return the newest tree node
     */
    private TreeNode<K, T> insertRec(TreeNode<K, T> current, K key, T content) {

        if (current == null) {
            current = new TreeNode<>(key, content);
            return current;
        }

        int compare = this.comparator.compare(key, current.key);
        if (compare < 0) {
            current.left = this.insertRec(current.left, key, content);
        } else {
            current.right = this.insertRec(current.right, key, content);
        }

        return current;
    }

    /**
     * It removes an element from the node given its key.
     *
     * @param key is the key of the node to remove
     */
    public void delete(K key) {

        var removed = this.deleteRecursive(this.root, key);

        // If root has been removed, reset the root
        if (key == this.root.key) this.root = removed;

        this.policy.trackDeletionOfKey(key);
    }

    /**
     * It recursively deletes a node given its key.
     *
     * @param current is the current node to evaluate
     * @param key     is the key of the node to delete
     * @return the deleted node
     */
    private TreeNode<K, T> deleteRecursive(TreeNode<K, T> current, K key) {

        if (current == null) return null;

        if (key == current.key) {
            // Node to delete found
            if (current.left == null && current.right == null) {
                return null;
            } else if (current.left == null) {
                return current.right;
            } else if (current.right == null) {
                return current.left;
            } else {

                // Node has both left and right children
                // Find the successor (smallest node in the right subtree)
                var smallest = this.findSmallest(current.right);
                current.key = smallest.key;
                current.content = smallest.content;
                // Delete the successor node
                current.right = deleteRecursive(current.right, current.key);
            }
        }
        if (this.comparator.compare(key, current.key) < 0) {
            current.left = deleteRecursive(current.left, key);
            return current;
        }
        current.right = deleteRecursive(current.right, key);
        return current;
    }

    /**
     * Given a node, it searches for the smallest node in its subtree.
     *
     * @param node is the node to evaluate
     * @return the smallest node
     */
    private TreeNode<K, T> findSmallest(TreeNode<K, T> node) {

        // Find the smallest node in the given subtree
        while (node.left != null) node = node.left;

        return node;
    }

    /**
     * It searches for a node content given its key.
     *
     * @param key is the key to use in the search process
     * @return the {@link Optional} wrapper of the content found
     */
    public Optional<T> search(K key) {
        return this.searchRec(this.root, key)
                .map(t -> {
                    this.policy.trackUsage(key);
                    return t;
                });
    }

    /**
     * It searches recursively for a node content given its key.
     *
     * @param current is the current node to evaluate
     * @param key     is the key to use in the recursion
     * @return the {@link Optional} wrapper of the content found
     */
    private Optional<T> searchRec(TreeNode<K, T> current, K key) {
        if (current == null || current.key.equals(key)) {
            return Optional.ofNullable(current)
                    .map(t -> t.content);
        }

        if (this.comparator.compare(key, current.key) < 0) return this.searchRec(current.left, key);

        return this.searchRec(current.right, key);
    }

    /**
     * It represents a single tree node identified by a key and a content.
     *
     * @param <K> is the key type
     * @param <T> is the content type
     */
    public static class TreeNode<K, T> {

        /**
         * The key that identifies the node.
         */
        private K key;

        /**
         * The left element of the node.
         */
        private TreeNode<K, T> left;

        /**
         * The right element of the node.
         */
        private TreeNode<K, T> right;

        /**
         * The node content.
         */
        private T content;

        /**
         * It constructs a new {@link TreeNode} structure by passing key and content.
         *
         * @param key     is the key
         * @param content is the content
         * @throws NullPointerException if either key or content are null
         */
        public TreeNode(K key, T content) {
            this.key = requireNonNull(key, "No `key` provided");
            this.content = requireNonNull(content, "No `content` provided");
            this.left = this.right = null;
        }
    }
}
