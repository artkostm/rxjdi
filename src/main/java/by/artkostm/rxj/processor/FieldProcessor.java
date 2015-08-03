package by.artkostm.rxj.processor;

import java.lang.reflect.Field;

import by.artkostm.rxj.filter.FieldFilter;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FieldProcessor implements Func1<Class<?>, Observable<Field>>{

    @Override
    public Observable<Field> call(Class<?> clazz) {
        //System.out.println("field: class="+clazz.getName()+", thread="+Thread.currentThread().getName());
        return Observable.from(clazz.getDeclaredFields())
                .filter(new FieldFilter()).subscribeOn(Schedulers.io());
    }
}
