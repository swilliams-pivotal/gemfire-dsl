package com.pivotal.pso.gemfire.dsl.region

import com.gemstone.gemfire.cache.*
import com.gemstone.gemfire.cache.asyncqueue.AsyncEventQueue
import com.gemstone.gemfire.cache.snapshot.SnapshotOptions
import com.gemstone.gemfire.cache.util.CacheListenerAdapter
import com.gemstone.gemfire.cache.wan.GatewaySender
import com.pivotal.pso.gemfire.dsl.Gemfire
import com.pivotal.pso.gemfire.dsl.listeners.CacheListenerBuilder
import com.pivotal.pso.gemfire.dsl.listeners.CacheLoaderBuilder
import com.pivotal.pso.gemfire.dsl.listeners.CacheWriterBuilder
import com.pivotal.pso.gemfire.dsl.support.DSLUtils
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import groovy.util.logging.Slf4j

/**
 * Author: smaldini
 * Date: 2/7/13
 * Project: gemfire-dsl
 */
@CompileStatic
@Slf4j
class BaseRegionBuilder<K, V> extends LookupRegionBuilder<K, V> {

    final List<CacheListener<K, V>> listeners = []

    Boolean destroy
    Boolean close = true
    File snapshot

    CacheLoader<K, V> cacheLoader
    CacheWriter<K, V> cacheWriter

    final List<GatewaySender> gatewaySenders = []
    final List<AsyncEventQueue> asyncEventQueues = []

    RegionAttributes<K, V> attributes

    Scope scope
    Boolean persistent = false

    //@Deprecated Boolean enableGateway
    //@Deprecated String hubId

    String diskStoreName
    String dataPolicy

    private Closure regionAttributesBuilder

    BaseRegionBuilder(Gemfire gemfire, String name) {
        super(gemfire, name)
    }

    @SuppressWarnings("deprecated")
    void setShortcut(RegionShortcut shortcut){
        this.attributes = new AttributesFactory<K,V>(cache.getRegionAttributes(shortcut.toString())).create()
    }

    void setShortcut(String shortcut){
        this.shortcut = shortcut as RegionShortcut
    }

    void setDestroy(boolean d){
        this.destroy = d
        this.close = false
    }

    void setClose(boolean d){
        this.close = d
        this.destroy = false
    }

    @SuppressWarnings("deprecated")
    protected postBuilderRun() {
        RegionFactory<K, V> regionFactory
        Cache c = ((Cache) cache)


        if(regionAttributesBuilder){
            def builder = attributes ? new AttributesFactory<K, V>(attributes) : new AttributesFactory<K, V>()
            DSLUtils.delegateFirstAndRun builder, regionAttributesBuilder
            attributes = builder.create()
        }

        if (attributes){
            regionFactory = c.createRegionFactory attributes
        }else
            regionFactory = c.createRegionFactory()

        for(String queue in asyncEventQueues.id){
            regionFactory.addAsyncEventQueueId queue
        }

        for(String sender in gatewaySenders.id){
            regionFactory.addAsyncEventQueueId sender
        }

        for(CacheListener listener in listeners){
            regionFactory.addCacheListener listener
        }

        if(cacheLoader)
            regionFactory.cacheLoader = cacheLoader

        if(cacheWriter)
            regionFactory.cacheWriter = cacheWriter

        if(diskStoreName)
            regionFactory.diskStoreName = diskStoreName

        resolveDataPolicy regionFactory, dataPolicy

        if(scope)
            regionFactory.scope = scope

        region = regionFactory.create name
        log.info "Created new cache region [$name]"

        if (snapshot) {
            region.getSnapshotService().load snapshot, SnapshotOptions.SnapshotFormat.GEMFIRE
        }

        if (attributes?.lockGrantor) {
            region.becomeLockGrantor()
        }

        gemfire.shutdownHooks <<  {
            this.close()
        }
    }

    protected void resolveDataPolicy(RegionFactory<K, V> regionFactory, String dataPolicy) {
        if (!dataPolicy) {
            if (persistent) {
                regionFactory.dataPolicy = DataPolicy.PERSISTENT_REPLICATE
            } else {
                regionFactory.dataPolicy = DataPolicy.DEFAULT
            }
        } else {
            regionFactory.dataPolicy = (DataPolicy) dataPolicy.properties[dataPolicy.toUpperCase()]
        }
    }

    protected close() {
        if (region) {
            if (close) {
                if (!region.getRegionService().isClosed()) {
                    try { region.close() } catch (CacheClosedException cce) {}
                }
            } else if (destroy) {
                region.destroyRegion()
            }
        }
    }

    CacheLoader<K, V> loader(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CacheLoaderBuilder) Closure c) {
        def builder = new CacheLoaderBuilder<K,V>()
        DSLUtils.delegateFirstAndRun builder, c

        cacheLoader = builder.create()
        cacheLoader
    }

    CacheWriter<K, V> writer(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CacheWriterBuilder) Closure c) {
        def builder = new CacheWriterBuilder<K,V>()
        DSLUtils.delegateFirstAndRun builder, c

        cacheWriter = builder.create()
        cacheWriter
    }

    CacheListener<K,V> listener(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CacheListenerBuilder) Closure c) {

        def builder = new CacheListenerBuilder<K,V>()
        DSLUtils.delegateFirstAndRun builder, c

        def cacheListener = builder.create()
        listeners << cacheListener

        cacheListener
    }

    void attributes(@DelegatesTo(AttributesFactory) Closure c) {
        regionAttributesBuilder = c
    }

}
