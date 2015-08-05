package by.artkostm.rxj.metadata;

import java.lang.reflect.Method;

public interface BeanMetadata
{
    public Object getObject();
    public Role getRole();
    public String getName();
    public Class<?> getType();
    public Method getFactoryMethod();
    public Method getInitMethod();
    public Method getDestroyMethod();
    public Method getPostConstructMethod();
    public Method getPreDestroyMethod();
    
    public enum Role
    {
        Bean, Configuration;
    }
}
