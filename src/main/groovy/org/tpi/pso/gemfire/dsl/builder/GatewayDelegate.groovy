package org.tpi.pso.gemfire.dsl.builder

import static groovy.lang.Closure.DELEGATE_FIRST

import com.gemstone.gemfire.cache.util.Gateway
import com.gemstone.gemfire.cache.util.GatewayHub


// @CompileStatic
public class GatewayDelegate {

    private final GatewayHub gatewayHub

    public GatewayDelegate(GatewayHub gatewayHub) {
        this.gatewayHub = gatewayHub
    }

    def gateway(String id, Map params = [:]) {
        println "GatewayDelegate.gateway.$id($params)"
        Gateway gateway = gatewayHub.addGateway(id)
//        gateway.setOrderPolicy(null) // OrderPolicy
//        gateway.setQueueAttributes(null) // QueueAttributes
//        gateway.setSocketBufferSize(0)
//        gateway.setSocketReadTimeout(0)
    }

    def methodMissing(String name, map) {
        println "GatewayDelegate.missing.$name($map)"
    }
}
