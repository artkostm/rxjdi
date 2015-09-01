package test.main;

import by.artkostm.rxj.annotation.Inject;

public class Stub2
{
    @Inject(name = "st1")
    private Stub1 st1;
    
    private String m;
    
    public Stub2()
    {}
    
    public Stub2(String str)
    {
        m = str;
    }

    public static void m1()
    {}

    public void m2()
    {}

    @SuppressWarnings("unused")
    private void m3()
    {}

    @Override
    public String toString()
    {
        return "Stub2 [st1=" + st1 + ", m=" + m + "]";
    }
}
