package com.pivotal.pso.gemfire.dsl.support

import com.pivotal.pso.gemfire.dsl.Gemfire
import groovy.transform.CompileStatic

import static groovy.lang.Closure.DELEGATE_FIRST

/**
 * Author: smaldini
 * Date: 2/10/13
 * Project: gemfire-dsl
 */
@CompileStatic
class GemfireSystemScriptWrapper extends Script{

    Gemfire doWithGemfire(@DelegatesTo(strategy = DELEGATE_FIRST, value = Gemfire) Closure c){
        Gemfire.create c
    }

    @Override
    Object run() {
        super.run()
    }
}
