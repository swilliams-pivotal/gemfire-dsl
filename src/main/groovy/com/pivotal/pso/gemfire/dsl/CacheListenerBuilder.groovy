package com.pivotal.pso.gemfire.dsl

import groovy.transform.CompileStatic


@CompileStatic
class CacheListenerBuilder {

    private Map<String, Closure> listener = [:]

    def addListenerMethodClosure(String name, Closure closure) {

        def owner = closure.owner
        def thisObject = closure.thisObject

        def cls = new CacheListenerSupport()
        Closure hydrated = closure.rehydrate(cls, owner, thisObject)
        hydrated.resolveStrategy = Closure.DELEGATE_FIRST

        listener.put(name, hydrated)
        listener
    }

    def methodMissing(String name, Object args) {
        if (args instanceof Object[]) {
            Object[] array = (Object[]) args
            if (array[0] instanceof Closure) {
                return addListenerMethodClosure(name, (Closure) array[0])
            }
        }
        println "CacheListenerBuilder.name: $name"
    }

}
