package test.main;

import by.artkostm.rxj.annotation.Bean;
import by.artkostm.rxj.annotation.Configuration;

@Configuration(name = "testconfig")
public class Configuration2
{
    @Bean(skipBpdy = false)
    public static Object getInt()
    {
        return 10;
    }
}
