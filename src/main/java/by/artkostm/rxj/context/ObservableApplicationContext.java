package by.artkostm.rxj.context;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import by.artkostm.rxj.annotation.Configuration;
import by.artkostm.rxj.filter.BeanMethodFilter;
import by.artkostm.rxj.filter.ClassFilter;
import by.artkostm.rxj.filter.ConfigurationClassFilter;
import by.artkostm.rxj.metadata.ConfigurationMetadata;
import by.artkostm.rxj.metadata.builder.ConfigurationBuilder;
import by.artkostm.rxj.processor.MethodProcessor;
import by.artkostm.rxj.scanner.ClassScanner;
import by.artkostm.rxj.scanner.PackageClassScanner;
import rx.Observable;
import rx.Subscriber;
import rx.Observable.OnSubscribe;
import rx.functions.Func1;

public class ObservableApplicationContext extends ApplicationContext
{
    private final String packagePath;
    private final Set<Class<?>> classes;
    
    public ObservableApplicationContext(String packagePath)
    {
        super();
        this.packagePath = packagePath;
        final ClassScanner<String> scanner = new PackageClassScanner();
        classes = scanner.scan(packagePath);
        createContext();
    }
    
    @Override
    protected void createContext()
    {
        final Observable<Class<?>> configObservable = classObservable.filter(new ConfigurationClassFilter());
        
        final List<ConfigurationMetadata> configs =  configObservable.map(new Func1<Class<?>, ConfigurationMetadata>()
        {

            @Override
            public ConfigurationMetadata call(Class<?> t)
            {
                final Configuration cAn = t.getAnnotation(Configuration.class);
                String name = cAn.name();
                if (name.isEmpty())
                {
                    name = t.getSimpleName() + ".Configuration";
                }
                final ConfigurationMetadata configMetadata = ConfigurationBuilder.buildConfiguration(t, name);
                return configMetadata;
            }
        }).toList().toBlocking().single();
        
        final Observable<Method> factoryMethodObservable = 
                configObservable.flatMap(new MethodProcessor()).filter(new BeanMethodFilter());
        factoryMethodObservable.subscribe();
    }
    
    private Observable<Class<?>> classObservable = 
            Observable.create(new OnSubscribe<Class<?>>()
            {
                @Override
                public void call(Subscriber<? super Class<?>> t)
                {
                    for (Class<?> clazz : classes)
                    {
                        t.onNext(clazz);
                        t.onCompleted();
                    }
                }
            }).filter(new ClassFilter());
    
    private Subscriber<Method> createBeans = new Subscriber<Method>()
    {
        @Override
        public void onCompleted()
        {}

        @Override
        public void onError(Throwable e)
        {}

        @Override
        public void onNext(Method t)
        {
            
        }
   };
}
