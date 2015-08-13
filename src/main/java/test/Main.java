package test;

import test.main.Configuration1;
import by.artkostm.rxj.context.ApplicationContext;
import by.artkostm.rxj.context.ObservableApplicationContext;

public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        long start = System.currentTimeMillis();
        ApplicationContext context = new ObservableApplicationContext("test.main");
        Configuration1 stub = (Configuration1) context.getBean("stub");
        System.out.println(stub);
        stub.setF1("new f1");
        Configuration1 newStub = (Configuration1) context.getBean("stub");
        System.out.println(newStub);
        System.out.println("Time: " + (System.currentTimeMillis() - start) + "ms");
    }
}
