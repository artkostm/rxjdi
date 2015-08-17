package by.artkostm.rxj.metadata.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import by.artkostm.rxj.metadata.LifeCycleMetadata;
import by.artkostm.rxj.metadata.impl.BeanMetadataImpl;
import by.artkostm.rxj.util.MethodInvoker;
import by.artkostm.rxj.util.Reflections;

public class BeanBuilder
{
    public LifeCycleMetadata buildBean(final Object conf, final Method factoryMethod, final String name, final boolean skipBody)
    {
        final Class<?> beanClass = factoryMethod.getReturnType();
        
        if (skipBody && factoryMethod.getParameterTypes().length == 0)
        {
            try
            {
                @SuppressWarnings("unused")
                final Object obj = factoryMethod.invoke(conf);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
            {
                // TODO add logger
            }
        }
        else
        {
            try
            {
                final Object obj = MethodInvoker.invokeConstroctor(beanClass);
                @SuppressWarnings("unused")
                final LifeCycleMetadata lcm = new BeanMetadataImpl(obj, name, factoryMethod, 
                    Reflections.findInitMethod(beanClass), Reflections.findDestroyMethod(beanClass), skipBody);
            }
            catch (InstantiationException | IllegalAccessException 
                    | ClassNotFoundException | IllegalArgumentException 
                    | InvocationTargetException | NoSuchMethodException 
                    | SecurityException e)
            {
                // TODO add logger
            }
        }
        
        return null;
    }  
    
    public static LifeCycleMetadata build(final Method factoryMethod, final String name, final boolean skipBody)
    {
        final Class<?> beanClass = factoryMethod.getReturnType();
        
        final LifeCycleMetadata lcm = new BeanMetadataImpl(null, name, factoryMethod, 
            Reflections.findInitMethod(beanClass), Reflections.findDestroyMethod(beanClass), skipBody);
        
        return lcm;
    }  
}
