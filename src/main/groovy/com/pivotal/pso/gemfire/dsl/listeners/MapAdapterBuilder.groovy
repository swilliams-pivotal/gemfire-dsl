package com.pivotal.pso.gemfire.dsl.listeners

import com.pivotal.pso.gemfire.dsl.support.DSLUtils
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


}
