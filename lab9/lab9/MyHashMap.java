package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author pickwick
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private double loadFactor() {
        return (double) size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    public MyHashMap(int initialSize) {
        buckets = new ArrayMap[initialSize];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return -1;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int index = hash(key);
        // return null iff key == null
        if (index >= 0) {
            return buckets[index].get(key);
        } else {
            return null;
        }
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (index >= 0) {
            if (!buckets[index].containsKey(key)) {
                size += 1;
            }
            buckets[index].put(key, value);
        }
        if (loadFactor() > MAX_LF) {
            resize(2 * buckets.length);
        }
    }

    private void resize(int N) {
        MyHashMap<K, V> tmp = new MyHashMap<>(N);
        for (ArrayMap<K, V> bucket : buckets) {
            for (K key : bucket) {
                tmp.put(key, this.get(key));
            }
        }
        this.buckets = tmp.buckets;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (ArrayMap<K, V> bucket : buckets) {
            for (K item : bucket) {
                keySet.add(item);
            }
        }
        return keySet;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        int index = hash(key);
        V retV = buckets[index].get(key);
        if (buckets[index].containsKey(key)) {
            size -= 1;
        }
        buckets[index].remove(key);
        return retV;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        int index = hash(key);
        V retV = buckets[index].get(key);
        if (retV != null && retV.equals(value)) {
            buckets[index].remove(key, value);
            size -= 1;
        }
        return retV;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    private void printMap() {
        for (K item : this) {
            System.out.println(item + " " + get(item));
        }
        System.out.println();
        System.out.println("size: " + size());
    }

    public static void main(String[] args) {
        MyHashMap<String, Integer> b = new MyHashMap<String, Integer>();
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
            //make sure put is working via containsKey and get
            if (null == b.get("hi" + i)) {
                System.out.println("hi" + i);
            }
            // if (b.containsKey("hi" + i)) {
            //     System.out.println("hi" + i);
            // }
        }
        System.out.println(b.get("hi" + 1));
    }
}
