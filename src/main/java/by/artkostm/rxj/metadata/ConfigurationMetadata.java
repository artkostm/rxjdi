package by.artkostm.rxj.metadata;

import java.lang.reflect.Method;

import rx.Observable;

public interface ConfigurationMetadata extends LifeCycleMetadata
{
    Observable<Method> getFactoryMethods();
}
