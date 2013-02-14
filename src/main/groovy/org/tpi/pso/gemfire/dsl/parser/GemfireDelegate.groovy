package org.tpi.pso.gemfire.dsl.parser

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.distributed.DistributedSystem

class GemfireDelegate {

    private DistributedSystem system

    private Cache cache

    def '-->'(String address) {
        String regionName = address.split('@')[0]
        if (address.indexOf('@') > -1) {
            // targeted
        }
        else {
            // lookup
        }
    }
}
