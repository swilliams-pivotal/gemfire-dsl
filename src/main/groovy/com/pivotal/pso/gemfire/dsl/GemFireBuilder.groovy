package com.pivotal.pso.gemfire.dsl

import static groovy.lang.Closure.DELEGATE_FIRST
import groovy.transform.CompileStatic

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.CacheFactory

@CompileStatic
class GemFireBuilder {

    private CacheFactory cacheFactory = new CacheFactory()

    def properties(Map<String, String> params) {
        params.each { String name, String value ->
            cacheFactory.set(name, value)
        }
    }

    def pdx(@DelegatesTo(strategy=DELEGATE_FIRST, value=PdxBuilder) Closure closure) {
        def builder = new PdxBuilder(cacheFactory)
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = DELEGATE_FIRST
        hydrated()
    }

    def cache(@DelegatesTo(strategy=DELEGATE_FIRST, value=CacheBuilder) Closure closure) {
        Cache cache = cacheFactory.create()
        def builder = new CacheBuilder(cache)
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = DELEGATE_FIRST
        hydrated()

        cache
    }

    def call() {
        println "GemFireBuilder.call()"
    }

    def build() {
        println "GemFireBuilder.build()"
    }
}
