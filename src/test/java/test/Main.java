package test;

import test.main.HttpServer;
import test.main.Stub2;
import test.main.inner.HttpServerInitializer;
import by.artkostm.rxj.context.Bootstrap;
import by.artkostm.rxj.context.DependencyContext;
import by.artkostm.rxj.context.ObservableContext;
import by.artkostm.rxj.context.module.BindModule;

public class Main extends BindModule
{
    public static void main(String[] args) throws InterruptedException
    {
        long start = System.currentTimeMillis();
        DependencyContext context = Bootstrap.module(Main.class);
        final Stub2 st2 = (Stub2) context.getBean("st2");
        System.out.println(st2);
        System.out.println(context.getBean("st1"));
        HttpServerInitializer initialiser = (HttpServerInitializer) context.getBean(HttpServerInitializer.class);
        System.out.println(context.getBean("host"));
        System.out.println(initialiser.getClass().getName());
        final HttpServer server = (HttpServer) context.getBean(HttpServer.class);
        server.get();
        System.out.println("Time: " + (System.currentTimeMillis() - start) + "ms");
        
        //Thread.sleep(2000);
    }

    @Override
    public void declare()
    {
        context = new ObservableContext("test.main");
    }
}
