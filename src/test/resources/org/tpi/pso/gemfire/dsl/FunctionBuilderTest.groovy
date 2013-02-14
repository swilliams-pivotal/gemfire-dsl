package org.tpi.pso.gemfire.dsl

import org.junit.Test
import org.tpi.pso.gemfire.dsl.parser.FunctionBuilder;




class FunctionBuilderTest {

    @Test
    public void test() {
        new FunctionBuilder().create {
            def func = { e->
//                e to 'bar'
                send 'foo' to 'bar'
//                remove 'foo' from 'bar'
//                put 'foo' inRegion 'bar'
//                put('foo') >> 'bar'
//                put(e) in 'foo' // ???
            }
            region 'foo', one: 1, two: 2, listeners: [func]
        }
    }
}
