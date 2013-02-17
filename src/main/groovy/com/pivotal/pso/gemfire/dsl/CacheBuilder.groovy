package com.pivotal.pso.gemfire.dsl

import static groovy.lang.Closure.DELEGATE_FIRST
import static groovy.lang.Closure.OWNER_FIRST
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.Region
import com.gemstone.gemfire.cache.RegionAttributes
import com.gemstone.gemfire.cache.RegionFactory
import com.gemstone.gemfire.cache.RegionShortcut
import com.gemstone.gemfire.cache.execute.FunctionAdapter
import com.gemstone.gemfire.cache.execute.FunctionService


@CompileStatic
class CacheBuilder {

    private Cache cache

    public CacheBuilder(Cache cache) {
        this.cache = cache
    }

    def function(String name, Closure closure) {
        function([:], name, closure)
    }

    def function(Map<String, Closure> params, String id, @DelegatesTo(strategy=OWNER_FIRST, value=ClosureFunctionAdapter) Closure closure) {
        println "function.$id(${params})"

        Region<String, byte[]> cclr = cache.getRegion('closure.classloader')
        cclr.put(closure.class.getName(), new byte[0])

        FunctionAdapter adapter = new ClosureFunctionAdapter(id, closure)
//        if (params.ha) func['isHA'] = { params.ha }
//        if (params.result) func['hasResult'] = { params.result }
//        if (params.optimizeForWrite) func['optimizeForWrite'] = { params.optimizeForWrite }

        FunctionService.registerFunction(adapter)
    }


    def server(String name, @DelegatesTo(strategy=DELEGATE_FIRST, value=CacheServerBuilder) Closure closure) {
        def builder = new CacheServerBuilder(cache.addCacheServer())
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = DELEGATE_FIRST
        hydrated()
    }

    def region(Map params, String name, @DelegatesTo(strategy=DELEGATE_FIRST, value=RegionBuilder) Closure closure) {

        RegionFactory factory
        if (params['shortcut'] && (params['shortcut'] instanceof String)) {
            def shortcut = RegionShortcut.valueOf((String) params['shortcut'])
            factory = cache.createRegionFactory(shortcut)
        }
        else if (params['shortcut'] && (params['shortcut'] instanceof RegionShortcut)) {
            def shortcut = (RegionShortcut) params['shortcut']
            factory = cache.createRegionFactory(shortcut)
        }
        else if (params['attributes'] && (params['attributes'] instanceof RegionAttributes)) {
            def attributes = (RegionShortcut) params['attributes']
            factory = cache.createRegionFactory(attributes)
        }
        else {
            factory = cache.createRegionFactory()
        }

        def builder = new RegionBuilder(factory, name, params)
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = DELEGATE_FIRST
        hydrated()

        factory.create(name)
    }

    def asyncEventQueue() {
        cache.createAsyncEventQueueFactory()
    }

    def diskStore() {
        cache.createDiskStoreFactory()
    }

    def gatewayReceiver() {
        cache.createGatewayReceiverFactory()
    }

    def gatewaySender() {
        cache.createGatewaySenderFactory()
    }

    def pdxInstance() {
        cache.createPdxEnum("", "", 0)
    }

    def pdxInstanceFactory() {
        cache.createPdxInstanceFactory("")
    }

}
