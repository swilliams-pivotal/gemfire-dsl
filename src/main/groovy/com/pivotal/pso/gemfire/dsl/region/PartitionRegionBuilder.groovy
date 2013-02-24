package com.pivotal.pso.gemfire.dsl.region

import com.gemstone.gemfire.cache.CacheWriter
import com.gemstone.gemfire.cache.DataPolicy
import com.gemstone.gemfire.cache.GemFireCache
import com.gemstone.gemfire.cache.PartitionAttributes
import com.gemstone.gemfire.cache.PartitionAttributesFactory
import com.gemstone.gemfire.cache.PartitionResolver
import com.gemstone.gemfire.cache.RegionAttributes
import com.gemstone.gemfire.cache.RegionFactory
import com.gemstone.gemfire.cache.partition.PartitionListener
import com.pivotal.pso.gemfire.dsl.Gemfire
import com.pivotal.pso.gemfire.dsl.listeners.CacheWriterBuilder
import com.pivotal.pso.gemfire.dsl.listeners.PartitionResolverBuilder
import com.pivotal.pso.gemfire.dsl.support.DSLUtils
import com.pivotal.pso.gemfire.util.CacheListenerSupport
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

/**
 * Author: smaldini
 * Date: 2/7/13
 * Project: gemfire-dsl
 */
@CompileStatic
class PartitionRegionBuilder<K, V> extends BaseRegionBuilder<K, V> {

    PartitionRegionBuilder(Gemfire gemfire, String name) {
        super(gemfire, name)
    }

    @Override
    protected void resolveDataPolicy(RegionFactory<K, V> regionFactory, String dataPolicy) {
        if (!dataPolicy) {
            if (persistent) {
                regionFactory.dataPolicy = DataPolicy.PERSISTENT_PARTITION
            } else {
                regionFactory.dataPolicy = DataPolicy.PARTITION
            }
        } else {
            regionFactory.dataPolicy = (DataPolicy) dataPolicy.properties[dataPolicy.toUpperCase()]
        }
    }

    void partitionAttributes(@DelegatesTo(PartitionAttributesBuilder) Closure c) {

        def builder = new PartitionAttributesBuilder<K, V>()
        DSLUtils.delegateFirstAndRun builder, c
        def _partitionAttributes = builder.create()

        super.attributes {
            partitionAttributes = _partitionAttributes
        }

    }

    static class PartitionAttributesBuilder<K, V> extends PartitionAttributesFactory{

        PartitionResolver<K, V> resolver(String name = null,
                @DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = PartitionResolverBuilder) Closure c) {
            def builder = new PartitionResolverBuilder<K, V>()
            DSLUtils.delegateFirstAndRun builder, c

            if(!builder.adapter.getName){
                builder.appendCallback 'getName', {name}
            }

            def partitionResolver = builder.create()
            this.partitionResolver = partitionResolver
            partitionResolver

        }

    }
}
