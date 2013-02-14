package org.tpi.pso.gemfire.dsl


class Main {

    static main(args) {

        def grid = new GemfireGrid()
        addShutdownHook { grid.stop() }

        try {
            grid.start()
        }
        catch (Throwable t) {
            t.printStackTrace() // TODO something sensible
        }
        finally {
            grid.stop()
        }
    }
}
