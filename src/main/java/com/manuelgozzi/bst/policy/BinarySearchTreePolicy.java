package com.manuelgozzi.bst.policy;

import com.manuelgozzi.bst.BinarySearchTree;

import java.util.List;

/**
 * It represents the policy to use for eviction handling and cap size limiting.
 *
 * @param <K> is the key type
 * @author Manuel Gozzi
 */
public interface BinarySearchTreePolicy<K> {

    /**
     * It tracks the insertion of a key in the policy.
     *
     * @param key is the key that needs to be added
     */
    void trackInsertionOfKey(K key);

    /**
     * It tracks the deletion of a key in the tree.
     *
     * @param key is the key to delete
     */
    void trackDeletionOfKey(K key);

    /**
     * It tracks the usage of a key in the tree.
     *
     * @param key is the key that's being used
     */
    void trackUsage(K key);

    /**
     * It ensures capacity of the tree returning a list of keys that needs to be removed in order to adapt the tree
     * structure.
     *
     * @param binarySearchTree is the binary search tree to consider
     * @return the list of keys that need to be removed
     */
    List<K> ensureCapacity(BinarySearchTree<K, ?> binarySearchTree);

    /**
     * It does the key insertion track first, and then ensures the binary tree capacity by updating the tree in order to
     * fulfill the binary tree size requirements in terms of cap size.
     *
     * @param key              is the key that's being inserted
     * @param binarySearchTree is the binary search tree to adapt
     */
    default void trackInsertionAndUpdateTree(K key, BinarySearchTree<K, ?> binarySearchTree) {

        this.trackInsertionOfKey(key);
        this.ensureCapacityAndUpdateTree(binarySearchTree);
    }

    /**
     * It calls the {@link #ensureCapacity(BinarySearchTree)} method obtaining a list of keys that need to be removed
     * from the tree in order to fulfill its size cap requirements.
     *
     * @param binarySearchTree is the binary search tree whose capacity needs to be ensured and maintained
     */
    private void ensureCapacityAndUpdateTree(BinarySearchTree<K, ?> binarySearchTree) {

        var list = this.ensureCapacity(binarySearchTree);
        if (list != null && !list.isEmpty()) {

            list.forEach(binarySearchTree::delete);
        }
    }
}
