package com.pivotal.pso.gemfire.dsl.system

import com.gemstone.gemfire.cache.DynamicRegionFactory
import groovy.transform.CompileStatic

/**
 * Author: smaldini
 * Date: 2/10/13
 * Project: gemfire-dsl
 */
@CompileStatic
class DynamicRegionConfigBuilder {

    boolean persistBackup
    java.io.File diskDir
    boolean registerInterest
    java.lang.String poolName

    /**
     * Generate Gemfire DynamicRegionFactory.config based on builder values
     * @return
     */
    DynamicRegionFactory.Config createConfig(){
        new DynamicRegionFactory.Config(diskDir, poolName, persistBackup, registerInterest)
    }

}
