package com.pivotal.pso.gemfire.dsl

import com.gemstone.gemfire.cache.server.CacheServer
import com.gemstone.gemfire.cache.server.ServerLoadProbe

class CacheServerBuilder {

    private CacheServer server

    public CacheServerBuilder(CacheServer server) {
        this.server = server
    }

    def bindAddress(String bindAddress) {
        server.setBindAddress(bindAddress)
        this
    }

    def hostnameForClients(String hostnameForClients) {
        server.setHostnameForClients(hostnameForClients)
        this
    }

    def loadPollInterval(int loadPollInterval) {
        server.setLoadPollInterval(loadPollInterval)
        this
    }

    def loadProbe(String loadProbe) {
        Class clazz = Class.forName(loadProbe)
        ServerLoadProbe probe = clazz.newInstance() as ServerLoadProbe
        server.setLoadProbe(probe)
        this
    }

    def maxConnections(int maxConnections) {
        server.setMaxConnections(maxConnections)
        this
    }

    def maximumMessageCount(int maximumMessageCount) {
        server.setMaximumMessageCount(maximumMessageCount)
        this
    }

    def maximumTimeBetweenPings(int maximumTimeBetweenPings) {
        server.setMaximumTimeBetweenPings(maximumTimeBetweenPings)
        this
    }

    def maxThreads(int maxThreads) {
        server.setMaxThreads(maxThreads)
        this
    }

    def messageTimeToLive(int messageTimeToLive) {
        server.setMessageTimeToLive(messageTimeToLive)
        this
    }

    def missingMethod(String name, args) {
        println "CacheServerBuilder $name(${args})"
    }
}
