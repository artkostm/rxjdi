package by.artkostm.rxj.context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import by.artkostm.rxj.annotation.Configuration;
import by.artkostm.rxj.annotation.Inject;
import by.artkostm.rxj.filter.BeanMetadataFilter;
import by.artkostm.rxj.filter.ConfigurationClassFilter;
import by.artkostm.rxj.metadata.ConfigurationMetadata;
import by.artkostm.rxj.metadata.LifeCycleMetadata;
import by.artkostm.rxj.metadata.builder.BeanBuilder;
import by.artkostm.rxj.metadata.builder.ConfigurationBuilder;
import by.artkostm.rxj.processor.FactoryMethodProcessor;
import by.artkostm.rxj.scanner.ClassScanner;
import by.artkostm.rxj.scanner.PackageClassScanner;
import by.artkostm.rxj.util.FieldInjector;
import by.artkostm.rxj.util.Reflections;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Application context implementation using RxJava and Observable concept
 * @author Artsiom_Chuiko
 *
 */
public class ObservableApplicationContext extends ApplicationContext
{
    
    private final String packagePath;
    
    private final Observable<Class<?>> classObservable;
    
    /**
     * 
     * @param packagePath - the base package path to scan classes
     */
    public ObservableApplicationContext(String packagePath)
    {
        super();
        this.packagePath = packagePath;
        final ClassScanner<String> scanner = new PackageClassScanner();
        classObservable = Observable.from(scanner.scan(packagePath));
        createContext();
    }
    
    @Override
    protected void createContext()
    {
        final Observable<Class<?>> configObservable = classObservable.filter(new ConfigurationClassFilter());
        
        putConfigMetadataToContext(configObservable);
        processFactoryMethods(configObservable);
    }
    
    /**
     * To put configuration meta data to the context;
     * @param configObservable
     */
    private void putConfigMetadataToContext(final Observable<Class<?>> configObservable)
    {
        configObservable.map(new Func1<Class<?>, LifeCycleMetadata>()
        {
            @Override
            public ConfigurationMetadata call(Class<?> t)
            {
                final String name = Reflections.getAnnotaitedClassName(t, Configuration.class);
                final ConfigurationMetadata configMetadata = ConfigurationBuilder.buildConfiguration(t, name);
                LOG.debug("Created configuration with name : " + name + ", " + configMetadata);
                return configMetadata;
            }
        }).forEach(contexInserter);
    }
    
    /**
     * 
     * @param configObservable
     */
    private void processFactoryMethods(final Observable<Class<?>> configObservable)
    {
        final Observable<LifeCycleMetadata> constructedBeans 
                 = configObservable.flatMap(new FactoryMethodProcessor())
                                   .map(new Func1<Method, LifeCycleMetadata>()
        {
            @Override
            public LifeCycleMetadata call(Method t)
            {
                final String name = Reflections.getBeanName(t);
                final boolean skipBody = Reflections.getSkipBody(t);
                final String configName = Reflections.getAnnotaitedClassName(t.getDeclaringClass(), Configuration.class);
                final Object config = getBean(configName);
                final LifeCycleMetadata beanMetadata = BeanBuilder.build(t, config, name, skipBody);
                return beanMetadata;
            }
        });
        
        constructedBeans.forEach(contexInserter);
        //sets properties for role == Role.Bean
        //invokes post constructor method
        Observable.from(context.values()).filter(new BeanMetadataFilter())
                        .doOnNext(propertySetter)
                        .doOnNext(postConstructInvoker)
                        .subscribe();
    }
    
    /**
     * To invoke PostConstruct annotated methods
     */
    private final Action1<LifeCycleMetadata> postConstructInvoker = new Action1<LifeCycleMetadata>()
    {
        @Override
        public void call(LifeCycleMetadata lcm)
        {
            final Method init = lcm.getPostConstructMethod();
            if (init != null)
            {
                try
                {
                    init.invoke(lcm.getObject());
                }
                catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
                {
                    LOG.warn("Cannot invoke init method for bean: " + lcm.getName());
                }
            }
        }
    };
    
    /**
     * Sets properties for meta data where role == Role.Bean
     */
    private final Action1<LifeCycleMetadata> propertySetter = new Action1<LifeCycleMetadata>()
    {
        @Override
        public void call(LifeCycleMetadata lcm)
        {
            final List<Field> fields = Reflections.getAnnotaitedFields(lcm.getType(), Inject.class);
            final FieldInjector fieldInjector = new FieldInjector();
            for (Field field : fields)
            {
                final String injectedBeanName = Reflections.getInjectedBeanName(field);
                final Object value = getBean(injectedBeanName);
                fieldInjector.injectField(lcm.getObject(), field, value);
            }
        }
    };
   
    /**
     * Anonymous class to insert a meta data object to the context;
     */
    private final Action1<LifeCycleMetadata> contexInserter = new Action1<LifeCycleMetadata>()
    {
        @Override
        public void call(LifeCycleMetadata t)
        {
            LOG.debug("Added '" + t.getName() + "' bean to the Context.");
            context.put(t.getName(), t);
        }
    };
   
    /**
     * 
     * @return the package path
     */
    public String getPackagePath()
    {
        return packagePath;
    }

    @Override
    protected void close()
    {
        Observable.from(context.values())
            .doOnNext(new Action1<LifeCycleMetadata>()
            {
                @Override
                public void call(LifeCycleMetadata lcm)
                {
                    final Method destroy = lcm.getPreDestroyMethod();
                    if (destroy != null)
                    {
                        try
                        {
                            destroy.invoke(lcm.getObject());
                        }
                        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
                        {
                            LOG.warn("Cannot invoke destroy method for bean: " + lcm.getName());
                        }
                    }
                }
            }).subscribe();
    }
}
