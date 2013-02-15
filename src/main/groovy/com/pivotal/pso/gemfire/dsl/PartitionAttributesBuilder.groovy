package com.pivotal.pso.gemfire.dsl

import com.gemstone.gemfire.cache.PartitionAttributesFactory
import com.gemstone.gemfire.cache.PartitionResolver
import com.gemstone.gemfire.cache.partition.PartitionListener


class PartitionAttributesBuilder {

    private PartitionAttributesFactory factory

    public PartitionAttributesBuilder(PartitionAttributesFactory factory) {
        this.factory = factory
    }

    def fixedPartitionAttributes(Closure closure) {
        factory.addFixedPartitionAttributes(null)
        this
    }

    def listener(Closure closure) {
        def listener = null
        factory.addPartitionListener(listener as PartitionListener)
        this
    }

    def listener(Map funcmap) {
        factory.addPartitionListener(funcmap as PartitionListener)
        this
    }

    def colocatedWith(String colocatedWith) {
        factory.setColocatedWith(colocatedWith)
        this
    }

    def localMaxMemory(int localMaxMemory) {
        factory.setLocalMaxMemory(localMaxMemory)
        this
    }

    def resolver(Map funcmap) {
        Map<String, Closure> resolver = [:].withDefault { -> }
        funcmap.collectEntries(resolver) { String name, Closure closure ->
            def owner = closure.owner
            def thisObject = closure.thisObject
            def cls = new CacheListenerSupport()
            def hydrated = closure.rehydrate(cls, owner, thisObject)
            hydrated.resolveStrategy = Closure.DELEGATE_FIRST
            [name, hydrated]
        }
        resolver(resolver.getName(), resolver.getRoutingObject(), resolver.close())
        this
    }

    def resolver(String name, Closure routing, Closure closer = null) {

        def owner = routing.owner
        def thisObject = routing.thisObject
        def cls = new CacheListenerBuilder()

        def hydrated = routing.rehydrate(cls, owner, thisObject)
        hydrated.resolveStrategy = Closure.DELEGATE_FIRST

        def resolver = [:]
        resolver['getName'] = { name }
        resolver['getRoutingObject'] = hydrated
        resolver['close'] = closer ?: {}
        factory.setPartitionResolver(resolver as PartitionResolver)
        this
    }

    def recoveryDelay(int recoveryDelay) {
        factory.setRecoveryDelay(recoveryDelay)
        this
    }

    def redundantCopies(int redundantCopies) {
        factory.setRedundantCopies(redundantCopies)
        this
    }

    def startupRecoveryDelay(int startupRecoveryDelay) {
        factory.setStartupRecoveryDelay(startupRecoveryDelay)
        this
    }

    def totalMaxMemory(int totalMaxMemory) {
        factory.setTotalMaxMemory(totalMaxMemory)
        this
    }

    def totalNumBuckets(int totalNumBuckets) {
        factory.setTotalNumBuckets(totalNumBuckets)
        this
    }

    def methodMissing(String name, args) {
        println "PartitionAttributesBuilder.name: $name(${args as List})"
    }

}
