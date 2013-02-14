package com.pivotal.pso.gemfire.dsl

import groovy.transform.CompileStatic

import com.gemstone.gemfire.cache.Region
import com.gemstone.gemfire.cache.execute.FunctionService


@CompileStatic
class CacheListenerSupport {

    def send(Object args) {
        println "CacheListenerSupport.send(${args})"
    }

    def func(String func, Object... args) {
        println "CacheListenerSupport.func(${func})"
        Region region = null
        FunctionService.onRegion(region)
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
