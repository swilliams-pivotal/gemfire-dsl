package com.pivotal.pso.gemfire.dsl.tests

import com.gemstone.gemfire.cache.CacheFactory
import com.gemstone.gemfire.cache.EntryEvent
import com.gemstone.gemfire.cache.GemFireCache
import com.gemstone.gemfire.cache.TransactionListener
import com.gemstone.gemfire.cache.client.ClientCacheFactory
import com.gemstone.gemfire.cache.server.CacheServer
import com.pivotal.pso.gemfire.dsl.Gemfire
import spock.lang.Specification
import com.gemstone.gemfire.cache.RegionShortcut

import java.util.concurrent.TimeUnit

/**
 * Author: smaldini
 * Date: 1/27/13
 * Project: gemfire-dsl
 */
class SimpleConfigurationTests extends Specification {

    GemFireCache cache

    void cleanup() {
        cache?.close()
    }

    Map<String, String> createData() {
        [
                'smaldini': 'Stephane Maldini',
                'pidster': 'Stuart Williams',
                'glaforge': 'Guillaume Laforge',
                'cchampeau': "Cedric Champeau"
        ]
    }

    def 'Can load a standalone Member Cache DSL'() {
        when:
        def gs = Gemfire.build(new File('src/test/resources/GemfireTest.groovy'))
        cache = gs.cache

        then:
        cache == CacheFactory.anyInstance
    }

    def 'Can add a Transaction Listener'() {
        when:
        def tx
        def gs = Gemfire.create {
            tx = transactionListener {
                afterCommit {}
            }
        }

        cache = gs.cache

        then:
        tx instanceof TransactionListener
        gs.transactionListeners.size() == 1
    }

    def 'Can add a Transaction Writer'() {
        when:
        def tx
        def gs = Gemfire.create {
            tx = transactionWriter {}
        }
        cache = gs.cache

        then:
        gs.transactionWriter == tx
    }

    def "Can create a simple Client cache"() {
        when:
        def gs = Gemfire.create {
            client()
        }

        cache = gs.cache

        then:
        cache == ClientCacheFactory.anyInstance

    }

    def "Can create a simple Cache with Server"() {
        when:
        CacheServer cacheServer = null
        def gs = Gemfire.create {
            cacheServer = server()
        }
        cache = gs.cache

        then:
        cacheServer.running
    }

    def "Can create a simple Region"() {
        when:
        Gemfire.create {
            region 'test', {
                data << createData()
            }
        }

        then:
        def region = CacheFactory.anyInstance.getRegion 'test'
        region['pidster']
        region['smaldini']
    }

    def "Simple Region Scenario"() {
        setup:
        def listener3 = {
                afterCreate { e->
                    println "3.afterCreate: ${e}" // DOES fire.
                    send(e)
                }
                afterDestroy { e->
                    println "3.afterDestroy: ${e}"
                }
                afterRegionCreate { e->
                    println "3.afterRegionCreate: ${e}"
                }
                afterRegionDestroy: { e->
                    println "3.afterRegionDestroy: ${e}"
                }
        }

        when:
        def gs = Gemfire.create(
                'jmx-manager': 'true',
                'jmx-manager-start': 'false',
                'bind-address': '127.0.0.1',
                'license-working-dir': System.properties['user.dir'] + '/license'
        ) {
            pdx {
                diskStore = 'foo'
                ignoreUnreadFields = false
                persistent = false
                readSerialized = false
                serializer = new dummy.DummyPdxSerializer()
            }

            server {
                bindAddress = '127.0.0.1'
            }

            replicatedRegion 'region1', {
                listener{
                        afterCreate { EntryEvent e ->
                            println "1.afterCreate: ${e}"  // DOES fire.
                            send(e)
                        }
                        afterDestroy { EntryEvent e ->
                            println "1.afterDestroy: ${e}"
                        }
                        afterRegionCreate {EntryEvent e ->
                            println "1.afterRegionCreate: ${e}"
                        }
                        afterRegionDestroy {EntryEvent e ->
                            println "1.afterRegionDestroy: ${e}"
                        }
                }
            }
            replicatedRegion 'region2', {
                writer {
                    beforeCreate { e ->
                        println "2.beforeCreate: ${e}"  // DOES fire.
                        if (e.key == 'two-2') drop() // delegated utility to throw a CacheWriterException
                    }
                }
                listener {
                    afterCreate { e ->
                        // println "2.afterCreate: ${e}"
                        cache.getRegion('region3').put('two-b', 'two-b')  // cache is not declared, it's discovered at runtime using propertyMissing(name)
                        region3.put('two-c', 'two-c')  // region3 is not declared, it's discovered at runtime using propertyMissing(name)
                    }
                    afterDestroy { e ->
                        println "2.afterDestroy: ${e}"
                    }
                    afterRegionCreate { e ->
                        println "2.afterRegionCreate: ${e}"
                    }
                    afterRegionDestroy { e ->
                        println "2.afterRegionDestroy: ${e}"
                    }
                }
            }
            replicatedRegion 'region3', {
                listener listener3
            }
            replicatedRegion 'region4'
            replicatedRegion 'region5'
            partitionRegion 'all', {
                writer {
                    beforeCreate {  println "all.loader.put.beforeCreate($it)" }
                }
                listener {
                    afterCreate {  println "all.listener.put.afterCreate($it)" }
                }
                /*partitionAttributes {
                    resolver 'foo', { e ->
                        println "all.route:${e}"
                        e.getKey() // return routing object
                    }
                }*/
            }
        }

        cache = gs.cache

        then:
        cache.getRegion('region1').put('one', 'one', 'one')
        cache.getRegion('region2').put('two', 'two', 'two')
        cache.getRegion('region2').put('two-2', 'two-2', 'two-2')
        cache.getRegion('region3').put('three', 'three', 'three')
        cache.getRegion('region4').put('four', 'four', 'four')
        cache.getRegion('region5').put('five', 'five', 'five')
        cache.getRegion('all').put('all', 'all', 'all')

//        cache.getRegion('all').getAttributesMutator().addCacheListener {}
//        regions.all.listeners << {}

        TimeUnit.SECONDS.sleep(10)
    }

}