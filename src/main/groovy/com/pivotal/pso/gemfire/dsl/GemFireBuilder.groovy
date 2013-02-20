package com.pivotal.pso.gemfire.dsl

import static groovy.lang.Closure.DELEGATE_FIRST
import static groovy.lang.Closure.OWNER_FIRST
import groovy.transform.CompileStatic

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.CacheFactory
import com.gemstone.gemfire.cache.RegionShortcut


@CompileStatic
class GemFireBuilder {

    private CacheFactory cacheFactory = new CacheFactory()

    def properties(Map<String, String> params) {
        params.each { String name, String value ->
            cacheFactory.set(name, value)
        }
    }

    def pdx(@DelegatesTo(strategy=DELEGATE_FIRST, value=PdxBuilder) Closure<Object> closure) {
        def builder = new PdxBuilder(cacheFactory)
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = DELEGATE_FIRST
        hydrated()
    }

    def cache(@DelegatesTo(strategy=DELEGATE_FIRST, value=CacheBuilder) Closure<Object> closure) {
        Cache cache = cacheFactory.create()

        cache.createRegionFactory(RegionShortcut.REPLICATE)
            .setKeyConstraint(String.class)
            .setValueConstraint(byte[].class)
            .setCacheWriter(new ClosureClassLoaderCacheWriter())
            // .addCacheListener(new ClosureClassLoaderCacheListener())
            .create('closure.classloader')

        def builder = new CacheBuilder(cache)
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = DELEGATE_FIRST
        hydrated()

        cache
    }

}
