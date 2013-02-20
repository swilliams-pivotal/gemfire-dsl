package org.tpi.pso.gemfire.dsl.support;

import com.gemstone.gemfire.cache.CacheWriterException;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.RegionEvent;
import com.gemstone.gemfire.cache.util.CacheWriterAdapter;

@Deprecated
public class DefaultCacheWriter<K, V> extends CacheWriterAdapter<K, V> {

    @Override
    public void beforeCreate(EntryEvent<K, V> event)
            throws CacheWriterException {
        super.beforeCreate(event);
    }

    @Override
    public void beforeDestroy(EntryEvent<K, V> event)
            throws CacheWriterException {
        super.beforeDestroy(event);
    }

    @Override
    public void beforeRegionClear(RegionEvent<K, V> event)
            throws CacheWriterException {
        super.beforeRegionClear(event);
    }

    @Override
    public void beforeRegionDestroy(RegionEvent<K, V> event)
            throws CacheWriterException {
        super.beforeRegionDestroy(event);
    }

    @Override
    public void beforeUpdate(EntryEvent<K, V> event)
            throws CacheWriterException {
        super.beforeUpdate(event);
    }

    @Override
    public void close() {
        super.close();
    }

}
