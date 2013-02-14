package org.tpi.pso.gemfire.dsl.support;

import groovy.transform.CompileStatic

import java.lang.reflect.Array

import com.gemstone.gemfire.cache.execute.Function
import com.gemstone.gemfire.cache.execute.FunctionContext
import com.gemstone.gemfire.cache.execute.ResultSender


@CompileStatic
public class ClosureReceivingFunction implements Function {

    private String id

    private boolean ha

    private boolean optimizeForWrite

    private boolean result

    public ClosureReceivingFunction(String id) {
        this(id, false, true, true) // Default for FunctionAdapter
    }

    public ClosureReceivingFunction(String id, boolean optimizeForWrite) {
        this(id, optimizeForWrite, true, true) // 
    }

    public ClosureReceivingFunction(String id, boolean optimizeForWrite, boolean result) {
        this(id, optimizeForWrite, result, true) // 
    }

    public ClosureReceivingFunction(String id, boolean optimizeForWrite, boolean result, boolean ha) {
        this.id = id
        this.optimizeForWrite = optimizeForWrite
        this.result = result
        this.ha = ha
    }

    @Override
    public void execute(FunctionContext context) {

        Closure<?> closure
        Object funcArgs

        if (context.getArguments().getClass().isArray() && Array.getLength(context.getArguments()) > 0 && Array.get(context.getArguments(), 0) instanceof Closure) {
            closure = (Closure<?>) Array.get(context.getArguments(), 0)
        }
        else if (context.getArguments() instanceof Closure) {
            closure = (Closure<?>) context.getArguments()
        }
        else {
            context.getResultSender().sendException(new IllegalArgumentException("Function arguments must start with a Closure"))
        }

        try {
            if (result) {
                ResultSender<?> sender = context.getResultSender()
                Object result = closure?.call(funcArgs)
                sender.sendResult(result)
            }
            else {
                closure?.call(funcArgs)
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
