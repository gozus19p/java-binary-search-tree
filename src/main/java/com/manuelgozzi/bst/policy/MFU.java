package com.manuelgozzi.bst.policy;

import com.manuelgozzi.bst.BinarySearchTree;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * @author Manuel Gozzi
 */
@SuppressWarnings("unused")
public class MFU<K> implements BinarySearchTreePolicy<K> {

    private final Map<K, Integer> keys = new HashMap<>();

    private final Integer capSize;

    public MFU(Integer capSize) {
        this.capSize = requireNonNull(capSize, "No `capSize` provided");
    }

    @Override
    public List<K> ensureCapacity(BinarySearchTree<K, ?> binarySearchTree) {

        List<K> keysToRemove = new ArrayList<>();

        var sortedKeys = this.keys.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .toList();
        while ((sortedKeys.size() - keysToRemove.size()) > this.capSize) {

            var entry = sortedKeys.get(sortedKeys.size() - 1);
            keysToRemove.add(entry.getKey());
        }
        return keysToRemove;
    }

    @Override
    public void trackInsertionOfKey(K key) {
        int usages = Optional.ofNullable(this.keys.get(key))
                .orElse(0);
        this.keys.put(key, ++usages);
    }

    @Override
    public void trackDeletionOfKey(K key) {
        this.keys.remove(key);
    }

    @Override
    public void trackUsage(K key) {
        if (this.keys.containsKey(key)) {

            this.keys.put(key, this.keys.get(key) + 1);
        }
    }
}
