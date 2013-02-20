package org.tpi.pso.gemfire.dsl.support;

import com.gemstone.gemfire.cache.CacheLoader;
import com.gemstone.gemfire.cache.CacheLoaderException;
import com.gemstone.gemfire.cache.LoaderHelper;

@Deprecated
public class DefaultCacheLoader<K, V> implements CacheLoader<K, V> {

    @Override
    public V load(LoaderHelper<K, V> helper) throws CacheLoaderException {
        return null;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

}
