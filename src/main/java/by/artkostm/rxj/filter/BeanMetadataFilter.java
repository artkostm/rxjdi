package by.artkostm.rxj.filter;

import by.artkostm.rxj.metadata.LifeCycleMetadata;
import by.artkostm.rxj.metadata.LifeCycleMetadata.Role;
import rx.functions.Func1;

public class BeanMetadataFilter implements Func1<LifeCycleMetadata, Boolean>
{
    @Override
    public Boolean call(LifeCycleMetadata t)
    {
        return t.getRole() == Role.Bean;
    }
}
