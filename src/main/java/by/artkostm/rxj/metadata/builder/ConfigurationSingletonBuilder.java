package by.artkostm.rxj.metadata.builder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.artkostm.rxj.metadata.ConfigurationMetadata;
import by.artkostm.rxj.metadata.LifeCycleMetadata.Role;
import by.artkostm.rxj.metadata.impl.ConfigurationMetadataImpl;
import by.artkostm.rxj.util.MethodInvoker;

public class ConfigurationSingletonBuilder
{
    private static final Logger LOG = LogManager.getLogger(ConfigurationSingletonBuilder.class);
        
    public static ConfigurationMetadata buildConfigurationSingleton(Class<?> clazz, String name, Role role)
    {
        Object configObject = null;
        try
        {
            configObject = MethodInvoker.invokeConstroctor(clazz);
        }
        catch (Exception e)
        {
            LOG.warn("Cannot create instance of " + clazz.getName() + " class.", e);
        }
        final ConfigurationMetadata configMetadata = new ConfigurationMetadataImpl(configObject, name, role);
        return configMetadata;
    }
}
