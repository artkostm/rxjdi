package by.artkostm.rxj;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import by.artkostm.rxj.filter.ClassFilter;
import by.artkostm.rxj.filter.ConfigurationClassFilter;
import by.artkostm.rxj.processor.ConstructorProcessor;
import by.artkostm.rxj.processor.FieldProcessor;
import by.artkostm.rxj.processor.MethodProcessor;
import by.artkostm.rxj.scanner.ClassScanner;
import by.artkostm.rxj.scanner.PackageClassScanner;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func3;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        long start = System.currentTimeMillis();
        ClassScanner<String> scanner = new PackageClassScanner();
        final Set<Class<?>> classes = scanner.scan("test");
        
        final Observable<Class<?>> classObservable 
                 = Observable.create(new OnSubscribe<Class<?>>() {

                    @Override
                    public void call(Subscriber<? super Class<?>> t) {
                        for(Class<?> clazz : classes){
                            t.onNext(clazz);
                        }
                        t.onCompleted();
                    }
                }).filter(new ClassFilter());
        
        final Observable<Class<?>> configs = classObservable.filter(new ConfigurationClassFilter());
        
        final Observable<Constructor<?>> constructorObservable 
                 = classObservable.flatMap(new ConstructorProcessor());
        
        final Observable<Method> methodObservable = classObservable.flatMap(new MethodProcessor());
        
        final Observable<Field> fieldObservable = classObservable.flatMap(new FieldProcessor());
        
        classObservable.subscribe(new Action1<Class<?>>()
        {
            @Override
            public void call(Class<?> t)
            {
                System.out.println("class=" + t.getName() + ", thread=" + Thread.currentThread().getName());
            }
        });
        constructorObservable.subscribe(new Action1<Constructor<?>>()
        {
            @Override
            public void call(Constructor<?> t)
            {
                System.out.println("constructor=" + t.getName() + ", thread=" + Thread.currentThread().getName());
            }
        });
        methodObservable.subscribe(new Action1<Method>()
        {
            @Override
            public void call(Method t)
            {
                System.out.println("method=" + t.getName() + ", thread=" + Thread.currentThread().getName());
            }
        });
        fieldObservable.subscribe(new Action1<Field>()
        {
            @Override
            public void call(Field t)
            {
                System.out.println("field=" + t.getName() + ", thread=" + Thread.currentThread().getName());
            }
        });
    }
    /**
     * ƒл€ методов:
     *  ѕробегаемс€ по всем методам, если метод аннотирован при помощи @Bean, 
     *  то провер€ем тип возвращаемого значени€ и им€ бина, создаем объект, 
     *  реализующий BeanMetadata интерфейс. 
     *  ≈сли же при помощи @Resource, то провер€ем им€ в аннотации(если его 
     *  нет, то им€ бина = им€ метода), а потом и тип принимаемого значени€. 
     */
}
