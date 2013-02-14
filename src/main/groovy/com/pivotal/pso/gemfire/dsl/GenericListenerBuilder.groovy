package com.pivotal.pso.gemfire.dsl



// @CompileStatic
class GenericListenerBuilder {

    private Map<String, Closure> listener = [:].withDefault { { -> } }

    def addListenerMethodClosure(String name, Closure closure) {

        def owner = closure.owner
        def thisObject = closure.thisObject

        def rls = new CacheListenerSupport()
        Closure hydrated = closure.rehydrate(rls, owner, thisObject)
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
        println "GenericListenerBuilder.name: $name"
    }

}
