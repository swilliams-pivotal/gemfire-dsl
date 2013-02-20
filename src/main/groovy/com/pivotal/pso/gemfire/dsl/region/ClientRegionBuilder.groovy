package com.pivotal.pso.gemfire.dsl.region

import com.gemstone.gemfire.cache.GemFireCache
import com.gemstone.gemfire.cache.client.ClientCache
import com.gemstone.gemfire.cache.client.ClientRegionFactory
import com.gemstone.gemfire.cache.client.ClientRegionShortcut
import groovy.transform.CompileStatic

/**
 * Author: smaldini
 * Date: 2/7/13
 * Project: gemfire-dsl
 */
@CompileStatic
class ClientRegionBuilder<K,V> extends LookupRegionBuilder<K,V> {

    ClientRegionShortcut shortcut

    final List<?> listeners = []

    protected postBuilderRun(){
        ClientRegionFactory<K, V> regionFactory = ((ClientCache)cache).
                createClientRegionFactory(shortcut ?: ClientRegionShortcut.PROXY)
    }

    ClientRegionBuilder(GemFireCache cache, String name) {
        super(cache, name)
    }
}
