package by.artkostm.rxj.context;

import java.util.Set;

import by.artkostm.rxj.filter.ClassFilter;
import by.artkostm.rxj.filter.ConfigurationClassFilter;
import by.artkostm.rxj.scanner.ClassScanner;
import by.artkostm.rxj.scanner.PackageClassScanner;
import rx.Observable;
import rx.Subscriber;
import rx.Observable.OnSubscribe;

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
        
    }
    
    protected Observable<Class<?>> classObservable = 
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
}
