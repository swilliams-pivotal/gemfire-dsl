package org.tpi.pso.gemfire.dsl2

import groovy.transform.CompileStatic

import com.gemstone.gemfire.cache.CacheFactory
import com.gemstone.gemfire.pdx.PdxSerializer

@CompileStatic
public class PdxBuilder {

    private final CacheFactory cacheFactory

    public PdxBuilder(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory
    }

    def diskStore(String diskStore) {
        cacheFactory.setPdxDiskStore(diskStore)
    }

    def unreadFields(boolean unreadFields) {
        cacheFactory.setPdxIgnoreUnreadFields(unreadFields)
    }

    def persistent(boolean persistent) {
        cacheFactory.setPdxPersistent(persistent)
    }

    def readSerialized(boolean readSerialized) {
        cacheFactory.setPdxReadSerialized(readSerialized)
    }

    def serializer(String serializer) {
        def clazz = Class.forName(serializer)
        cacheFactory.setPdxSerializer(clazz.newInstance() as PdxSerializer)
    }

}
