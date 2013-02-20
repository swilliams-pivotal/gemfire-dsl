package com.pivotal.pso.gemfire.dsl.tests

import com.pivotal.pso.gemfire.dsl.Gemfire
import groovy.transform.CompileStatic

/**
 * Author: smaldini
 * Date: 2/11/13
 * Project: gemfire-dsl
 *
 * Verify static DSLBuilder compilation
 */
@CompileStatic
class CompleteStaticDSL {
    Gemfire build(){
        Gemfire.create {
            dynamic{
                persistBackup = false
                diskDir = new File(".")
                registerInterest = false
                poolName = null
            }

            copyOnRead = null
            lockLease = null
            lockTimeout = null
            searchTimeout = null
            messageSyncInterval = null

            criticalHeapPercentage = null
            evictionHeapPercentage = null

            gatewayConflictResolver{ a,b->
            }

            datasource {
                attrs.'some-property' = 'foo'
                attrs.some = 'foo'

                prop 'test', 'test', 'test'
                prop 'test', 'test', 'test'
                prop 'test', 'test', 'test'
            }

            transactionWriter {}

            transactionListener {
                afterCommit {}
                afterFailedCommit {}
                afterRollback {}
                close{}
            }

            def somePool = pool 'somePool', {
                subscriptionEnabled = true
                freeConnectionTimeout = 1000
                idleTimeout = 1000
                loadConditioningInterval = 1000
                maxConnections = 10
                minConnections = 1
                multiUserAuthentication = true
                pingInterval = 100
                prSingleHopEnabled = true
                readTimeout = 100
                retryAttempts = 3
                serverGroup = 'test'
                socketBufferSize = 123
                statisticInterval = 123
                subscriptionAckInterval = 100
                subscriptionMessageTrackingTimeout = 1000
                subscriptionRedundancy = true
                threadLocalConnections = true
            }

            client somePool
            //client 'somePool'

            server {
                port = 1234
                maxConnections = 1234
                maxThreads = 12
                notifyBySubscription = true
                socketBufferSize = 1234
                maxTimeBetweenPings = 1234
                maxMessageCount = 1234
                messageTimeToLive = 1234
                serverGroups = ['DEFAULT_GROUPS']
                //ServerLoadProbe serverLoadProbe = DEFAULT_LOAD_PROBE
                loadPollInterval = 123
                bindAddress = 'localhost'
                hostNameForClients = 'localhost'
                //Set<InterestRegistrationListener> listeners = []
                evictionPolicy = MEM
                subscriptionCapacity = 1
                subscriptionDiskStore = new File('.')
            }

            region('toto'){
                data.lol = 'xx'
            }
        }
    }
}
