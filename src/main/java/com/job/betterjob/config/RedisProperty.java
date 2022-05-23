package com.job.betterjob.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author songle
 * @create 2022-05-22 17:17
 * @descreption redis配置信息
 */

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisProperty {
    private String host;
    private int port;
    private int timeout = 2000;
    private int database;
}
