package test;

import test.main.Stub2;
import by.artkostm.rxj.context.ApplicationContext;
import by.artkostm.rxj.context.ObservableApplicationContext;

public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        long start = System.currentTimeMillis();
        ApplicationContext context = new ObservableApplicationContext("test.main");
        final Stub2 st2 = (Stub2) context.getBean("st2");
        System.out.println(st2);
        System.out.println(context.getBean("st1"));
        System.out.println("Time: " + (System.currentTimeMillis() - start) + "ms");
        Thread.sleep(2000);
    }
}
