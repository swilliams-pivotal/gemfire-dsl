package com.pivotal.pso.gemfire.dsl;

import static com.gemstone.gemfire.cache.RegionShortcut.PARTITION
import static com.gemstone.gemfire.cache.RegionShortcut.REPLICATE
import static org.junit.Assert.*

import java.util.concurrent.TimeUnit

import org.junit.Test

import com.gemstone.gemfire.cache.EntryEvent


class GridBuilderTest {

    @Test
    public void test() throws Exception {
        def listener3 = [
            afterCreate: { e->
                println "3.afterCreate: ${e}" // DOES fire.
                send(e)
            },
            afterDestroy: { e->
                println "3.afterDestroy: ${e}"
            },
            afterRegionCreate: { e->
                println "3.afterRegionCreate: ${e}"
            },
            afterRegionDestroy: { e->
                println "3.afterRegionDestroy: ${e}"
            }
        ]

// FIXME this listener doesn't work correctly, the dispatcher doesn't like it
//        def listener4 = {
//            afterCreate: { e->
//                println "4.afterCreate: $(e}" // Does NOT fire, why?
//                // send(e)
//            }
//            afterDestroy: {
//                println "4.afterDestroy: ${it}"
//            }
//            afterRegionCreate: {
//                println "4.afterRegionCreate: ${it}" // DOES fire.
//            }
//            afterRegionDestroy: {
//                println "4.afterRegionDestroy: ${it}"
//            }
//        }

        def listener5 = {
            afterCreate { e->
                // println "5.afterCreate: ${e}" // Does NOT fire, why?
                send(e)
            }
            afterDestroy { e->
                println "5.afterDestroy: ${e}"
            }
            afterRegionCreate { e->
                println "5.afterRegionCreate: ${e}" // DOES fire.
            }
            afterRegionDestroy { e->
                println "5.afterRegionDestroy: ${e}"
            }
        }

        def builder = new GridBuilder()
        def cache = builder.gemfire {
            properties (
                'jmx-manager': 'true',
                'jmx-manager-start': 'false',
                'bind-address': '127.0.0.1',
                'license-working-dir': System.getProperty('user.dir') + '/license'
            )
            pdx {
                diskStore 'foo'
                unreadFields false
                persistent false
                readSerialized false
                serializer 'dummy.DummyPdxSerializer'
            }
            cache {
                server 'server1', {
                    bindAddress '127.0.0.1'
                }

                region 'region1', shortcut: REPLICATE, bob:'foo', {
                    listener (
                        afterCreate: { EntryEvent e->
                            // println "1.afterCreate: ${e}"  // DOES fire.
                            send(e)
                        },
                        afterDestroy: { e->
                            println "1.afterDestroy: ${e}"
                        },
                        afterRegionCreate: { e->
                            println "1.afterRegionCreate: ${e}"
                        },
                        afterRegionDestroy: { e->
                            println "1.afterRegionDestroy: ${e}"
                        }
                    )
                }
                region 'region2', shortcut: REPLICATE, {
                    writer {
                        beforeCreate { e->
                            println "2.beforeCreate: ${e}"  // DOES fire.
                            if (e.key == 'two-2') drop() // delegated utility to throw a CacheWriterException
                        }
                    }
                    listener {
                        afterCreate { e->
                            // println "2.afterCreate: ${e}"
                            cache.getRegion('region3').put('two-b', 'two-b')  // cache is not declared, it's discovered at runtime using propertyMissing(name)
                            region3.put('two-c', 'two-c')  // region3 is not declared, it's discovered at runtime using propertyMissing(name)
                        }
                        afterDestroy { e->
                            println "2.afterDestroy: ${e}"
                        }
                        afterRegionCreate { e->
                            println "2.afterRegionCreate: ${e}"
                        }
                        afterRegionDestroy { e->
                            println "2.afterRegionDestroy: ${e}"
                        }
                    }
                }
                region 'region3', shortcut: REPLICATE, {
                    listener listener3
                }
                region 'region4', shortcut: REPLICATE, {
                    // listener listener4 // FIXME this listener doesn't work correctly, the dispatcher doesn't like it
                }
                region 'region5', shortcut: REPLICATE, {
                    listener listener5
                }
                region 'all', shortcut: PARTITION, {
                    writer {
                        beforeCreate: { e-> println "all.loader.put.beforeCreate($e)" }
                    }
                    listener {
                        afterCreate: { e-> println "all.listener.put.afterCreate($e)" }
                    }
                    partitionAttributes {
                        resolver 'foo', { e->
                            println "all.route:${e}"
                            e.getKey() // return routing object
                        }
                    }
                }
            }
        }

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
