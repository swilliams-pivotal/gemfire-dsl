package com.pivotal.pso.gemfire.dsl

import groovy.transform.CompileStatic


@CompileStatic
class CacheListenerSupport {

    def send(Object args) {
        println "CacheListenerSupport.send(${args})"
    }

    def propertyMissing(String name) {
        println "CacheListenerSupport.propertyMissing.$name"
    }

    def propertyMissing(String name, Object args) {
        println "CacheListenerSupport.propertyMissing.$name = ${args.class}"
    }

    def methodMissing(String name, Object args) {
        println "CacheListenerSupport.methodMissing.$name(${args.class})"
    }

}
