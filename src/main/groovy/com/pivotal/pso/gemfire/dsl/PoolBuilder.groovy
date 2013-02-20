package com.pivotal.pso.gemfire.dsl

import com.gemstone.gemfire.cache.client.Pool
import com.gemstone.gemfire.cache.client.PoolFactory
import com.gemstone.gemfire.cache.client.PoolManager
import com.gemstone.gemfire.distributed.DistributedSystem
import com.gemstone.gemfire.distributed.internal.InternalDistributedSystem
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j


/**
 * Author: smaldini
 * Date: 2/7/13
 * Project: gemfire-dsl
 */
@CompileStatic
@Slf4j
class PoolBuilder {

    boolean internalPool = true
    final Collection<InetSocketAddress> locators = []
    final Collection<InetSocketAddress> servers = []

    String name

    boolean keepAlive = false

    int freeConnectionTimeout = PoolFactory.DEFAULT_FREE_CONNECTION_TIMEOUT
    long idleTimeout = PoolFactory.DEFAULT_IDLE_TIMEOUT
    int loadConditioningInterval = PoolFactory.DEFAULT_LOAD_CONDITIONING_INTERVAL
    int maxConnections = PoolFactory.DEFAULT_MAX_CONNECTIONS
    int minConnections = PoolFactory.DEFAULT_MIN_CONNECTIONS
    boolean multiUserAuthentication = PoolFactory.DEFAULT_MULTIUSER_AUTHENTICATION
    long pingInterval = PoolFactory.DEFAULT_PING_INTERVAL
    boolean prSingleHopEnabled = PoolFactory.DEFAULT_PR_SINGLE_HOP_ENABLED
    int readTimeout = PoolFactory.DEFAULT_READ_TIMEOUT
    int retryAttempts = PoolFactory.DEFAULT_RETRY_ATTEMPTS
    String serverGroup = PoolFactory.DEFAULT_SERVER_GROUP
    int socketBufferSize = PoolFactory.DEFAULT_SOCKET_BUFFER_SIZE
    int statisticInterval = PoolFactory.DEFAULT_STATISTIC_INTERVAL
    int subscriptionAckInterval = PoolFactory.DEFAULT_SUBSCRIPTION_ACK_INTERVAL
    boolean subscriptionEnabled = PoolFactory.DEFAULT_SUBSCRIPTION_ENABLED
    int subscriptionMessageTrackingTimeout = PoolFactory.DEFAULT_SUBSCRIPTION_MESSAGE_TRACKING_TIMEOUT
    int subscriptionRedundancy = PoolFactory.DEFAULT_SUBSCRIPTION_REDUNDANCY
    boolean threadLocalConnections = PoolFactory.DEFAULT_THREAD_LOCAL_CONNECTIONS


    Pool createPool(Properties props = null) {
        Pool pool

        assert name

        // eagerly initialize cache (if needed)
        if (!InternalDistributedSystem.anyInstance) {
            connectToTemporaryDs props
        }

        // first check the configured pools
        Pool existingPool = PoolManager.find name
        if (existingPool) {
            pool = existingPool
            internalPool = false
            log.debug "Pool '$name' already exists using found instance..."
        } else {
            log.debug "No pool named '$name' found. Creating a new once..."

            if (!locators && !servers) {
                server()
            }

            internalPool = true

            PoolFactory poolFactory = PoolManager.createFactory()

            if (locators) {
                for (connection in locators) {
                    poolFactory.addLocator connection.hostName, connection.port
                }
            }

            if (servers) {
                for (connection in servers) {
                    poolFactory.addServer connection.hostName, connection.port
                }
            }

            poolFactory.freeConnectionTimeout = freeConnectionTimeout
            poolFactory.idleTimeout = idleTimeout
            poolFactory.loadConditioningInterval = loadConditioningInterval
            poolFactory.maxConnections = maxConnections
            poolFactory.minConnections = minConnections
            poolFactory.multiuserAuthentication = multiUserAuthentication
            poolFactory.pingInterval = pingInterval
            poolFactory.PRSingleHopEnabled = prSingleHopEnabled
            poolFactory.readTimeout = readTimeout
            poolFactory.retryAttempts = retryAttempts
            poolFactory.serverGroup = serverGroup
            poolFactory.socketBufferSize = socketBufferSize
            poolFactory.statisticInterval = statisticInterval
            poolFactory.subscriptionEnabled = subscriptionEnabled
            poolFactory.subscriptionAckInterval = subscriptionAckInterval
            poolFactory.subscriptionMessageTrackingTimeout = subscriptionMessageTrackingTimeout
            poolFactory.subscriptionRedundancy = subscriptionRedundancy
            poolFactory.threadLocalConnections = threadLocalConnections

            pool = poolFactory.create name

            if (internalPool && pool) {
                addShutdownHook {
                    if (!pool.destroyed) {
                        pool.releaseThreadLocalConnection()
                        pool.destroy keepAlive
                        log.debug "Destroyed pool '$name'..."
                    }
                }
            }

        }
        pool
    }

    private static InetSocketAddress inetSocketAddress(String name, int port) {
        new InetSocketAddress(name, port)
    }

    PoolBuilder locator(String name = 'localhost', int port = 40404) {
        locators << inetSocketAddress(name, port)
        this
    }

    PoolBuilder server(String name = 'localhost', int port = 40404) {
        servers << inetSocketAddress(name, port)
        this
    }

    /*
	 *  A work around to findOrCreate a pool if no cache has been created yet
	 *  initialize a client-like Distributed System before initializing
	 *  the pool
	 */

    @SuppressWarnings("deprecated")
    static void connectToTemporaryDs(Properties properties) {
        def props = properties ? properties.clone() as Properties : new Properties()
        props['mcast-port'] = '0'
        props['locators'] = ''
        DistributedSystem.connect props
    }
}
