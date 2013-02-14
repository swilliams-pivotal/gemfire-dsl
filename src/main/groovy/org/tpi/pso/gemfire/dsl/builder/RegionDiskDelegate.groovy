package org.tpi.pso.gemfire.dsl.builder


class RegionDiskDelegate {

    def methodMissing(String name, map) {
        println "RegionDiskDelegate.missing.$name($map)"
    }
}
