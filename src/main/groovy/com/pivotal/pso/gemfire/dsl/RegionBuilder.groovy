package com.pivotal.pso.gemfire.dsl

import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

import com.gemstone.gemfire.cache.CacheLoader
import com.gemstone.gemfire.cache.CacheWriter
import com.gemstone.gemfire.cache.CustomExpiry
import com.gemstone.gemfire.cache.DataPolicy
import com.gemstone.gemfire.cache.EvictionAttributes
import com.gemstone.gemfire.cache.ExpirationAttributes
import com.gemstone.gemfire.cache.MembershipAttributes
import com.gemstone.gemfire.cache.PartitionAttributesFactory
import com.gemstone.gemfire.cache.RegionFactory
import com.gemstone.gemfire.cache.Scope
import com.gemstone.gemfire.cache.SubscriptionAttributes
import com.gemstone.gemfire.cache.util.CacheListenerAdapter
import com.gemstone.gemfire.cache.util.CacheWriterAdapter
import com.pivotal.pso.gemfire.util.CacheListenerSupport
import com.pivotal.pso.gemfire.util.CacheWriterSupport


@CompileStatic
@TypeChecked
class RegionBuilder {

    private final String name

    private final Map<?, ?> params

    private final RegionFactory regionFactory

    public RegionBuilder(RegionFactory regionFactory, String name, java.util.Map<?, ?> params) {
        this.regionFactory = regionFactory
        this.name = name
        this.params = params
    }

    def asyncEventQueueIds(List ids) {
        ids.each { String id->
            regionFactory.addAsyncEventQueueId(id)
        }
    }

    def loader(@DelegatesTo(strategy=Closure.OWNER_FIRST, value=CacheListenerBuilder) Closure closure) {

        def builder = new CacheListenerBuilder()
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

    def partitionAttributes(@DelegatesTo(strategy=Closure.OWNER_FIRST, value=PartitionAttributesBuilder) Closure closure) {
        PartitionAttributesFactory factory = new PartitionAttributesFactory()

        def builder = new PartitionAttributesBuilder(factory)
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = Closure.DELEGATE_FIRST
        hydrated()

        regionFactory.setPartitionAttributes(factory.create())
    }

    def listener(@DelegatesTo(strategy=Closure.OWNER_FIRST, value=CacheListenerBuilder) Closure closure) {

        def builder = new CacheListenerBuilder()
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = Closure.DELEGATE_FIRST
        def listener = hydrated()

        regionFactory.addCacheListener(listener as CacheListenerAdapter)
    }

    def listener(Map funcmap) {
        Map<String, Closure> listener = [:]

        // create a new map, add & transform closures in it
        funcmap.collectEntries(listener) { String name, Closure closure ->
            def owner = closure.owner
            def thisObject = closure.thisObject
            def cls = new CacheListenerSupport()
            def hydrated = closure.rehydrate(cls, owner, thisObject)
            hydrated.resolveStrategy = Closure.DELEGATE_FIRST
            [name, hydrated]
        }

        regionFactory.addCacheListener(listener as CacheListenerAdapter)
    }

    def writer(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=CacheListenerBuilder) Closure closure) {

        def builder = new CacheWriterSupport()
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = Closure.DELEGATE_FIRST
        def writer = hydrated()

        regionFactory.setCacheWriter(writer as CacheWriterAdapter)
    }

    def writer(Map funcmap) {
        Map<String, Closure> writer = [:]

        // create a new map, add & transform closures in it
        funcmap.collectEntries(writer) { String name, Closure closure ->
            def owner = closure.owner
            def thisObject = closure.thisObject
            def cls = new CacheWriterSupport()
            def hydrated = closure.rehydrate(cls, owner, thisObject)
            hydrated.resolveStrategy = Closure.DELEGATE_FIRST
            [name, hydrated]
        }

        regionFactory.setCacheWriter(writer as CacheWriter)
    }

    def customEntryIdleTimeout() {
        CustomExpiry customExpiry = null
        regionFactory.setCustomEntryIdleTimeout(customExpiry)
    }

    def customEntryTimeToLive() {
        CustomExpiry customExpiry = null
        regionFactory.setCustomEntryTimeToLive(customExpiry)
    }

    def dataPolicy() {
        DataPolicy dataPolicy = null
        regionFactory.setDataPolicy(dataPolicy)
    }

    def entryIdleTimeout() {
        ExpirationAttributes expirationAttributes = null
        regionFactory.setEntryIdleTimeout(expirationAttributes)
    }

    def entryTimeToLive() {
        ExpirationAttributes expirationAttributes = null
        regionFactory.setEntryTimeToLive(expirationAttributes)
    }

    def evictionAttributes() {
        EvictionAttributes evictionAttributes = null
        regionFactory.setEvictionAttributes(evictionAttributes)
    }

    def membershipAttributes() {
        MembershipAttributes membershipAttributes = null
        regionFactory.setMembershipAttributes(membershipAttributes)
    }

    def regionIdleTimeout() {
        ExpirationAttributes expirationAttributes = null
        regionFactory.setRegionIdleTimeout(expirationAttributes)
    }

    def regionTimeToLive() {
        ExpirationAttributes expirationAttributes = null
        regionFactory.setRegionTimeToLive(expirationAttributes)
    }

    def scope() {
        Scope scopeType = null
        regionFactory.setScope(scopeType)
    }

    def subscriptionAttributes() {
        SubscriptionAttributes subscriptionAttributes = null
        regionFactory.setSubscriptionAttributes(subscriptionAttributes)
    }

    def methodMissing(String name, args) {
        println "RegionBuilder.name: $name"
    }

}
