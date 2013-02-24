package com.pivotal.pso.gemfire.dsl.region

import com.gemstone.gemfire.cache.DataPolicy
import com.gemstone.gemfire.cache.GemFireCache
import com.gemstone.gemfire.cache.RegionFactory
import com.pivotal.pso.gemfire.dsl.Gemfire
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

/**
 * Author: smaldini
 * Date: 2/7/13
 * Project: gemfire-dsl
 */
@CompileStatic
class ReplicatedRegionBuilder<K, V> extends BaseRegionBuilder<K, V> {

    ReplicatedRegionBuilder(Gemfire gemfire, String name) {
        super(gemfire, name)
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
