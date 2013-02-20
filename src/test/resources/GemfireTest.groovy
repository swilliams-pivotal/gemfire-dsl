/**
 * Author: smaldini
 * Date: 2/10/13
 * Project: gemfire-dsl
 * This DSL is statically checked using Gemfire
 */
doWithGemfire {
    props 'jmx-manager': 'true'

    dynamic {
        persistBackup = false
        diskDir = new File(".")
        registerInterest = false
        poolName = null
    }

    copyOnRead = null
    lockLease = null
    lockTimeout = null
    searchTimeout = null
    messageSyncInterval = null

    criticalHeapPercentage = null
    evictionHeapPercentage = null

    gatewayConflictResolver { a, b -> }

    transactionWriter {}

    transactionListener {
        afterCommit {}
        afterFailedCommit {}
        afterRollback {}
        close{}
    }

    region('toto') {
    }
}