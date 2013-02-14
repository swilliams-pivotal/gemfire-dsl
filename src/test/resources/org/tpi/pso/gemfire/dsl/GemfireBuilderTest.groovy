package org.tpi.pso.gemfire.dsl

import org.junit.Test
import org.tpi.pso.gemfire.dsl.parser.GemfireBuilder;

class GemfireBuilderTest {

    @Test
    public void testStaticCreate() {
        GemfireBuilder.create {
            // fire builder
            gemfire {
                region 'foo', [expiry: 3], { disk file: 'test' }
            }
        }
    }

    @Test
    public void testGemfireClosure() {
        def builder = new GemfireBuilder()
        builder.configure {
            region 'bar', expiry: 10, test: 11
            region 'foo', [expiry: 3], { disk file: 'test' }
            region 'egg', expiry: 3, { disk file: 'test' }
        }
    }

    @Test
    public void testWithClosure() {
        def builder = new GemfireBuilder()
        builder.with { gemfire { region 'foo', expiry: 3 } }
    }

    @Test
    public void testLoadingScript() {
        def builder = new GemfireBuilder()
        builder.build(new File('src/test/resources/test-dsl.groovy'))
    }
}
