package by.artkostm.rxj.context.module;

import by.artkostm.rxj.context.DependencyContext;

public abstract class BindModule implements Module
{
    protected DependencyContext context;

    public DependencyContext getContext()
    {
        return context;
    }
}
