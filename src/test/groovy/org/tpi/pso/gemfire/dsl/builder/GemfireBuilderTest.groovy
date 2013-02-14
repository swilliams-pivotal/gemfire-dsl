package org.tpi.pso.gemfire.dsl.builder;

import static com.gemstone.gemfire.cache.RegionShortcut.PARTITION
import static com.gemstone.gemfire.cache.RegionShortcut.REPLICATE
import static org.junit.Assert.*

import java.util.concurrent.CountDownLatch

import org.junit.After
import org.junit.Before
import org.junit.Test

import com.gemstone.gemfire.cache.Cache


class GemfireBuilderTest {

    private Cache gemfire

    @Before
    public void setup() throws Exception {
        def builder = new GemfireBuilder()
        gemfire = builder.build {
            def listener1 = [
                afterCreate: { e-> 
                    send e.key + "-from1", e.newValue to 'two'
                    send e.key + "-from1", e.newValue to 'two'
                }
            ]
            def listener2 = [
                afterCreate: { e-> 
                    send(e.key + "-created2", e.newValue).to 'three'
                    println "afterCreate1: " + e
                },
                afterUpdate: { e-> 
                    send(e.key + "-updated2", e.newValue).to 'three'
                    println "afterUpdate1: " + e
                }
            ]
            def listener3 = { String type, event->
                println "event3: " + event
            }
            def listener4 = { String type, event->
                println "event4: " + event
            }
            // ------------------------------------------------------------------
            
            // ------------------------------------------------------------------
            properties (
                'jmx-manager': 'true',
                'jmx-manager-start': 'false',
                'bind-address': '127.0.0.1',
                'license-working-dir': System.getProperty('user.dir') + '/license',
//                'license-application-cache': 'dynamic',
//                'license-data-management': 'dynamic'
            )
            locator port: 40000
            cache name: 'label', pdx: [persistent: false, ignoreUnreadFields: false], {
                server 'cs1'
                function 'func1', { args-> println "func1 called with ${args}" }
                function 'func2', { args-> println "func2 called with ${args}" }
                region 'one', shortcut: REPLICATE, {
                    listener (
                        afterCreate: { e-> println "created: " + e }
                    )
                }
                region 'two', shortcut: REPLICATE, {
                    // listener listener2
                }
                region 'three', shortcut: REPLICATE, {
                    // listener listener3
                }
                region 'four', shortcut: REPLICATE, {
                    // listener listener4
                }
                gatewayHub 'hub1', 39999, bindAddress: '127.0.0.1', {
                    gateway 'gateway1', [
                        endpoints: [],
                        listeners: []
                    ]
                }
            }
        }
    }

    @After
    public void teardown() {
        gemfire?.close()
    }

    @Test
    public void testCreate() throws Exception {
        def latch = new CountDownLatch(1)

        println "putting into region 'one'"
        gemfire?.getRegion('one')?.put("test", 'foo')

        println "executing function in region 'two'"
        // Execution e = FunctionService.onMember(gemfire.getDistributedSystem());
        // Execution e = FunctionService.onMember(gemfire.getRegion('two'))
        // e.execute('func1')

        // latch.await(20000L, TimeUnit.MILLISECONDS)
    }
}

