package org.tpi.pso.gemfire.dsl.loader;

import com.gemstone.gemfire.cache.CacheWriterException;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.util.CacheWriterAdapter;

public class CacheListeningClassLoader extends ClassLoader {

    private final CacheWriterAdapter<String, byte[]> cacheWriter = new CacheWriterAdapter<String, byte[]>() {

        @Override
        public void beforeCreate(EntryEvent<String, byte[]> event)
                throws CacheWriterException {
            if (!event.isOriginRemote()) {
                return;
            }
            fireCacheEvent(event);
        }

        @Override
        public void beforeUpdate(EntryEvent<String, byte[]> event)
                throws CacheWriterException {
            if (!event.isOriginRemote()) {
                return;
            }
            fireCacheEvent(event);
        }
    };

    protected void fireCacheEvent(EntryEvent<String, byte[]> event) {

        String name = event.getKey();
        byte[] data = event.getNewValue();

        Class<?> c = defineClass(name, data, 0, data.length);
        resolveClass(c);
    }

}
