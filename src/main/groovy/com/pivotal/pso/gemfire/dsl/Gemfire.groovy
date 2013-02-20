package com.pivotal.pso.gemfire.dsl

import com.gemstone.gemfire.cache.*
import com.gemstone.gemfire.cache.client.Pool
import com.gemstone.gemfire.cache.server.CacheServer
import com.gemstone.gemfire.cache.util.GatewayConflictResolver
import com.gemstone.gemfire.internal.jndi.JNDIInvoker
import com.pivotal.pso.gemfire.dsl.listeners.TransactionListenerBuilder
import com.pivotal.pso.gemfire.dsl.pdx.PdxBuilder
import com.pivotal.pso.gemfire.dsl.region.BaseRegionBuilder
import com.pivotal.pso.gemfire.dsl.region.ClientRegionBuilder
import com.pivotal.pso.gemfire.dsl.region.LocalRegionBuilder
import com.pivotal.pso.gemfire.dsl.region.LookupRegionBuilder
import com.pivotal.pso.gemfire.dsl.region.PartitionRegionBuilder
import com.pivotal.pso.gemfire.dsl.region.ReplicatedRegionBuilder
import com.pivotal.pso.gemfire.dsl.support.DSLUtils
import com.pivotal.pso.gemfire.dsl.support.GemfireSystemScriptWrapper
import com.pivotal.pso.gemfire.dsl.system.DatasourceBuilder
import com.pivotal.pso.gemfire.dsl.system.DynamicRegionConfigBuilder
import com.pivotal.pso.gemfire.dsl.topology.ClientSystem
import com.pivotal.pso.gemfire.dsl.topology.MemberSystem
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.util.logging.Slf4j
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.builder.CompilerCustomizationBuilder

import static groovy.lang.Closure.DELEGATE_FIRST

/**
 * Author: smaldini
 * Date: 2/7/13
 * Project: gemfire-dsl
 */
@CompileStatic
@Slf4j
class Gemfire {

    @Lazy GemFireCache cache = { initCache() }()

    Properties props

    DynamicRegionFactory dynamicRegionFactory

    Boolean copyOnRead
    Integer lockLease
    Integer lockTimeout
    Integer searchTimeout
    Integer messageSyncInterval
    Float criticalHeapPercentage
    Float evictionHeapPercentage
    File cacheXml
    GatewayConflictResolver gatewayConflictResolver
    TransactionWriter transactionWriter
    final Collection<TransactionListener> transactionListeners = []
    final Collection<DatasourceBuilder> jndiDatasources = []

    MemberSystem memberSystem

    PdxBuilder pdxBuilder

    //specific Client System
    Boolean readyForEvents

    protected void setMemberSystem(MemberSystem memberSystem) { this.memberSystem = memberSystem }

    /**
     * Build methods to findOrCreate a Gemfire System from a File/Stream Reader
     * @param r reader
     * @return GemfireSystem
     */
    @CompileStatic(TypeCheckingMode.SKIP)
    static Gemfire build(Reader r) {
        def configuration = new CompilerConfiguration()
        CompilerCustomizationBuilder.withConfig(configuration) {
            ast(CompileStatic)
        }

        configuration.scriptBaseClass = GemfireSystemScriptWrapper.name
        def shell = new GroovyShell(configuration)
        def script = shell.parse r
        script.run()
    }

    static Gemfire build(File file) {
        Gemfire gs = null
        file.withReader { Reader reader ->
            gs = build reader
        }
        gs
    }

    static Gemfire build(String script) {
        def reader = new StringReader(script)
        Gemfire gs = build reader
        gs
    }

    /**
     * Root DSL to findOrCreate a Gemfire System
     * @param properties
     * @param c DSL
     * @return GemfireSystem
     */
    static Gemfire create(Map properties = null,
                          @DelegatesTo(strategy = DELEGATE_FIRST, value = Gemfire) Closure c
    ) {
        log.debug 'Preparing Gemfire System'
        def gemfireSystem = new Gemfire(props: properties as Properties)

        DSLUtils.delegateFirstAndRun gemfireSystem, c

        gemfireSystem
    }

    /**
     * Create or Re-use a the gemfire cache system (CacheFactory cache is unique per Classloader)
     * @param properties Gemfire Properties (Licence etc)
     * @return
     */
    protected initCache() {
        GemFireCache c
        memberSystem = memberSystem ?: new MemberSystem(this)

        String msg
        try {
            c = memberSystem.fetchCache()
            msg = 'Retrieved existing'
        }
        catch (CacheClosedException cce) {
            c = memberSystem.createWithFactory()
            msg = 'Created'
        }

        c.copyOnRead = copyOnRead ?: c.copyOnRead
        memberSystem.setupCache(c)

        cacheXml?.withInputStream { InputStream s ->
            c.loadCacheXml s
            log.debug "Initialized cache from $cacheXml"
        }

        def rMgr = c.resourceManager
        rMgr.criticalHeapPercentage = criticalHeapPercentage ?: rMgr.criticalHeapPercentage
        rMgr.evictionHeapPercentage = evictionHeapPercentage ?: rMgr.evictionHeapPercentage

        def txMgr = c.cacheTransactionManager
        for (listener in transactionListeners) {
            txMgr.addListener listener
        }

        if (transactionWriter)
            txMgr.writer = transactionWriter

        def system = c.distributedSystem
        def member = system.distributedMember

        log.info "Connected to Distributed System [$system.name = $member.id@$member.host]"
        log.info "$msg GemFire v.$CacheFactory.version Cache [$c.name]"

        addShutdownHook {
            synchronized (c) {
                if (!c.isClosed()) {
                    c.close()
                    log.debug 'Cache closed'
                }
            }
        }

        c
    }

    /**
     * initialize a Dynamic Gemfire System (using DynamicRegionFactory)
     * @param c DSL
     */
    DynamicRegionFactory dynamic(
            @DelegatesTo(strategy = DELEGATE_FIRST, value = DynamicRegionConfigBuilder) Closure c = null
    ) {
        log.debug 'Creating DynamicRegionFactory'
        def config

        if (c) {
            def builder = new DynamicRegionConfigBuilder()
            DSLUtils.delegateFirstAndRun builder, c
            config = builder.createConfig()
        } else {
            log.debug 'Using DynamicRegionFactory.Config default values'
            config = new DynamicRegionFactory.Config()
        }

        dynamicRegionFactory = DynamicRegionFactory.get()
        dynamicRegionFactory.open config

        log.debug 'DynamicRegionFactory created'
        dynamicRegionFactory
    }

    /**
     * initialize Properties
     * @param c DSL
     */
    Properties props(Map<String, String> _props) {
        props = _props as Properties
    }

    /**
     * Build a Gemfire Generic Non-Client region (Or try to lookup existing one if no closure passed)
     * @param name String representing region name
     * @param c
     * @return
     */
    public <K, V> Region<K, V> region(String name,
                                      @DelegatesTo(strategy = DELEGATE_FIRST, value = BaseRegionBuilder) Closure c = null) {
        new BaseRegionBuilder<K,V>(cache, name).findOrCreate c
    }

    /**
     * Build a Gemfire Local region
     * @param name String representing region name
     * @param c
     * @return
     */
    public <K, V> Region<K, V> localRegion(String name,
                                           @DelegatesTo(strategy = DELEGATE_FIRST, value = LocalRegionBuilder) Closure c = DSLUtils.EMPTY_CLOSURE) {
        new LocalRegionBuilder<K,V>(cache, name).findOrCreate c
    }

    /**
     * Build a Gemfire Partition region
     * @param name String representing region name
     * @param c
     * @return
     */
    public <K, V> Region<K, V> partitionRegion(String name,
                                               @DelegatesTo(strategy = DELEGATE_FIRST, value = PartitionRegionBuilder) Closure c = DSLUtils.EMPTY_CLOSURE) {
        new PartitionRegionBuilder<K,V>(cache, name).findOrCreate c
    }

    /**
     * Build a Gemfire Replicated region
     * @param name String representing region name
     * @param c
     * @return
     */
    public <K, V> Region<K, V> replicatedRegion(String name,
                                                @DelegatesTo(strategy = DELEGATE_FIRST, value = ReplicatedRegionBuilder) Closure c = DSLUtils.EMPTY_CLOSURE) {
        new ReplicatedRegionBuilder<K,V>(cache, name).findOrCreate c
    }

    /**
     * Build a Gemfire Client region
     * @param name String representing region name
     * @param c
     * @return
     */
    public <K, V> Region<K, V> clientRegion(String name,
                                            @DelegatesTo(strategy = DELEGATE_FIRST, value = ClientRegionBuilder) Closure c = DSLUtils.EMPTY_CLOSURE) {
        new ClientRegionBuilder<K,V>(cache, name).findOrCreate c
    }

    /**
     * Build a GatewayEventResolver
     * @param c
     * @return
     */
    GatewayConflictResolver gatewayConflictResolver(Closure c) {
        gatewayConflictResolver = [onEvent: c] as GatewayConflictResolver
    }

    /**
     * Build a TransactionListener
     * @param c
     * @return
     */
    TransactionListener transactionListener(@DelegatesTo(strategy = DELEGATE_FIRST, value = TransactionListenerBuilder) Closure c) {
        def builder = new TransactionListenerBuilder()
        DSLUtils.delegateFirstAndRun builder, c

        TransactionListener txListener = builder.create()
        transactionListeners << txListener

        txListener
    }

    /**
     * Build a TransactionWriter
     * @param c
     * @return
     */
    TransactionWriter transactionWriter(Closure c) {
        transactionWriter = [beforeCommit: c] as TransactionWriter
    }

    /**
     * Build a JNDI datasource
     * @param c
     */
    void datasource(@DelegatesTo(strategy = DELEGATE_FIRST, value = DatasourceBuilder) Closure c) {
        def builder = new DatasourceBuilder()
        DSLUtils.delegateFirstAndRun builder, c

        JNDIInvoker.mapDatasource builder.attrs, builder.props

        jndiDatasources << builder
    }

    /**
     * Enable Client mode
     * @param poolName
     */
    void client(String poolName = 'gemfirePool') {
        memberSystem = new ClientSystem(this, poolName)
    }

    /**
     * enable Client mode
     * @param pool
     */
    void client(Pool pool) {
        memberSystem = new ClientSystem(this, pool)
    }

    /**
     * Create Cache Server
     * @param pool
     */
    CacheServer server(@DelegatesTo(strategy = DELEGATE_FIRST, value = ServerBuilder) Closure c = null) {
        def serverSystem = new ServerBuilder()

        if (c) {
            DSLUtils.delegateFirstAndRun serverSystem, c
        }

        serverSystem.createCacheServer cache as Cache
    }

    /**
     * Create pool builder
     * @param c
     */
    Pool pool(String name = 'gemfirePool', @DelegatesTo(strategy = DELEGATE_FIRST, value = PoolBuilder) Closure c = null) {
        def builder = new PoolBuilder(name: name)

        if (c) {
            DSLUtils.delegateFirstAndRun builder, c
        }

        //trigger cache creation
        getCache()

        builder.createPool props
    }

    void pdx(@DelegatesTo(strategy=DELEGATE_FIRST, value=PdxBuilder) Closure closure) {
        def builder = new PdxBuilder()
        DSLUtils.delegateFirstAndRun builder, closure
        pdxBuilder = builder
    }

    def asyncEventQueue() {
        ((Cache)cache).createAsyncEventQueueFactory()
    }

    def diskStore() {
        cache.createDiskStoreFactory()
    }

    def gatewayReceiver() {
        ((Cache)cache).createGatewayReceiverFactory()
    }

    def gatewaySender() {
        cache.createGatewaySenderFactory()
    }

    def pdxInstance() {
        cache.createPdxEnum("", "", 0)
    }

    def pdxInstanceFactory() {
        cache.createPdxInstanceFactory("")
    }
}
