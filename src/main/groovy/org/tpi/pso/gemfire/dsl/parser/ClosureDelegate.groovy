package org.tpi.pso.gemfire.dsl.parser

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.Region

class ClosureDelegate<K, V> {

    private final Cache cache

    public ClosureDelegate(Cache cache) {
        this.cache = cache
    }

    public V put(String regionName, K key, V value) {
        Region<K, V> region = cache.getRegion(regionName)
        return region.put(key, value)
    }

    public V put(String regionName, K key, V value, Object thing) {
        Region<K, V> region = cache.getRegion(regionName)
        return region.put(key, value, thing)
    }

    def propertyMissing(String name) {
        Region<K, V> region = cache.getRegion(name)
        if (region == null) {
            super.propertyMissing(name)
        }
        else {
            region
        }
    }
}
