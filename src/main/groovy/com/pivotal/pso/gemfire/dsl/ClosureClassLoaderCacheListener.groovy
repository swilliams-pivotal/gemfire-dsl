package com.pivotal.pso.gemfire.dsl

import com.gemstone.gemfire.cache.CacheWriterException
import com.gemstone.gemfire.cache.EntryEvent
import com.gemstone.gemfire.cache.util.CacheListenerAdapter

class ClosureClassLoaderCacheListener extends CacheListenerAdapter {

    @Override
    public void afterCreate(EntryEvent<String, byte[]> event)
            throws CacheWriterException {
        fireCacheEvent(event)
    }

    protected void fireCacheEvent(EntryEvent<String, byte[]> event) {

        String name = event.getKey()
        byte[] data = event.getNewValue()

        if (!event.isOriginRemote()) {
            return
        }

        ClassLoader loader = ClosureClassLoaderCacheListener.class.getClassLoader()
        Class<?> c = loader.defineClass(name, data, 0, data.length)
        loader.resolveClass(c)
    }

}