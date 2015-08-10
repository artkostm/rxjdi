package by.artkostm.rxj.metadata.builder;

import java.lang.reflect.InvocationTargetException;

import by.artkostm.rxj.metadata.ConfigurationMetadata;
import by.artkostm.rxj.metadata.impl.ConfigurationMetadataImpl;
import by.artkostm.rxj.util.MethodInvoker;

public class ConfigurationBuilder
{
    public static ConfigurationMetadata buildConfiguration(Class<?> clazz, String name)
    {
        Object configObject = null;
        try
        {
            configObject = MethodInvoker.invokeConstroctor(clazz);
        }
        catch (InstantiationException | IllegalAccessException 
                | ClassNotFoundException | IllegalArgumentException 
                | InvocationTargetException | NoSuchMethodException 
                | SecurityException e)
        {
            //TODO: add logs
        }
        final ConfigurationMetadata configMetadata = new ConfigurationMetadataImpl(configObject, name);
        return configMetadata;
    }
}
