package com.pivotal.pso.gemfire.dsl.region

import com.gemstone.gemfire.cache.GemFireCache
import com.gemstone.gemfire.cache.client.ClientCache
import com.gemstone.gemfire.cache.client.ClientRegionFactory
import com.gemstone.gemfire.cache.client.ClientRegionShortcut
import com.pivotal.pso.gemfire.dsl.Gemfire
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

/**
 * Author: smaldini
 * Date: 2/7/13
 * Project: gemfire-dsl
 */
@CompileStatic
class ClientRegionBuilder<K,V> extends LookupRegionBuilder<K,V> {

    ClientRegionShortcut shortcut

    final List<?> listeners = []

    ClientRegionBuilder(Gemfire gemfire, String name) {
        super(gemfire, name)
    }

    protected postBuilderRun(){
        ClientRegionFactory<K, V> regionFactory = ((ClientCache)cache).
                createClientRegionFactory(shortcut ?: ClientRegionShortcut.PROXY)
    }

}
