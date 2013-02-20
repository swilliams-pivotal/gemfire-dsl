package com.pivotal.pso.gemfire.dsl.region

import com.gemstone.gemfire.cache.DataPolicy
import com.gemstone.gemfire.cache.GemFireCache
import com.gemstone.gemfire.cache.RegionFactory
import groovy.transform.CompileStatic

/**
 * Author: smaldini
 * Date: 2/7/13
 * Project: gemfire-dsl
 */
@CompileStatic
class ReplicatedRegionBuilder<K, V> extends BaseRegionBuilder<K, V> {

    ReplicatedRegionBuilder(GemFireCache cache, String name) {
        super(cache, name)
    }

    @Override
    protected void resolveDataPolicy(RegionFactory<K, V> regionFactory, String dataPolicy) {
        if (!dataPolicy) {
            if (persistent) {
                regionFactory.dataPolicy = DataPolicy.PERSISTENT_REPLICATE
            } else {
                regionFactory.dataPolicy = DataPolicy.REPLICATE
            }
        } else {
            regionFactory.dataPolicy = (DataPolicy) dataPolicy.properties[dataPolicy.toUpperCase()]
        }
    }
}
