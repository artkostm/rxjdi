package test.main;

import by.artkostm.rxj.annotation.Bean;
import by.artkostm.rxj.annotation.Configuration;

@Configuration(name = "testconfig")
public class Configuration2
{
    @Bean
    public static Integer getInt()
    {
        return 10;
    }
}
