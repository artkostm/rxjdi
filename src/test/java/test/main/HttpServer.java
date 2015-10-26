package test.main;

import by.artkostm.rxj.annotation.Inject;
import by.artkostm.rxj.annotation.Singleton;
import test.main.inner.HttpServerInitializer;

@Singleton
public class HttpServer
{
    @Inject
    private HttpServerInitializer initializer;
    
    public void get()
    {
        System.out.println("TEST:"+initializer.getClass().getName());
    }
}
