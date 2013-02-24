package com.pivotal.pso.gemfire.dsl.listeners

import com.gemstone.gemfire.cache.CacheWriter
import com.gemstone.gemfire.cache.TransactionListener
import com.pivotal.pso.gemfire.dsl.support.CacheCallbackBuilder
import groovy.transform.CompileStatic

/**
 * Author: smaldini
 * Date: 2/10/13
 * Project: gemfire-dsl
 */
@CompileStatic
class CacheWriterBuilder<K,V> extends CacheCallbackBuilder<CacheWriter> {

    CacheWriterBuilder beforeUpdate(Closure c) {
        appendCallback 'beforeUpdate', c
        this
    }

    CacheWriterBuilder beforeCreate(Closure c) {
        appendCallback 'beforeCreate', c
        this
    }

    CacheWriterBuilder beforeDestroy(Closure c) {
        appendCallback 'beforeDestroy', c
        this
    }

    CacheWriterBuilder beforeRegionDestroy(Closure c) {
        appendCallback 'beforeRegionDestroy', c
        this
    }

    CacheWriterBuilder beforeRegionClear(Closure c) {
        appendCallback 'beforeRegionClear', c
        this
    }

}
