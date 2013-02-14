package org.tpi.pso.gemfire.dsl.builder

import static groovy.lang.Closure.DELEGATE_FIRST
import groovy.transform.CompileStatic


@CompileStatic
public class GemfireDslDelegate {

    def gemfire(Map params, Closure closure = null) {
        println "GemfireDslDelegate.gemfire($params)"
        if (closure != null) {
            closure()
        }
    }

    def cache(Map params, Closure closure = null) {
        println "GemfireDslDelegate.cache($params)"
        if (closure != null) {
            closure()
        }
    }

    def region(Map params, String name, Closure closure = null) {
        println "GemfireDslDelegate.region.$name($params)"
        if (closure != null) {
            closure()
        }
    }

    def attributes(Map params, Closure closure = null) {
        println "GemfireDslDelegate.attributes($params)"
        if (closure != null) {
            closure()
        }
    }

    def disk(Map params, Closure closure = null) {
        println "GemfireDslDelegate.disk($params)"
        if (closure != null) {
            closure()
        }
    }

    def methodMissing(String name, map) {
        println "GemfireDslDelegate.missing.$name($map)"
    }
}
