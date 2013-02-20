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
class CacheWriterBuilder extends CacheCallbackBuilder<CacheWriter> {

    CacheWriterBuilder beforeUpdate(Closure c) {
        adapter.beforeUpdate = c
        this
    }

    CacheWriterBuilder beforeCreate(Closure c) {
        adapter.beforeCreate = c
        this
    }

    CacheWriterBuilder beforeDestroy(Closure c) {
        adapter.beforeDestroy = c
        this
    }

    CacheWriterBuilder beforeRegionDestroy(Closure c) {
        adapter.beforeRegionDestroy = c
        this
    }

    CacheWriterBuilder beforeRegionClear(Closure c) {
        adapter.beforeRegionClear = c
        this
    }

}
