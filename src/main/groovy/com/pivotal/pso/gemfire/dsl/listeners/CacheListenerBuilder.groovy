package com.pivotal.pso.gemfire.dsl.listeners

import com.gemstone.gemfire.cache.CacheListener
import com.gemstone.gemfire.cache.EntryEvent
import com.gemstone.gemfire.cache.RegionEvent
import com.gemstone.gemfire.cache.TransactionListener
import com.pivotal.pso.gemfire.dsl.support.CacheCallbackBuilder
import groovy.transform.CompileStatic

/**
 * Author: smaldini
 * Date: 2/10/13
 * Project: gemfire-dsl
 */
@CompileStatic
class CacheListenerBuilder extends CacheCallbackBuilder<CacheListener>{

    CacheListenerBuilder afterRegionLive(Closure c){
        adapter.afterRegionLive = c
        this
    }

    CacheListenerBuilder afterRegionCreate(Closure c){
        adapter.afterRegionCreate = c
        this
    }

    CacheListenerBuilder afterRegionClear(Closure c){
        adapter.afterRegionClear = c
        this
    }
    CacheListenerBuilder afterRegionDestroy(Closure c){
        adapter.afterRegionDestroy = c
        this
    }

    CacheListenerBuilder afterRegionInvalidate(Closure c){
        adapter.afterRegionInvalidate = c
        this
    }

    CacheListenerBuilder afterDestroy(Closure c){
        adapter.afterDestroy = c
        this
    }
    CacheListenerBuilder afterInvalidate(Closure c){
        adapter.afterInvalidate = c
        this
    }

    CacheListenerBuilder afterUpdate(Closure c){
        adapter.afterUpdate = c
        this
    }

    CacheListenerBuilder afterCreate(Closure c){
        adapter.afterCreate = c
        this
    }

}
