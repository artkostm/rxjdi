package by.artkostm.rxj.metadata.builder;

import by.artkostm.rxj.metadata.LifeCycleMetadata;

public abstract class BeanBuildcycle
{
    public LifeCycleMetadata buildBean()
    {
        
        return null;
    }
    
    protected abstract void prepareToCreate();
    
    protected abstract Object buildObject();
    
    protected abstract void invokeInit();
    
    protected abstract void invokePostConstruct();
}
