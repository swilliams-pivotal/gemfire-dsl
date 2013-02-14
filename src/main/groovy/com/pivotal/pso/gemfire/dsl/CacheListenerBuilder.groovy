package com.pivotal.pso.gemfire.dsl

import groovy.transform.CompileStatic


@CompileStatic
class CacheListenerBuilder {

    private Map<String, Closure> listener = [:].withDefault { { -> } }

    def addListenerMethodClosure(String name, Closure closure) {
        listener.put(name, closure)
        listener
    }

    def methodMissing(String name, Object args) {
        if (args instanceof Object[]) {
            Object[] array = (Object[]) args
            return addListenerMethodClosure(name, (Closure) array[0])
        }
    }

}
