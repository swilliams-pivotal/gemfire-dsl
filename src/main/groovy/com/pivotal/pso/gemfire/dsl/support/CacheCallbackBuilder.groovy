package com.pivotal.pso.gemfire.dsl.support

import com.gemstone.gemfire.cache.CacheCallback
import com.pivotal.pso.gemfire.dsl.listeners.MapAdapterBuilder
import groovy.transform.CompileStatic

/**
 * Author: smaldini
 * Date: 2/12/13
 * Project: gemfire-dsl
 */
@CompileStatic
class CacheCallbackBuilder<T extends CacheCallback> extends MapAdapterBuilder<T> {

    MapAdapterBuilder close(Closure c) {
        adapter.close = c
        this
    }
}
