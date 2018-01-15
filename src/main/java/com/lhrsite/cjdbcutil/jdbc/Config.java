package com.lhrsite.cjdbcutil.jdbc;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author lhr
 * @create 2018/1/5
 */
@Configurable
public class Config {

    @Bean
    public BaseConnention baseConnention(){
        return new BaseConnention();
    }
}
