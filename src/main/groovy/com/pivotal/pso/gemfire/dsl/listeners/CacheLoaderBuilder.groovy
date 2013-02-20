package com.pivotal.pso.gemfire.dsl.listeners

import com.gemstone.gemfire.cache.CacheLoader
import com.gemstone.gemfire.cache.TransactionListener
import com.pivotal.pso.gemfire.dsl.support.CacheCallbackBuilder
import groovy.transform.CompileStatic

/**
 * Author: smaldini
 * Date: 2/10/13
 * Project: gemfire-dsl
 */
@CompileStatic
class CacheLoaderBuilder extends CacheCallbackBuilder<CacheLoader>{

    CacheLoaderBuilder load(Closure c){
        adapter.load = c
        this
    }

}
