package org.tpi.pso.gemfire.dsl.parser

import static groovy.lang.Closure.DELEGATE_FIRST


class GemfireBuilder extends FactoryBuilderSupport {


    @Override
    def registerObjectFactories() {
        registerExplicitMethod "region", this.&region
    }

    Object build(InputStream is) {
        def grs  = new GroovyCodeSource(new InputStreamReader(is),this.getClass().getName(),GroovyShell.DEFAULT_CODE_BASE)
        def script = new GroovyClassLoader().parseClass(grs).newInstance() as Script
        this.build(script)
    }

    Object build(String className) {
        Class script = new GroovyClassLoader().loadClass(className)
        this.build(script)
    }

    Object build(File file) {
        def script = new GroovyClassLoader().parseClass(file).newInstance() as Script
        this.build(script)
    }

    Object build(GroovyCodeSource codeSource) {
        def script = new GroovyClassLoader().parseClass(codeSource).newInstance() as Script
        this.build(script)
    }

    def configure(Closure closure) {
        println "configure: " + closure.toString()
        this.with closure
    }

    def gemfire(Closure closure) {
        println "gemfire: " + closure.toString()
    }

    def region(Map args = [:], String name, Closure closure = null) {
        println "region.map: $name $args"
        // region(name, args, closure)
    }

    def region(String name, Map args, Closure closure = null) {
        println "region.name: $name $args"
        // this.with closure
        //        if (closure != null) closure(name)
    }

    def disk(Map args, Closure closure = null) {
        disk(args, closure)
    }

    static void create(@DelegatesTo(strategy=DELEGATE_FIRST, value=GemfireConfigurationDelegate) Closure c){
        c.delegate = new GemfireConfigurationDelegate()
        c.resolveStrategy = DELEGATE_FIRST
        c()
    }
}
