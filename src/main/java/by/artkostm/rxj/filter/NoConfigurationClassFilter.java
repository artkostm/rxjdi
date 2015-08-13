package by.artkostm.rxj.filter;

import by.artkostm.rxj.annotation.Configuration;

public class NoConfigurationClassFilter extends ClassFilter
{
    @Override
    public Boolean call(Class<?> clazz)
    {
        final boolean isConfiguration = clazz.isAnnotationPresent(Configuration.class);
        final boolean decision = super.call(clazz) && !isConfiguration;
        return decision;
    }
}
