package org.tpi.pso.gemfire.dsl.support



// @CompileStatic
class ListenerClosureDelegate {

    Closure afterCreate

    Closure afterDestroy

    Closure afterInvalidate

    Closure afterUpdate

    Closure afterRegionClear

    Closure afterRegionCreate

    Closure afterRegionDestroy

    Closure afterRegionInvalidate

    Closure afterRegionLive

    def methodMissing(String name, map) {
        println "FunctionDelegate.missing $name($map)"
    }

    public RegionOperationHelper put(Map.Entry entry) {
        // TODO check arg is Serializable
        // TODO check arg implements Key (provides getRegionKey())
        new RegionOperationHelper(entry.getKey(), entry.getValue())
    }

    public RegionOperationHelper send(Object arg) {
        // TODO check arg is Serializable
        // TODO check arg implements Key (provides getRegionKey())
        send(key: arg.hashCode(), value: arg)
    }

    public RegionOperationHelper send(Object key, Object value) {
        // TODO check arg is Serializable
        // TODO check arg implements Key (provides getRegionKey())
        new RegionOperationHelper(key, value)
    }

    public RegionOperationHelper msg(Object key, Object value) {
        // TODO check arg is Serializable
        // TODO check arg implements Key (provides getRegionKey())
        new RegionOperationHelper(key, value)
    }

    public RegionOperationHelper remove(Object key) {
        new RegionOperationHelper(key, null) // FIXME safe?
    }
}
