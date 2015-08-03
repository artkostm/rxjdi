package by.artkostm.rxj.metadata;

import java.lang.reflect.Method;

public interface BeanMetadata
{
    public String getName();
    public Class<?> getType();
    public Method getFactoryMethod();
}
