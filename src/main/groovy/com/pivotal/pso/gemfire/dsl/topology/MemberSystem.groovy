package com.pivotal.pso.gemfire.dsl.topology

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.CacheFactory
import com.gemstone.gemfire.cache.GemFireCache
import com.pivotal.pso.gemfire.dsl.Gemfire
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 * Author: smaldini
 * Date: 2/10/13
 * Project: gemfire-dsl
 */
@CompileStatic
@Slf4j
class MemberSystem {

    Gemfire g

    MemberSystem(Gemfire g){
        this.g = g
    }

    void setupCache(GemFireCache _c){
        def c = _c as Cache
        c.lockLease = g.lockLease ?: c.lockLease
        c.lockTimeout = g.lockTimeout ?: c.lockTimeout
        c.searchTimeout = g.searchTimeout ?: c.searchTimeout
        c.messageSyncInterval = g.messageSyncInterval ?: c.messageSyncInterval
        c.gatewayConflictResolver = g.gatewayConflictResolver ?: c.gatewayConflictResolver
    }

    GemFireCache fetchCache(){
        CacheFactory.anyInstance
    }

    GemFireCache createWithFactory(){
        def cacheFactory = g.props ?
            new CacheFactory(g.props) :
            new CacheFactory()

        if(g.pdxBuilder){
            cacheFactory.pdxSerializer = g.pdxBuilder.serializer
            cacheFactory.pdxDiskStore = g.pdxBuilder.diskStore
            cacheFactory.pdxIgnoreUnreadFields = g.pdxBuilder.ignoreUnreadFields
            cacheFactory.pdxPersistent = g.pdxBuilder.persistent
            cacheFactory.pdxReadSerialized = g.pdxBuilder.readSerialized
        }

        cacheFactory.create()
    }
}
