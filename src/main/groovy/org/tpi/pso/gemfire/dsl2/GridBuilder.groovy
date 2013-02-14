package org.tpi.pso.gemfire.dsl2

import static groovy.lang.Closure.DELEGATE_FIRST
import groovy.transform.CompileStatic

@CompileStatic
class GridBuilder {

    def gemfire(@DelegatesTo(strategy=DELEGATE_FIRST, value=GemFireBuilder) Closure closure) {
        def builder = new GemFireBuilder()
        def hydrated = closure.rehydrate(builder, this, this)
        hydrated.resolveStrategy = DELEGATE_FIRST
        hydrated()
    }
}
