package by.artkostm.rxj.metadata.builder;

import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.artkostm.rxj.metadata.ConfigurationMetadata;
import by.artkostm.rxj.metadata.impl.ConfigurationMetadataImpl;
import by.artkostm.rxj.util.MethodInvoker;

public class ConfigurationBuilder
{
    private static final Logger LOG = LogManager.getLogger(ConfigurationBuilder.class);
        
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
            LOG.warn("Cannot create instance of " + clazz.getName() + " class.", e);
        }
        final ConfigurationMetadata configMetadata = new ConfigurationMetadataImpl(configObject, name);
        return configMetadata;
    }
}
