package com.pivotal.pso.gemfire.dsl.region

import com.gemstone.gemfire.cache.GemFireCache
import com.gemstone.gemfire.cache.Region
import com.pivotal.pso.gemfire.dsl.support.DSLUtils
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import static groovy.lang.Closure.DELEGATE_FIRST

/**
 * Author: smaldini
 * Date: 2/7/13
 * Project: gemfire-dsl
 */
@CompileStatic
@Slf4j
class LookupRegionBuilder<K, V> {

    Region<K, V> region

    final Map<K, V> data = [:]
    final GemFireCache cache
    final String name

    LookupRegionBuilder(GemFireCache cache, String name) {
        this.cache = cache
        this.name = name
    }

    protected postBuilderRun() {}

    protected postInitData() {
        if(data)
            region?.putAll data
    }

    /**
     * Root DSL to find Or Create a Gemfire Region
     * @param GemFireCache cache to use
     * @param String region name
     * @param c DSL
     * @return Region
     */
    Region<K, V> findOrCreate(@DelegatesTo(strategy = DELEGATE_FIRST, value = LookupRegionBuilder) Closure c = null
    ) {
        region = lookupRegion()

        if (c && !region) {
            log.debug "Creating region [$name] through ${this.class.simpleName}"
            DSLUtils.delegateFirstAndRun this, c
            postBuilderRun()
            postInitData()
        } else {
            log.debug "Returning existing region [$name] - $region"
        }

        region
    }

    /**
     * Find a Gemfire Region
     * @param GemFireCache cache to use
     * @param String region name
     * @return Region
     */
    static <K, V> Region<K, V> find(String name, GemFireCache cache) {
        new LookupRegionBuilder<K, V>(cache, name).lookupRegion()
    }


    private Region<K, V> lookupRegion() {
        cache.getRegion name
    }

}
