package com.pivotal.pso.gemfire.dsl

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.InterestRegistrationListener
import com.gemstone.gemfire.cache.server.CacheServer
import com.gemstone.gemfire.cache.server.ClientSubscriptionConfig
import com.gemstone.gemfire.cache.server.ServerLoadProbe
import com.pivotal.pso.gemfire.dsl.support.CacheCallbackBuilder
import com.pivotal.pso.gemfire.dsl.support.DSLUtils
import groovy.transform.CompileStatic

import static com.gemstone.gemfire.cache.server.CacheServer.*
import static groovy.lang.Closure.DELEGATE_FIRST

/**
 * Author: smaldini
 * Date: 2/10/13
 * Project: gemfire-dsl
 */
@CompileStatic
class ServerBuilder {


    final static NONE = 'none'
    final static MEM = 'mem'
    final static ENTRY = 'entry'

    int port = DEFAULT_PORT
    int maxConnections = DEFAULT_MAX_CONNECTIONS
    int maxThreads = DEFAULT_MAX_THREADS
    boolean notifyBySubscription = DEFAULT_NOTIFY_BY_SUBSCRIPTION
    int socketBufferSize = DEFAULT_SOCKET_BUFFER_SIZE
    int maxTimeBetweenPings = DEFAULT_MAXIMUM_TIME_BETWEEN_PINGS
    int maxMessageCount = DEFAULT_MAXIMUM_MESSAGE_COUNT
    int messageTimeToLive = DEFAULT_MESSAGE_TIME_TO_LIVE
    String[] serverGroups = DEFAULT_GROUPS
    ServerLoadProbe serverLoadProbe = DEFAULT_LOAD_PROBE
    long loadPollInterval = DEFAULT_LOAD_POLL_INTERVAL
    String bindAddress = DEFAULT_BIND_ADDRESS
    String hostNameForClients = DEFAULT_HOSTNAME_FOR_CLIENTS
    Set<InterestRegistrationListener> listeners = []

    String evictionPolicy = ClientSubscriptionConfig.DEFAULT_EVICTION_POLICY
    int subscriptionCapacity = ClientSubscriptionConfig.DEFAULT_CAPACITY
    File subscriptionDiskStore

    CacheServer createCacheServer(Cache c) {

        CacheServer cs = c.addCacheServer()
        cs.bindAddress = bindAddress
        cs.groups = serverGroups
        cs.hostnameForClients = hostNameForClients
        cs.loadPollInterval = loadPollInterval
        cs.loadProbe = serverLoadProbe
        cs.maxConnections = maxConnections
        cs.maximumMessageCount = maxMessageCount
        cs.maximumTimeBetweenPings = maxTimeBetweenPings
        cs.maxThreads = maxThreads
        cs.messageTimeToLive = messageTimeToLive
        cs.notifyBySubscription = notifyBySubscription
        cs.port = port
        cs.socketBufferSize = socketBufferSize

        for (listener in listeners) {
            cs.registerInterestRegistrationListener listener
        }

        // client config
        def config = cs.clientSubscriptionConfig
        config.capacity = subscriptionCapacity
        config.evictionPolicy = evictionPolicy
        config.diskStoreName = subscriptionDiskStore?.canonicalPath

        cs.start()

        addShutdownHook {
            cs.stop()
        }

        cs
    }


    static class ServerLoadProbeBuilder extends CacheCallbackBuilder<ServerLoadProbe>{
        ServerLoadProbeBuilder open(Closure c){
            adapter.open = c
            this
        }

        ServerLoadProbeBuilder getLoad(Closure c){
            adapter.getLoad = c
            this
        }
    }

    ServerLoadProbe serverLoadProbe(@DelegatesTo(value=ServerLoadProbeBuilder, strategy=DELEGATE_FIRST) Closure closure){
        def builder = new ServerLoadProbeBuilder()
        DSLUtils.delegateFirstAndRun builder, closure
        serverLoadProbe = builder.create()
    }

}
