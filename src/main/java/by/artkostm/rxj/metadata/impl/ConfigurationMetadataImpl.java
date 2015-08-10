package by.artkostm.rxj.metadata.impl;

import java.lang.reflect.Method;

import rx.Observable;
import by.artkostm.rxj.filter.StaticMethodFilter;
import by.artkostm.rxj.metadata.ConfigurationMetadata;

public class ConfigurationMetadataImpl implements ConfigurationMetadata
{
    private final Object configurationObject;
    private final String name;

    public ConfigurationMetadataImpl(Object configurationObject, String name)
    {
        super();
        this.configurationObject = configurationObject;
        this.name = name;
    }

    @Override
    public Object getObject()
    {
        return configurationObject;
    }

    @Override
    public Role getRole()
    {
        return Role.Configuration;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Class<?> getType()
    {
        return configurationObject.getClass();
    }

    /**
     *  always return null;
     */
    @Override
    public Method getFactoryMethod()
    {
        return null;
    }

    /**
     *  always return null;
     */
    @Override
    public Method getInitMethod()
    {
        return null;
    }

    /**
     *  always return null;
     */
    @Override
    public Method getDestroyMethod()
    {
        return null;
    }

    /**
     *  always return null;
     */
    @Override
    public Method getPostConstructMethod()
    {
        return null;
    }

    /**
     *  always return null;
     */
    @Override
    public Method getPreDestroyMethod()
    {
        return null;
    }

    @Override
    public Observable<Method> getFactoryMethods()
    {
        final Class<?> clazz = getType();
        final Method[] methods = clazz.getDeclaredMethods();
        return Observable.from(methods).filter(new StaticMethodFilter());
    }
}
