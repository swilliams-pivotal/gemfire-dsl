package org.tpi.pso.gemfire.dsl2

import static groovy.lang.Closure.DELEGATE_FIRST
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.RegionFactory
import com.gemstone.gemfire.cache.RegionShortcut


@CompileStatic
class CacheBuilder {

    private Cache cache

    public CacheBuilder(Cache cache) {
        this.cache = cache
    }

    def server(String name, @DelegatesTo(strategy=DELEGATE_FIRST, value=CacheServerBuilder) Closure closure) {
        def builder = new CacheServerBuilder(cache.addCacheServer())
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = DELEGATE_FIRST
        hydrated()
    }

    def region(Map params, String name, @DelegatesTo(strategy=DELEGATE_FIRST, value=RegionBuilder) Closure closure) {

        RegionFactory factory
        if (params['shortcut'] && (params['shortcut'] instanceof String)) {
            def shortcut = RegionShortcut.valueOf((String) params['shortcut'])
            factory = cache.createRegionFactory(shortcut)
        }
        else if (params['shortcut'] && (params['shortcut'] instanceof RegionShortcut)) {
            def shortcut = (RegionShortcut) params['shortcut']
            factory = cache.createRegionFactory(shortcut)
        }
        else {
            factory = cache.createRegionFactory()
        }

        def builder = new RegionBuilder(factory, name, params)
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = DELEGATE_FIRST
        hydrated()

        factory.create(name)
    }

}
