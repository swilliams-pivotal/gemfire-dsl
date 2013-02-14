package org.tpi.pso.gemfire.dsl.builder

import static groovy.lang.Closure.DELEGATE_FIRST

import org.tpi.pso.gemfire.dsl.support.ClosureDelegatingFunction

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.Region
import com.gemstone.gemfire.cache.RegionFactory
import com.gemstone.gemfire.cache.RegionShortcut
import com.gemstone.gemfire.cache.execute.FunctionService
import com.gemstone.gemfire.cache.util.GatewayHub


// @CompileStatic
public class CacheDelegate {

    private Cache cache

    public CacheDelegate(Cache cache) {
        this.cache = cache
    }

    public GatewayHub gatewayHub(Map params = [:], String id, int port, @DelegatesTo(strategy=DELEGATE_FIRST, value=GatewayDelegate) Closure closure = null) {
        println "CacheDelegate.gatewayHub.${id}:${port}"
        GatewayHub gatewayHub = cache.addGatewayHub(id, port)
        params.each { key, value->
            if (gatewayHub.hasProperty(key)) {
                gatewayHub[key] = value
            }
        }

        if (closure != null) {
            closure.delegate = new GatewayDelegate(gatewayHub)
            closure.resolveStrategy = DELEGATE_FIRST
            closure()
        }

        gatewayHub.start()
        gatewayHub
    }

    public void function(String name, Closure closure) {
        println "CacheDelegate.registerFunction($name, ${closure.toString()})"
        FunctionService.registerFunction(new ClosureDelegatingFunction(closure, name))
    }

    // TODO Figure out why I need this
    public Region region(String name, Map params, @DelegatesTo(strategy=DELEGATE_FIRST, value=RegionDelegate) Closure closure = null) {
        region(params, name, closure)
    }

    public Region region(Map params, String name, @DelegatesTo(strategy=DELEGATE_FIRST, value=RegionDelegate) Closure closure = null) {
        println "CacheDelegate.region.$name($params)"

        RegionFactory factory
        if (params['shortcut']) {
            def shortcut = (params['shortcut'] instanceof String) ? RegionShortcut.valueOf(params['shortcut']) : params['shortcut']
            factory = cache.createRegionFactory(shortcut as RegionShortcut)
        }
        else {
            factory = cache.createRegionFactory()
        }

//        if (params.containsKey('loader')) {
//            Closure loader = params.remove('loader')
//            factory.setCacheLoader(new ClosureDelegatingCacheLoader(loader))
//        }
//
//        if (params.containsKey('writer')) {
//            Closure writer = params.remove('writer')
//            factory.setCacheWriter(new ClosureDelegatingCacheWriter(writer))
//        }

        // factory.initCacheListeners(null) // CacheListener[]
//        if (params.containsKey('listeners')) {
//            Closure[] listeners = params.remove('listeners')
//            for (@DelegatesTo(strategy=Closure.OWNER_FIRST, value=ListenerClosureDelegate) Closure listener : listeners) {
//                println "addCacheListener " + (listener as Map).dump()
//
////                listener.delegate = new ListenerClosureDelegate()
////                listener.resolveStrategy = Closure.OWNER_FIRST
//
//                // factory.addCacheListener(new ClosureDelegatingCacheListener(listener))
//                factory.addCacheListener(listener as CacheListener)
//            }
//        }

        params.each { key, value-> 
            if (factory.hasProperty(key)) {
                factory[key] = value
            }
        }

        //        factory.addAsyncEventQueueId(name)
        //        factory.addGatewaySenderId(name)
        //        factory.setDiskStoreName(name)
        //        factory.setPoolName(name)

        //        factory.setConcurrencyLevel(0)
        //        factory.setInitialCapacity(0)
        //        factory.setLoadFactor(0)

        //        factory.setCloningEnabled(false)
        //        factory.setConcurrencyChecksEnabled(false)
        //        factory.setDiskSynchronous(false)
        //        factory.setEnableAsyncConflation(false)
        //        factory.setEnableSubscriptionConflation(false)
        //        factory.setIgnoreJTA(false)
        //        factory.setIndexMaintenanceSynchronous(false)
        //        factory.setLockGrantor(false)
        //        factory.setMulticastEnabled(false)
        //        factory.setStatisticsEnabled(false)

        //        factory.setKeyConstraint(null) // Class
        //        factory.setValueConstraint(null) // Class

        //        factory.setCustomEntryIdleTimeout(null) // CustomExpiry
        //        factory.setCustomEntryTimeToLive(null) // CustomExpiry
        //        factory.setDataPolicy(null) // DataPolicy
        //        factory.setEntryIdleTimeout(null) // ExpirationAttributes
        //        factory.setEntryTimeToLive(null) // ExpirationAttributes
        //        factory.setEvictionAttributes(null) // EvictionAttributes
        //        factory.setMembershipAttributes(null) // MembershipAttributes
        //        factory.setPartitionAttributes(null) // PartitionAttributes
        //        factory.setRegionIdleTimeout(null) // ExpirationAttributes
        //        factory.setRegionTimeToLive(null) // ExpirationAttributes
        //        factory.setScope(null) // Scope
        //        factory.setSubscriptionAttributes(null) // SubscriptionAttributes

        if (closure != null) {
            closure.delegate = new RegionDelegate(factory)
            closure.resolveStrategy = DELEGATE_FIRST
            closure()
        }

        Region region = factory.create(name)
        region
    }

    def methodMissing(String name, map) {
        println "CacheDelegate.missing.$name($map)"
    }
}
