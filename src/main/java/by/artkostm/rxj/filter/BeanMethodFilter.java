package by.artkostm.rxj.filter;

import java.lang.reflect.Method;

import by.artkostm.rxj.annotation.Bean;

public class BeanMethodFilter extends MethodFilter
{
    @Override
    public Boolean call(Method t)
    {
        final boolean isBeanAnnotaitionPresent = t.isAnnotationPresent(Bean.class) && super.call(t); 
        return isBeanAnnotaitionPresent;
    }
}
