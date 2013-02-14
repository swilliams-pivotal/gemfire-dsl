package org.tpi.pso.gemfire.dsl.support;

import groovy.lang.Closure;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.tpi.pso.gemfire.dsl.loader.DistributedClassLoader;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.FunctionService;

public class ClosureDelegatingFunction implements Function {

    public static void main(String[] arguments) {

        Cache cache = CacheFactory.getAnyInstance();
        Region<Object, Object> region = cache.getRegion("foo");
        Region<String, byte[]> clRegion = cache.getRegion("classLoader");
        Execution execution = FunctionService.onRegion(region);

        Closure closure = null;
        Object[] args = null;

        Closure dehydrated = closure.dehydrate();
        String name = dehydrated.getClass().getName();

        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        InputStream is = tccl.getResourceAsStream(name);

//        ByteArrayInputStream bais = new ByteArrayInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] data = baos.toByteArray();
        clRegion.put(name, data);

        execution.withArgs(new Object[] { name, args });

    }

    @Override
    public void execute(FunctionContext fc) {

        try {
            Cache cache = CacheFactory.getAnyInstance();
            Region<String, byte[]> clRegion = cache.getRegion("classLoader");

            Object[] args = (Object[]) fc.getArguments();
            String name = (String) args[0];

            ClassLoader tccl = Thread.currentThread().getContextClassLoader();
            DistributedClassLoader loader = new DistributedClassLoader(clRegion, tccl);

            Thread.currentThread().setContextClassLoader(loader);
            Class<?> rawClass = loader.loadClass(name);
            Class<? extends Closure> closureClass = rawClass.asSubclass(Closure.class);
            Closure<?> closure = closureClass.newInstance();

            Object delegate = null;      // utility delegate
            Object owner = null;         // cache or region?
            Object thisObject = closure; // ???

            Closure<?> rehydrated = closure.rehydrate(delegate, owner, thisObject);
            Object result = rehydrated.call(args);

            fc.getResultSender().sendResult(result);

            Thread.currentThread().setContextClassLoader(tccl);

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasResult() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isHA() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean optimizeForWrite() {
        // TODO Auto-generated method stub
        return false;
    }


}
