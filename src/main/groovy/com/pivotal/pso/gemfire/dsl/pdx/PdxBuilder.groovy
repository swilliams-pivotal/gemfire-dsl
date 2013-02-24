package com.pivotal.pso.gemfire.dsl.pdx

import groovy.transform.CompileStatic

import com.gemstone.gemfire.cache.CacheFactory
import com.gemstone.gemfire.pdx.PdxSerializer


@CompileStatic
class PdxBuilder {

    Boolean readSerialized
    Boolean persistent
    Boolean ignoreUnreadFields
    String diskStore
    PdxSerializer serializer

}
