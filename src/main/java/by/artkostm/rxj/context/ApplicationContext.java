package by.artkostm.rxj.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.artkostm.rxj.metadata.LifeCycleMetadata;

public abstract class ApplicationContext implements BeanFactory
{
    protected static final Logger LOG = LogManager.getLogger(ApplicationContext.class);
    
    protected Map<String, LifeCycleMetadata> context;
    
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
    
    /**
     * The method to create the context. Usage: after creating application context object;
     */
    protected abstract void createContext();
    
    protected abstract void close();
    
    /**
     * Register shutdown hook for the context
     */
    {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
        { 
            @Override
            public void run()
            {
                close();
                context.clear();
                context = null;
            }
        }));
    }
}
