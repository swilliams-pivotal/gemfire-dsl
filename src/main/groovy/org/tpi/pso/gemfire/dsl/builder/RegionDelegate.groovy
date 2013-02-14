package org.tpi.pso.gemfire.dsl.builder

import groovy.transform.CompileStatic

import org.tpi.pso.gemfire.dsl.support.ListenerClosureDelegate
import org.tpi.pso.gemfire.dsl.support.SimpleClosureDelegatingCacheListener

import com.gemstone.gemfire.cache.CacheListener
import com.gemstone.gemfire.cache.RegionFactory

@CompileStatic
class RegionDelegate {

    private RegionFactory regionFactory

    public RegionDelegate(RegionFactory regionFactory) {
        this.regionFactory = regionFactory
    }

    def loader(Closure closure) {
        println "RegionDelegate.loader($closure)"
        // regionFactory.setCacheLoader(closure as ClosureDelegatingCacheLoader)
    }

    def writer(Closure closure) {
        println "RegionDelegate.writer($closure)"
        // regionFactory.setCacheWriter(closure as ClosureDelegatingCacheWriter)
    }

    def listener(@DelegatesTo(strategy=Closure.OWNER_FIRST, value=ListenerClosureDelegate) Closure closure) {
        println "RegionDelegate.listenerFromClosure(closure: ${closure.dump()})"
        closure.delegate = new ListenerClosureDelegate()
        closure.resolveStrategy = Closure.OWNER_FIRST
        CacheListener listener = new SimpleClosureDelegatingCacheListener(closure)

        Map<String, Closure> all = [:].withDefault { e-> }
        all.with closure
        regionFactory.addCacheListener(all as CacheListener)
    }

    def listener(Map listener) {
        println "RegionDelegate.listenerFromMap(map: ${listener.dump()})"
        Map<String, Closure> all = [:].withDefault { e-> }
        all.putAll(listener)
        regionFactory.addCacheListener(all as CacheListener)
    }

    def attributes(Map params, Closure closure = null) {
        println "RegionDelegate.attributes($params)"
        if (closure != null) {
            closure()
        }
    }

    def disk(Map params, Closure closure = null) {
        println "RegionDelegate.disk($params)"
        if (closure != null) {
            closure()
        }
    }

    def methodMissing(String name, map) {
        println "RegionDelegate.missing.$name($map)"
    }
}
