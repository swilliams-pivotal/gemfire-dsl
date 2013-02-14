package org.tpi.pso.gemfire.dsl.parser

import org.tpi.pso.gemfire.dsl.support.ListenerClosureDelegate;


class FunctionBuilder {

    def create(Closure closure) {

        //        Object.metaClass.to { String regionName-> println "to ${regionName}.put(delegate)" }
        this.with closure
    }

    def region(String name, Map params, Closure c = null) {
        region(params, name, c)
    }

    def region(Map params, String name, Closure c = null) {
        println "region $name($params)"
        params.listeners.each { Closure closure->
            //            println "dump: " + l.dump()
            //            println "serialized: " + l
            closure.delegate = new ListenerClosureDelegate()
            // closure.directive = null
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure('foo')

            // println "func: " + closure.dehydrate().asWritable().toString()
            println "func: " + closure.getClass().getName()
        }
    }

    def methodMissing(String name, map) {
        println "missing $name($map)"
    }
}
