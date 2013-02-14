package org.tpi.pso.gemfire.dsl.builder

import static groovy.lang.Closure.*
import groovy.transform.CompileStatic

import com.gemstone.gemfire.cache.Cache


@CompileStatic
public class GemfireBuilder {

    private Properties properties = new Properties()

    public void setProperty(String name, String value) {
        properties.setProperty(name, value)
    }

    public void setProperties(Properties properties) {
        properties.putAll(properties)
    }

    public void setProperties(Map map) {
        properties.putAll(map)
    }

    def properties(Map params, Closure c = null) {
        properties.putAll(params)
    }

    public Cache compile(String source) {
        byte[] bytes = source.getBytes('UTF-8')
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes)
        build(bais)
    }

    public Cache build(InputStream is) {
        def grs  = new GroovyCodeSource(new InputStreamReader(is),this.getClass().getName(),GroovyShell.DEFAULT_CODE_BASE)
        def script = new GroovyClassLoader().parseClass(grs).newInstance() as Script
        null
    }

    public Cache build(File file) {
        def script = new GroovyClassLoader().parseClass(file).newInstance() as Script
        null
    }

    public Cache build(String className) {
        Class cls = new GroovyClassLoader().loadClass(className)
        def script = cls.newInstance() as Script
        null
    }

    public Cache build(GroovyCodeSource codeSource) {
        def script = new GroovyClassLoader().parseClass(codeSource).newInstance() as Script
        null
    }

    public Cache build(@DelegatesTo(strategy=DELEGATE_FIRST, value=GemfireDelegate) Closure closure) {
        // closure.directive = 1
        closure.delegate = new GemfireDelegate(properties)
        closure.resolveStrategy = DELEGATE_FIRST
        Cache cache = (Cache) closure()
        cache
    }
}
