package by.artkostm.rxj.context;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.artkostm.rxj.annotation.Configuration;
import by.artkostm.rxj.filter.ConfigurationClassFilter;
import by.artkostm.rxj.metadata.ConfigurationMetadata;
import by.artkostm.rxj.metadata.LifeCycleMetadata;
import by.artkostm.rxj.metadata.builder.BeanBuilder;
import by.artkostm.rxj.metadata.builder.ConfigurationBuilder;
import by.artkostm.rxj.processor.FactoryMethodProcessor;
import by.artkostm.rxj.scanner.ClassScanner;
import by.artkostm.rxj.scanner.PackageClassScanner;
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
    private static final Logger LOG = LogManager.getLogger(ObservableApplicationContext.class);
    
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
        configObservable.flatMap(new FactoryMethodProcessor()).map(new Func1<Method, LifeCycleMetadata>()
        {
            @Override
            public LifeCycleMetadata call(Method t)
            {
                final String name = Reflections.getBeanName(t);
                final boolean skipBody = Reflections.getSkipBody(t);
                final LifeCycleMetadata beanMetadata = BeanBuilder.build(t, name, skipBody);
                return beanMetadata;
            }
        })//TODO:set property for role == Role.Bean
        .forEach(contexInserter);
    }
   
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
}
