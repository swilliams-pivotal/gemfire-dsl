package org.tpi.pso.gemfire.dsl.support

import com.gemstone.gemfire.cache.EntryEvent
import com.gemstone.gemfire.cache.RegionEvent
import com.gemstone.gemfire.cache.util.CacheListenerAdapter


public class SimpleClosureDelegatingCacheListener<K, V> extends
        CacheListenerAdapter<K, V> {

    private final Closure closure

    public SimpleClosureDelegatingCacheListener(Closure) {
        this.closure = closure
    }

    @Override
    public void afterCreate(EntryEvent<K, V> event) {
        if (closure.getMaximumNumberOfParameters() > 0 && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterCreate").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterDestroy(EntryEvent<K, V> event) {
        if (closure.getMaximumNumberOfParameters() > 0 && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterDestroy").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterInvalidate(EntryEvent<K, V> event) {
        if (closure.getMaximumNumberOfParameters() > 0 && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterInvalidate").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterUpdate(EntryEvent<K, V> event) {
        if (closure.getMaximumNumberOfParameters() > 0 && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterUpdate").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterRegionClear(RegionEvent<K, V> event) {
        if (closure.getMaximumNumberOfParameters() > 0 && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterRegionClear").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterRegionCreate(RegionEvent<K, V> event) {
        if (closure.getMaximumNumberOfParameters() > 0 && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterRegionCreate").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterRegionDestroy(RegionEvent<K, V> event) {
        if (closure.getMaximumNumberOfParameters() > 0 && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterRegionDestroy").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterRegionInvalidate(RegionEvent<K, V> event) {
        if (closure.getMaximumNumberOfParameters() > 0 && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterRegionInvalidate").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterRegionLive(RegionEvent<K, V> event) {
        if (closure.getMaximumNumberOfParameters() > 0 && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterRegionLive").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        super.close()
    }

}
