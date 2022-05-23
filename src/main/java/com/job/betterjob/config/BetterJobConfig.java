package com.job.betterjob.config;

import com.job.betterjob.handler.CommandHandler;
import com.job.betterjob.handler.ExecutorHandler;
import com.job.betterjob.handler.JedisHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author songle
 * @create 2022-05-22 17:19
 * @descreption betterJob配置类
 */

@Configuration
@Slf4j
public class BetterJobConfig {

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    private RedisProperty redisProperty;

    @Primary
    @Bean
    public ExecutorHandler buildExecutorHandler() {
        log.info("开始生成任务执行处理器bean~");
        JedisHandler jedisHandler =
                new JedisHandler(
                        redisProperty.getHost(),
                        redisProperty.getPort(),
                        redisProperty.getTimeout(),
                        redisProperty.getDatabase());
        return new ExecutorHandler(serviceName,jedisHandler);
    }


    @ConditionalOnBean(ExecutorHandler.class)
    @Bean
    public CommandHandler buildCommandHandler(ExecutorHandler executorHandler){
        return new CommandHandler(executorHandler);
    }
}
