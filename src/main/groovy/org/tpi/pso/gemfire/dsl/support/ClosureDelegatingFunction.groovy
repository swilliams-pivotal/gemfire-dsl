package org.tpi.pso.gemfire.dsl.support;

import groovy.transform.CompileStatic

import com.gemstone.gemfire.cache.execute.Function
import com.gemstone.gemfire.cache.execute.FunctionContext
import com.gemstone.gemfire.cache.execute.ResultSender


@CompileStatic
public class ClosureDelegatingFunction implements Function {

    private Closure closure

    private String id

    private boolean ha

    private boolean optimizeForWrite

    private boolean result

    public ClosureDelegatingFunction(Closure closure, String id) {
        this(closure, id, false, true, true) // Default for FunctionAdapter
    }

    public ClosureDelegatingFunction(Closure closure, String id, boolean optimizeForWrite) {
        this(closure, id, optimizeForWrite, true, true) // 
    }

    public ClosureDelegatingFunction(Closure closure, String id, boolean optimizeForWrite, boolean result) {
        this(closure, id, optimizeForWrite, result, true) // 
    }

    public ClosureDelegatingFunction(Closure closure, String id, boolean optimizeForWrite, boolean result, boolean ha) {
        this.closure = closure
        this.id = id
        this.optimizeForWrite = optimizeForWrite
        this.result = result
        this.ha = ha
    }

    @Override
    public void execute(FunctionContext context) {
        try {
            if (result) {
                ResultSender<?> sender = context.getResultSender()
                Object result = closure?.call(context.getArguments())
                sender.sendResult(result)
            }
            else {
                closure?.call(context.getArguments())
            }
        }
        catch (Throwable t) {
            context.getResultSender().sendException(t)
        }
    }

    @Override
    public String getId() {
        return id
    }

    @Override
    public boolean hasResult() {
        return result
    }

    @Override
    public boolean isHA() {
        return ha
    }

    @Override
    public boolean optimizeForWrite() {
        return optimizeForWrite
    }

}
