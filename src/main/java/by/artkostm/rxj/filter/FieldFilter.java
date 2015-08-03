package by.artkostm.rxj.filter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import by.artkostm.rxj.util.Reflections;
import rx.functions.Func1;

public class FieldFilter implements Func1<Field, Boolean>{
    
    @Override
    public Boolean call(Field field) {
        final boolean isPublic = Modifier.PUBLIC == field.getModifiers();
        if (!isPublic)
        {
            final Class<?> clazz = field.getDeclaringClass();
            try {
                clazz.getMethod(Reflections.getSetterName(field.getName()), field.getType());
            } catch (NoSuchMethodException e) {
                return false;
            } catch (SecurityException e) {
                return false;
            }
        }
        return true;
    }
}
