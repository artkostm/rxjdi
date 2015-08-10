package by.artkostm.rxj.context;

import java.util.HashMap;
import java.util.Map;

import by.artkostm.rxj.metadata.LifeCycleMetadata;

public abstract class ApplicationContext implements Context
{
    Map<String, LifeCycleMetadata> context;
    
    public ApplicationContext()
    {
        context = new HashMap<>();
    }

    @Override
    public Object getBean(final String name)
    {
        for (String beanName : context.keySet())
        {
            if (name.equals(beanName))
            {
                final LifeCycleMetadata metadata = context.get(beanName);
                final Object bean = metadata.getObject();
                return bean;
            }
        }
        return null;
    }

    @Override
    public boolean containtsBean(final String name)
    {
        for (String beanName : context.keySet())
        {
            if (name.equals(beanName))
            {
                return true;
            }
        }
        return false;
    }
    
    protected abstract void createContext();
}
