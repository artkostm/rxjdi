package by.artkostm.rxj.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import by.artkostm.rxj.annotation.Configuration;

public class Reflections {
    
    public static final String SETTER_PREFIX = "set";
    
    public static String getSetterName(String fieldName)
    {
        final String firstLetter = String.valueOf(fieldName.charAt(0)).toUpperCase();
        final String setter = SETTER_PREFIX + firstLetter + fieldName.substring(1);
        return setter;
    }
    
    public static String getAnnotaitedClassName(Class<?> annotaitedClass, Class<? extends Annotation> annotationClass)
    {
        if (annotaitedClass.isAnnotationPresent(annotationClass))
        {
            Annotation annotation = annotaitedClass.getAnnotation(annotationClass);
            if (annotation instanceof Configuration)
            {
                Configuration conf = (Configuration) annotation;
                String name = conf.name();
                if (name.isEmpty())
                {
                    name = annotaitedClass.getSimpleName() + ".Configuration";
                }
                return name;
            }
        }
        return null;
    }
    
    public static Method findPreDestroyMethod(final Class<?> clazz)
    {
        return findMethodAnnotatedWith(clazz, PreDestroy.class);
    }
    
    public static Method findPostConstructMethod(final Class<?> clazz)
    {
        return findMethodAnnotatedWith(clazz, PostConstruct.class);
    }
    
    public static Method findMethodAnnotatedWith(final Class<?> clazz, Class<? extends Annotation> annotationClass)
    {
        final Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods)
        {
            if (method.isAnnotationPresent(annotationClass))
            {
                return method;
            }
        }
        return null;
    }
    
    public static Method[] getStaticMethods(final Class<?> clazz)
    {
        List<Method> staticMethods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods())
        {
            final boolean isStatic = Modifier.isStatic(method.getModifiers());
            if (isStatic)
            {
                staticMethods.add(method);
            }
        }
        
        return (Method[]) staticMethods.toArray();
    }
}
