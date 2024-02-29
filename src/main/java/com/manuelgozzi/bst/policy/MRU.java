package com.manuelgozzi.bst.policy;

import com.manuelgozzi.bst.BinarySearchTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * @author Manuel Gozzi
 */
@SuppressWarnings("unused")
public class MRU<K> implements BinarySearchTreePolicy<K> {

    private final LinkedList<K> keys = new LinkedList<>();

    private final Integer capSize;

    public MRU(Integer capSize) {
        this.capSize = requireNonNull(capSize, "No `capSize` has been provided");
    }

    @Override
    public List<K> ensureCapacity(BinarySearchTree<K, ?> binarySearchTree) {
        List<K> keysToRemove = new ArrayList<>();
        while ((this.keys.size() - keysToRemove.size()) > this.capSize) {

            keysToRemove.add(this.keys.getLast());
        }
        return keysToRemove;
    }

    @Override
    public void trackInsertionOfKey(K key) {
        this.keys.addFirst(key);
    }

    @Override
    public void trackDeletionOfKey(K key) {
        this.keys.remove(key);
    }

    @Override
    public void trackUsage(K key) {
        var hasBeenRemoved = this.keys.remove(key);
        if (hasBeenRemoved) this.keys.addFirst(key);
    }
}
