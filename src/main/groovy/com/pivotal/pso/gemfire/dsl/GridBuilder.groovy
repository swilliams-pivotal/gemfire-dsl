package com.pivotal.pso.gemfire.dsl

import static groovy.lang.Closure.DELEGATE_FIRST
import groovy.transform.CompileStatic

import com.gemstone.gemfire.cache.Cache


@CompileStatic
class GridBuilder {

    def gemfire(@DelegatesTo(strategy=DELEGATE_FIRST, value=GemFireBuilder) Closure<Object> closure) {

        def tccl = Thread.currentThread().getContextClassLoader()
        def cl = new URLClassLoader(new URL[0], tccl)
        Thread.currentThread().setContextClassLoader(cl)

        def builder = new GemFireBuilder()
        Closure hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = DELEGATE_FIRST
        Cache cache = (Cache) hydrated()

        Thread.currentThread().setContextClassLoader(tccl)

        cache
    }
}
