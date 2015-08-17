package by.artkostm.rxj.processor;

import java.lang.reflect.Method;

import by.artkostm.rxj.filter.BeanMethodFilter;
import rx.Observable;
import rx.functions.Func1;

public class FactoryMethodProcessor implements Func1<Class<?>, Observable<Method>>
{
    @Override
    public Observable<Method> call(Class<?> t)
    {
        return Observable.from(t.getDeclaredMethods()).filter(new BeanMethodFilter());
    }
}
