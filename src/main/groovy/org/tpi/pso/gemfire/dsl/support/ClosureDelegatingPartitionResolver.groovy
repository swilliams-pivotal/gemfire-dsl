package org.tpi.pso.gemfire.dsl.support

import groovy.transform.CompileStatic

import com.gemstone.gemfire.cache.EntryOperation
import com.gemstone.gemfire.cache.PartitionResolver


@CompileStatic
public class ClosureDelegatingPartitionResolver<K, V> implements
        PartitionResolver<K, V> {

    private final String name

    private final Closure<?> closure

    public ClosureDelegatingPartitionResolver(String name, Closure<?> closure) {
        this.name = name
        this.closure = closure
    }

    @Override
    public String getName() {
        name
    }

    @Override
    public Object getRoutingObject(EntryOperation<K, V> entryOp) {
        closure.call(entryOp)
    }

    @Override
    public void close() {
        //
    }

}
