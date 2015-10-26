package by.artkostm.rxj.filter;

import by.artkostm.rxj.annotation.Configuration;
import by.artkostm.rxj.annotation.Singleton;

public class ConfigurationClassFilter extends ClassFilter
{
    @Override
    public Boolean call(Class<?> clazz)
    {
        final boolean isConfiguration = clazz.isAnnotationPresent(Configuration.class) || 
                clazz.isAnnotationPresent(Singleton.class);
        final boolean decision = super.call(clazz) && isConfiguration;
        return decision;
    }
}
