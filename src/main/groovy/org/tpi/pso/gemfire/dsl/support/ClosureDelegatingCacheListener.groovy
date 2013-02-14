package org.tpi.pso.gemfire.dsl.support

import com.gemstone.gemfire.cache.EntryEvent
import com.gemstone.gemfire.cache.RegionEvent
import com.gemstone.gemfire.cache.util.CacheListenerAdapter


public class ClosureDelegatingCacheListener<K, V> extends
        CacheListenerAdapter<K, V> {

    private final Closure<?> closure

    public ClosureDelegatingCacheListener(Closure<?> closure) {
        this.closure = closure
    }

    @Override
    public void afterCreate(EntryEvent<K, V> event) {
        println "afterCreate: ${event} " + closure.delegate.hasProperty('afterCreate')
        println "afterCreate: closure: " + closure.dump()
        println "afterCreate: closure.delegate: " + closure.delegate.dump()
        
//        if (closure.delegate.hasProperty('afterCreate') && closure.delegate.afterCreate) {
//            closure.delegate.afterCreate.call(event)
//        }
        if (closure.afterCreate) {
            closure.afterCreate.call(event)
            println "calling afterCreate with ${event} "
        }
        else if (closure.getMaximumNumberOfParameters() > 0
                && closure.getParameterTypes()[0].equals(String.class)) {
            // println "afterCreate " + event
            closure.curry("afterCreate").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterDestroy(EntryEvent<K, V> event) {
        if (closure.delegate.hasProperty('afterDestroy')) {
            closure.delegate.afterDestroy?.call(event)
        }
        else if (closure.getMaximumNumberOfParameters() > 0
                && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterDestroy").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterInvalidate(EntryEvent<K, V> event) {
        if (closure.delegate.hasProperty('afterInvalidate')) {
            closure.delegate.afterInvalidate?.call(event)
        }
        else if (closure.getMaximumNumberOfParameters() > 0
                && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterInvalidate").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterUpdate(EntryEvent<K, V> event) {
        if (closure.delegate.hasProperty('afterUpdate') && closure.delegate.afterUpdate) {
            closure.delegate.afterUpdate?.call(event)
        }
        else if (closure.getMaximumNumberOfParameters() > 0
                && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterUpdate").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterRegionClear(RegionEvent<K, V> event) {
        if (closure.delegate.hasProperty('afterRegionClear')) {
            closure.delegate.afterRegionClear?.call(event)
        }
        else if (closure.getMaximumNumberOfParameters() > 0
                && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterRegionClear").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterRegionCreate(RegionEvent<K, V> event) {
        if (closure.delegate.hasProperty('afterRegionCreate')) {
            closure.delegate.afterRegionCreate?.call(event)
        }
        else if (closure.getMaximumNumberOfParameters() > 0
                && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterRegionCreate").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterRegionDestroy(RegionEvent<K, V> event) {
        if (closure.delegate.hasProperty('afterRegionDestroy')) {
            closure.delegate.afterRegionDestroy?.call(event)
        }
        else if (closure.getMaximumNumberOfParameters() > 0
                && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterRegionDestroy").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterRegionInvalidate(RegionEvent<K, V> event) {
        if (closure.delegate.hasProperty('afterRegionInvalidate')) {
            closure.delegate.afterRegionInvalidate?.call(event)
        }
        else if (closure.getMaximumNumberOfParameters() > 0
                && closure.getParameterTypes()[0].equals(String.class)) {
            closure.curry("afterRegionInvalidate").call(event)
        } else {
            closure.call(event)
        }
    }

    @Override
    public void afterRegionLive(RegionEvent<K, V> event) {
        if (closure.delegate.hasProperty('afterRegionLive')) {
            closure.delegate.afterRegionLive?.call(event)
        }
        else if (closure.getMaximumNumberOfParameters() > 0
                && closure.getParameterTypes()[0].equals(String.class)) {
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
