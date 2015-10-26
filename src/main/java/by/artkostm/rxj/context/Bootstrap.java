package by.artkostm.rxj.context;

import java.util.HashMap;
import java.util.Map;

import by.artkostm.rxj.context.module.BindModule;
import by.artkostm.rxj.context.module.Module;
import by.artkostm.rxj.util.MethodInvoker;

public class Bootstrap
{
    private static final Map<Class<? extends BindModule>, BindModule> modules 
       = new HashMap<Class<? extends BindModule>, BindModule>();
    
    public static DependencyContext module(final Class<? extends BindModule> clazz)
    {
        final BindModule module = modules.get(clazz);
        if (module == null)
        {
            try
            {
                final BindModule newModule = clazz.newInstance();
                MethodInvoker.invoke(newModule, Module.class.getDeclaredMethods()[0].getName(), null);
                return newModule.getContext();
            }
            catch (Exception e)
            {
                throw new RuntimeException("Can't bind module " + clazz.getName());
            }
        }
        return module.getContext();
    }
}
