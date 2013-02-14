package org.tpi.pso.gemfire.dsl.support

import groovy.transform.CompileStatic

import com.gemstone.gemfire.cache.CacheLoader
import com.gemstone.gemfire.cache.CacheLoaderException
import com.gemstone.gemfire.cache.LoaderHelper


@CompileStatic
public class ClosureDelegatingCacheLoader<K, V> implements
        CacheLoader<K, V> {

    private final Closure<?> closure

    public ClosureDelegatingCacheLoader(Closure<?> closure) {
        this.closure = closure
    }

    @Override
    public V load(LoaderHelper<K, V> helper) throws CacheLoaderException {
        closure?.call(helper)
    }

    @Override
    public void close() {
        // NO-OP
    }
}
