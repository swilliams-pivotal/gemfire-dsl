package com.pivotal.pso.gemfire.dsl.region

import com.gemstone.gemfire.cache.GemFireCache
import com.gemstone.gemfire.cache.Scope
import com.pivotal.pso.gemfire.dsl.Gemfire
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

/**
 * Author: smaldini
 * Date: 2/7/13
 * Project: gemfire-dsl
 */
@CompileStatic
@Slf4j
class LocalRegionBuilder<K,V> extends BaseRegionBuilder<K,V> {

    LocalRegionBuilder(Gemfire gemfire, String name) {
        super(gemfire, name)
        scope = Scope.LOCAL
    }

    @Override
    void setScope(Scope scope){
        log.error 'Scope assignment is prohibited with local regions'
    }

    @Override
    void setPersistent(Boolean flag){
        log.error 'Persistence flag assignment is prohibited with local regions'
    }
}
