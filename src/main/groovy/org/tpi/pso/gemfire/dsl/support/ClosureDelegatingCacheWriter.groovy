package org.tpi.pso.gemfire.dsl.support

import groovy.transform.CompileStatic

import com.gemstone.gemfire.cache.CacheWriterException
import com.gemstone.gemfire.cache.EntryEvent
import com.gemstone.gemfire.cache.RegionEvent
import com.gemstone.gemfire.cache.util.CacheWriterAdapter


@CompileStatic
public class ClosureDelegatingCacheWriter<K, V> extends
        CacheWriterAdapter<K, V> {

    private final Closure<?> closure

    public ClosureDelegatingCacheWriter(Closure<?> closure) {
        this.closure = closure
    }

    @Override
    public void beforeCreate(EntryEvent<K, V> event)
            throws CacheWriterException {
        try {
            if (closure.getMaximumNumberOfParameters() > 0
                    && closure.getParameterTypes()[0].equals(String.class)) {
                closure.curry("beforeCreate").call(event)
            } else {
                closure.call(event)
            }
        } catch (Exception e) {
            throw new CacheWriterException(e)
        }
    }

    @Override
    public void beforeDestroy(EntryEvent<K, V> event)
            throws CacheWriterException {
        try {
            if (closure.getMaximumNumberOfParameters() > 0
                    && closure.getParameterTypes()[0].equals(String.class)) {
                closure.curry("beforeDestroy").call(event)
            } else {
                closure.call(event)
            }
        } catch (Exception e) {
            throw new CacheWriterException(e)
        }
    }

    @Override
    public void beforeUpdate(EntryEvent<K, V> event)
            throws CacheWriterException {
        try {
            if (closure.getMaximumNumberOfParameters() > 0
                    && closure.getParameterTypes()[0].equals(String.class)) {
                closure.curry("beforeUpdate").call(event)
            } else {
                closure.call(event)
            }
        } catch (Exception e) {
            throw new CacheWriterException(e)
        }
    }

    @Override
    public void beforeRegionClear(RegionEvent<K, V> event)
            throws CacheWriterException {
        try {
            if (closure.getMaximumNumberOfParameters() > 0
                    && closure.getParameterTypes()[0].equals(String.class)) {
                closure.curry("beforeRegionClear").call(event)
            } else {
                closure.call(event)
            }
        } catch (Exception e) {
            throw new CacheWriterException(e)
        }
    }

    @Override
    public void beforeRegionDestroy(RegionEvent<K, V> event)
            throws CacheWriterException {
        try {
            if (closure.getMaximumNumberOfParameters() > 0
                    && closure.getParameterTypes()[0].equals(String.class)) {
                closure.curry("afterCreate").call(event)
            } else {
                closure.call(event)
            }
        } catch (Exception e) {
            throw new CacheWriterException(e)
        }
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        super.close()
    }

}
