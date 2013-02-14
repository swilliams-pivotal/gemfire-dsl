package com.pivotal.pso.gemfire.dsl

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

import com.gemstone.gemfire.cache.CacheLoader
import com.gemstone.gemfire.cache.CacheWriter
import com.gemstone.gemfire.cache.EntryEvent
import com.gemstone.gemfire.cache.RegionEvent
import com.gemstone.gemfire.cache.RegionFactory
import com.gemstone.gemfire.cache.util.CacheListenerAdapter
import com.gemstone.gemfire.cache.util.CacheWriterAdapter


@CompileStatic
// @TypeChecked
class RegionBuilder {

    static final Closure EEC = { EntryEvent e-> println "eec: $e" }

    static final Closure REC = { RegionEvent e-> println "rec: $e" }

    static final Map<String, Closure> CACHE_LISTENER_MAP = [
            afterCreate: EEC,
            afterUpdate: EEC,
            afterInvalidate: EEC,
            afterDestroy: EEC,
            afterRegionInvalidate: REC,
            afterRegionDestroy: REC,
            afterRegionClear: REC,
            afterRegionCreate: REC,
            afterRegionLive: REC,
            close: {}
        ]


    private final String name

    private final Map<?, ?> params

    private final RegionFactory regionFactory

    public RegionBuilder(RegionFactory regionFactory, String name, java.util.Map<?, ?> params) {
        this.regionFactory = regionFactory
        this.name = name
        this.params = params
    }

    def loader(@DelegatesTo(strategy=Closure.OWNER_FIRST, value=GenericListenerBuilder) Closure closure) {

        def builder = new GenericListenerBuilder()
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = Closure.DELEGATE_FIRST
        def loader = hydrated()

        regionFactory.setCacheLoader(loader as CacheLoader)
    }

    def loader(Map loader) {
        Map<String, Closure> all = [:].withDefault { -> { -> } }
        all.putAll(loader)
        regionFactory.setCacheLoader(all as CacheLoader)
    }

    def writer(@DelegatesTo(strategy=Closure.OWNER_FIRST, value=GenericListenerBuilder) Closure closure) {

        def builder = new GenericListenerBuilder()
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = Closure.DELEGATE_FIRST
        def writer = hydrated()
        regionFactory.setCacheWriter(writer as CacheWriterAdapter)
    }

    def writer(Map writer) {
        // println "RegionBuilder.writerFromMap(map: ${writer.dump()})"
        Map<String, Closure> all = [:].withDefault {-> { -> } }
        all.putAll(writer)
        regionFactory.setCacheWriter(all as CacheWriter)
    }

    def listener(@DelegatesTo(strategy=Closure.OWNER_FIRST, value=GenericListenerBuilder) Closure closure) {

        def builder = new GenericListenerBuilder()
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = Closure.DELEGATE_FIRST
        def listener = hydrated()

        regionFactory.addCacheListener(listener as CacheListenerAdapter)
    }

    def listener(Map listener) {
        Map<String, Closure> all = [:]

        listener.collectEntries(all) { String name, Closure closure ->

            def owner = closure.owner
            def thisObject = closure.thisObject
            def rls = new CacheListenerSupport()
            def hydrated = closure.rehydrate(rls, owner, thisObject)
            hydrated.resolveStrategy = Closure.DELEGATE_FIRST
            [name, hydrated]
        }

        regionFactory.addCacheListener(listener as CacheListenerAdapter)
    }

    def methodMissing(String name, args) {
        println "RegionBuilder.name: $name"
    }

}
