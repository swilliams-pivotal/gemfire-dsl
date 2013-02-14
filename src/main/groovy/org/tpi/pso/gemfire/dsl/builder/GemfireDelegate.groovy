package org.tpi.pso.gemfire.dsl.builder

import static groovy.lang.Closure.DELEGATE_FIRST

import com.gemstone.gemfire.cache.Cache
import com.gemstone.gemfire.cache.CacheFactory


// @CompileStatic
class GemfireDelegate {

    private CacheFactory cacheFactory

    public GemfireDelegate(java.util.Properties properties) {
        this.cacheFactory = new CacheFactory(properties)
    }

    def properties(Map params) {
        params.each { String name, String value->
            cacheFactory.set(name, value)
        }
    }

    def locator(Map params, Closure closure = null) {
        println "GemfireDelegate.locator($params)"
        if (closure != null) closure()
    }

//    def pdx(Map params) {
//        println "GemfireDelegate.pdx($params)"
//
//        if (params.diskStore) cacheFactory.setPdxDiskStore(params.diskStore as String)
//        if (params.ignoreUnreadFields) cacheFactory.setPdxIgnoreUnreadFields(params.ignoreUnreadFields as boolean)
//        if (params.persistent) cacheFactory.setPdxPersistent(params.persistent as boolean)
//        if (params.readSerializable) cacheFactory.setPdxReadSerialized(params.readSerializable as boolean)
//        if (params.serializer) {
//            Class<?> c = Class.forName(params.serializer as String)
//            Class<PdxSerializer> s = c.asSubclass(PdxSerializer)
//            cacheFactory.setPdxSerializer(s.newInstance())
//        }
//    }

    public Cache cache(String sysname, @DelegatesTo(strategy=DELEGATE_FIRST, value=CacheDelegate) Closure closure) {
        println "GemfireDelegate.cache1($sysname) { $closure }"
        cache([:], sysname, closure)
    }

    public Cache cache(Map<String, String> params, @DelegatesTo(strategy=DELEGATE_FIRST, value=CacheDelegate) Closure closure) {
        println "GemfireDelegate.cache2($params) { $closure }"
        cache([:], null, closure)
    }

    public Cache cache(Map<String, String> params, String sysname, @DelegatesTo(strategy=DELEGATE_FIRST, value=CacheDelegate) Closure closure) {
        println "GemfireDelegate.cache3($params, $sysname) { $closure }"

        params.each { String name, String value->
            if ('pdx'.equals(name)) {
                this.pdx(params['pdx'] as Map)
            }
            else {
                cacheFactory.set(name, value)
            }
        }

        Cache cache = cacheFactory.create()
        println "created cache: " + cache

        if (closure != null) {
            closure.delegate = new CacheDelegate(cache)
            closure.resolveStrategy = DELEGATE_FIRST
            closure()
        }

        cache
    }

    def methodMissing(String name, Object args) {
        println "GemfireDelegate.missing.$name($args)"
//        println "args == ${args.class}"
//        if (args instanceof Object[] && (args as Object[])[0] instanceof Closure) {
//            println "found array & closure!"
//            cache(name, (args as Object[])[0] as Closure)
//        }
    }
}
