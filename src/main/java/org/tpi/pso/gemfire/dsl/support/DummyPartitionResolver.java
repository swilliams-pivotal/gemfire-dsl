package org.tpi.pso.gemfire.dsl.support;

import com.gemstone.gemfire.cache.EntryOperation;
import com.gemstone.gemfire.cache.PartitionResolver;

public class DummyPartitionResolver<K, V> implements PartitionResolver<K, V> {

    @Override
    public void close() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getRoutingObject(EntryOperation<K, V> ope) {
        return ope.getKey();
    }


}
