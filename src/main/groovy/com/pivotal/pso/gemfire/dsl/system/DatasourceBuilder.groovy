package com.pivotal.pso.gemfire.dsl.system

import com.gemstone.gemfire.internal.datasource.ConfigProperty
import groovy.transform.CompileStatic

/**
 * Author: smaldini
 * Date: 2/10/13
 * Project: gemfire-dsl
 */
@CompileStatic
class DatasourceBuilder {

    List<ConfigProperty> props = []
    Map<String, String> attrs = [:]

    DatasourceBuilder attr(String name, String value){
        attrs[name] = value
        this
    }

    DatasourceBuilder prop(String name, String value, String type){
        props << new ConfigProperty(name,value,type)
        this
    }

}
