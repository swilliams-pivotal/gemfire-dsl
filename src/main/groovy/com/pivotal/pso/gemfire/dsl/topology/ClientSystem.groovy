package com.pivotal.pso.gemfire.dsl.topology

import com.gemstone.gemfire.cache.GemFireCache
import com.gemstone.gemfire.cache.client.ClientCache
import com.gemstone.gemfire.cache.client.ClientCacheFactory
import com.gemstone.gemfire.cache.client.Pool
import com.gemstone.gemfire.cache.client.PoolManager
import com.pivotal.pso.gemfire.dsl.Gemfire
import com.pivotal.pso.gemfire.dsl.PoolBuilder
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 * Author: smaldini
 * Date: 2/10/13
 * Project: gemfire-dsl
 */
@CompileStatic
@Slf4j
class ClientSystem extends MemberSystem {

    String poolName
    Pool pool

    ClientSystem(Gemfire g, String poolName) {
        super(g)
        this.poolName = poolName
    }

    ClientSystem(Gemfire g, Pool pool) {
        super(g)
        this.pool = pool
    }

    @Override
    GemFireCache createWithFactory() {
        def ccf = (g.props ?
            new ClientCacheFactory(g.props) :
            new ClientCacheFactory())

        initializePool ccf

        if(g.pdxBuilder){
            ccf.pdxSerializer = g.pdxBuilder.serializer
            ccf.pdxDiskStore = g.pdxBuilder.diskStore
            ccf.pdxIgnoreUnreadFields = g.pdxBuilder.ignoreUnreadFields
            ccf.pdxPersistent = g.pdxBuilder.persistent
            ccf.pdxReadSerialized = g.pdxBuilder.readSerialized
        }

        GemFireCache cache = ccf.create()



        cache
    }

    private initializePool(ClientCacheFactory ccf) {
        Pool p = pool

        if (!p) {
            if (poolName) {
                p = PoolManager.find poolName
            }

            // Bind this client cache to a pool that hasn't been created yet.
            if (!p) {
                PoolBuilder.connectToTemporaryDs g.props
            }
        }

        if (p) {
            // copy the pool settings - this way if the pool is not found, at
            // least the cache will have a similar config
            ccf.poolFreeConnectionTimeout = p.freeConnectionTimeout
            ccf.poolIdleTimeout = p.idleTimeout
            ccf.poolLoadConditioningInterval = p.loadConditioningInterval
            ccf.poolMaxConnections = p.maxConnections
            ccf.poolMinConnections = p.minConnections
            ccf.poolMultiuserAuthentication = p.multiuserAuthentication
            ccf.poolPingInterval = p.pingInterval
            ccf.poolPRSingleHopEnabled = p.PRSingleHopEnabled
            ccf.poolReadTimeout = p.readTimeout
            ccf.poolRetryAttempts = p.retryAttempts
            ccf.poolServerGroup = p.serverGroup
            ccf.poolSocketBufferSize = p.socketBufferSize
            ccf.poolStatisticInterval = p.statisticInterval
            ccf.poolSubscriptionAckInterval = p.subscriptionAckInterval
            ccf.poolSubscriptionEnabled = p.subscriptionEnabled
            ccf.poolSubscriptionMessageTrackingTimeout = p.subscriptionMessageTrackingTimeout
            ccf.poolSubscriptionRedundancy = p.subscriptionRedundancy
            ccf.poolThreadLocalConnections = p.threadLocalConnections

            def locators = p.locators
            if (locators) {
                for (inet in locators) {
                    ccf.addPoolLocator inet.hostName, inet.port
                }
            }

            def servers = p.servers
            if (servers) {
                for (inet in servers) {
                    ccf.addPoolServer inet.hostName, inet.port
                }
            }
        }
    }

    @Override
    GemFireCache fetchCache() {
        ClientCacheFactory.anyInstance
    }

    @Override
    void setupCache(GemFireCache _c) {
        ClientCache clientCache = (ClientCache)_c

        if( g.readyForEvents && !clientCache.isClosed()){
            try {
                clientCache.readyForEvents()
            }catch(IllegalStateException ex){
            }
        }
    }
}
