package com.pivotal.pso.gemfire.dsl.listeners

import com.gemstone.gemfire.cache.CacheLoader
import com.gemstone.gemfire.cache.PartitionResolver
import com.pivotal.pso.gemfire.dsl.support.CacheCallbackBuilder
import groovy.transform.CompileStatic

/**
 * Author: smaldini
 * Date: 2/10/13
 * Project: gemfire-dsl
 */
@CompileStatic
class PartitionResolverBuilder<K,V> extends CacheCallbackBuilder<PartitionResolver>{

    PartitionResolverBuilder getRoutingObject(Closure c){
        appendCallback 'getRoutingObject', c
        this
    }

    PartitionResolverBuilder getName(Closure c){
        appendCallback 'getName', c
        this
    }
}
