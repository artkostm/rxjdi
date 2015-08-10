package by.artkostm.rxj.context;

public interface Context
{
    Object getBean(String name);
    boolean containtsBean(String name);
}
