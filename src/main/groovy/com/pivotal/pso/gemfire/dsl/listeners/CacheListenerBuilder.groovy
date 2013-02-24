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
class CacheListenerBuilder<K,V> extends CacheCallbackBuilder<CacheListener>{

    CacheListenerBuilder afterRegionLive(Closure c){
        appendCallback 'afterRegionLive', c
        this
    }

    CacheListenerBuilder afterRegionCreate(Closure c){
        appendCallback 'afterRegionCreate', c
        this
    }

    CacheListenerBuilder afterRegionClear(Closure c){
        appendCallback 'afterRegionClear', c
        this
    }
    CacheListenerBuilder afterRegionDestroy(Closure c){
        appendCallback 'afterRegionDestroy', c
        this
    }

    CacheListenerBuilder afterRegionInvalidate(Closure c){
        appendCallback 'afterRegionInvalidate', c
        this
    }

    CacheListenerBuilder afterDestroy(Closure c){
        appendCallback 'afterDestroy', c
        this
    }
    CacheListenerBuilder afterInvalidate(Closure c){
        appendCallback 'afterInvalidate', c
        this
    }

    CacheListenerBuilder afterUpdate(Closure c){
        appendCallback 'afterUpdate', c
        this
    }

    CacheListenerBuilder afterCreate(Closure c){
        appendCallback 'afterCreate', c
        this
    }

}
