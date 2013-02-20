package com.pivotal.pso.gemfire.dsl.listeners

import com.gemstone.gemfire.cache.TransactionListener
import com.pivotal.pso.gemfire.dsl.support.CacheCallbackBuilder
import groovy.transform.CompileStatic

/**
 * Author: smaldini
 * Date: 2/10/13
 * Project: gemfire-dsl
 */
@CompileStatic
class TransactionListenerBuilder extends CacheCallbackBuilder<TransactionListener>{

    TransactionListenerBuilder afterCommit(Closure c){
        adapter.afterCommit = c
        this
    }

    TransactionListenerBuilder afterFailedCommit(Closure c){
        adapter.afterFailedCommit = c
        this
    }

    TransactionListenerBuilder afterRollback(Closure c){
        adapter.afterRollback = c
        this
    }

}
