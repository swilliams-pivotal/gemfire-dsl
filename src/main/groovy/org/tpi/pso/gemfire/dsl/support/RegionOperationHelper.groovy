package org.tpi.pso.gemfire.dsl.support;

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.CacheFactory
import com.gemstone.gemfire.cache.Region


class RegionOperationHelper {

    def key
    def value

    public RegionOperationHelper(Object key, Object value) {
        this.key = key
        this.value = value
    }

    private Region lookupRegion(String name) {
        null
    }

    def inRegion(String target) {
        println "lookupRegion ${target}.put($key, $value)"
        Cache cache = CacheFactory.getAnyInstance()
        Region region = cache.getRegion(target)
        inRegion(region)
    }

    def inRegion(Region region) {
        println "withRegion ${region.getName()}.put($key, $value)"
        region.put(key, value)
    }

    def to(Object target) {
        println "to ${target}.put($key, $value) then calls..."
        if (target instanceof String) {
            inRegion((String) target)
        }
        else if (target instanceof Region) {
            inRegion((Region) target)
        }
    }

    def from(Object target) {
        println "from ${target}.put($key, $value) then calls..."
        Region region = null
        if (target instanceof String) {
            region = lookupRegion((String) target)
        }
        else if (target instanceof Region) {
            region = target
        }
        region.remove(key)
    }

    @Override
    def rightShift(Object target) {
        to(target)
    }

    @Override
    def isCase(Object target) {
        to(target)
    }

}
