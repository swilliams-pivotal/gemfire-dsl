package com.pivotal.pso.gemfire.dsl.listeners

import com.pivotal.pso.gemfire.dsl.support.DSLUtils
import com.pivotal.pso.gemfire.util.CacheListenerSupport
import groovy.transform.CompileStatic
import org.codehaus.groovy.runtime.DefaultGroovyMethods

import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * Author: smaldini
 * Date: 2/12/13
 * Project: gemfire-dsl
 */
@CompileStatic
abstract class MapAdapterBuilder<T> {

    final Map<String, Closure> adapter
    final Class<T> clazz

    MapAdapterBuilder() {
        clazz = (Class<T>) ((ParameterizedType) getClass().genericSuperclass).actualTypeArguments[0]
        adapter = clazz.methods.collectEntries {Method m ->[m.name, DSLUtils.EMPTY_CLOSURE]}
    }

    T create() {
        (T) DefaultGroovyMethods.asType(adapter, clazz)
    }

    void appendCallback(String name, Closure c, delegate = new CacheListenerSupport()){
        Closure _c = (Closure) c.clone()
        if(delegate){
            _c.resolveStrategy = Closure.DELEGATE_FIRST
            _c.delegate = delegate
        }
        adapter[name] = _c
    }


}
