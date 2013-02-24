package com.pivotal.pso.gemfire.dsl.listeners

import com.gemstone.gemfire.cache.CacheWriterException
import com.gemstone.gemfire.cache.EntryEvent
import com.gemstone.gemfire.cache.util.CacheWriterAdapter


class ClosureClassLoaderCacheWriter extends CacheWriterAdapter {

    @Override
    public void beforeCreate(EntryEvent<String, byte[]> event)
            throws CacheWriterException {

        try {
            fireCacheEvent(event)
        }
        catch (Throwable t) {
            throw new CacheWriterException(t)
        }
    }

    protected void fireCacheEvent(EntryEvent<String, byte[]> event) throws Exception {

        String name = event.getKey()
        byte[] data = event.getNewValue()

        if (!event.isOriginRemote()) {
            return
        }

        ClassLoader loader = ClosureClassLoaderCacheWriter.class.getClassLoader()
        Class<?> c = loader.defineClass(name, data, 0, data.length)
        loader.resolveClass(c)
    }

}