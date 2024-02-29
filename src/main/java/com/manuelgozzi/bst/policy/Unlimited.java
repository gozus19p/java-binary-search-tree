package com.manuelgozzi.bst.policy;

import com.manuelgozzi.bst.BinarySearchTree;

import java.util.Collections;
import java.util.List;

/**
 * @author Manuel Gozzi
 */
@SuppressWarnings("unused")
public class Unlimited<K> implements BinarySearchTreePolicy<K> {
    @Override
    public void trackInsertionOfKey(Object key) {

    }

    @Override
    public void trackDeletionOfKey(Object key) {

    }

    @Override
    public void trackUsage(Object key) {

    }

    @Override
    public List<K> ensureCapacity(BinarySearchTree<K, ?> binarySearchTree) {
        return Collections.emptyList();
    }
}
