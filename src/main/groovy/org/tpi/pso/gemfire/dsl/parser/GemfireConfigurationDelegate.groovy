package org.tpi.pso.gemfire.dsl.parser

class GemfireConfigurationDelegate {

    def gemfire(Closure c) {
        c()
    }

    def gemfire(Map attributes = [:], Closure c = null) {
        if (c != null) c()
    }

    def disk(Map attributes, Closure c = null) {
        println "delegate.disk: $attributes"
        if (c != null) c()
    }

    def region(String name, Map attributes = [:], Closure c = null) {
        println "delegate.region.name: $name $attributes"
        if (c != null) c()
    }

    def region(Map attributes, String name, Closure c = null) {
        println "delegate.region.name: $name $attributes"
        if (c != null) c()
    }

    def replicateRegion(String name, Map attributes = [:], Closure c = null) {
        println "delegate.region.name: $name $attributes"
        if (c != null) c()
    }

    def partitionRegion(String name, Map attributes = [:], Closure c = null) {
        println "delegate.region.name: $name $attributes"
        if (c != null) c()
    }

    def methodMissing(String name, args) {
        println "delegate.methodMissing: $name($args)"
        //        this."$name"(args)
    }
}
