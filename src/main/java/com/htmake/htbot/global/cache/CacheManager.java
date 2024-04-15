package com.htmake.htbot.global.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheManager<K, V> {

    private final Map<K, CacheEntry<V>> cacheMap;
    private final long defaultTTL;

    public CacheManager() {
        this.cacheMap = new HashMap<>();
        this.defaultTTL = 1800000;
    }

    public void put(K key, V value) {
        put(key, value, defaultTTL);
    }

    public void put(K key, V value, long ttl) {
        cacheMap.put(key, new CacheEntry<>(value, ttl));
    }

    public V get(K key) {
        CacheEntry<V> entry = cacheMap.get(key);
        if (entry != null && !entry.isExpired()) {
            return entry.getValue();
        }
        return null;
    }

    public boolean containsKey(K key) {
        return cacheMap.containsKey(key) && !cacheMap.get(key).isExpired();
    }

    public void remove(K key) {
        cacheMap.remove(key);
    }

    private static class CacheEntry<V> {

        private final V value;
        private final long expiryTime;

        public CacheEntry(V value, long ttl) {
            this.value = value;
            this.expiryTime = System.currentTimeMillis() + ttl;
        }

        public V getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() >= expiryTime;
        }
    }
}
