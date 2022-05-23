package com.songle.betterjob.handler;


import com.songle.betterjob.annotation.BetterJobCron;
import com.songle.betterjob.command.impl.AnnotationJobCommand;
import com.songle.betterjob.model.JobInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author songle
 * @create 2022-05-22 22:23
 * @descreption  扫描被任务注解标注的类并注册任务
 */

@Slf4j
@Component
public class JobAnnotationHandler implements ApplicationContextAware,SmartInitializingSingleton{

   private static ApplicationContext context;

   public static ApplicationContext getApplicationContext(){
       return context;
   }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }


    /**
     * @description  当所有单例bean实例化完成后，扫描所有被任务注解标注的方法，并将其注册
     *
     */
    @Override
    public void afterSingletonsInstantiated() {
        if (context == null) {
            return;
        }

        String[] beanDefinitionNames = context.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName:beanDefinitionNames) {
            Object bean = context.getBean(beanDefinitionName);
            Map<Method, BetterJobCron> betterJobCronMethodMap = null;
            try {
                betterJobCronMethodMap = MethodIntrospector.selectMethods(bean.getClass(),(MethodIntrospector.MetadataLookup<BetterJobCron>) method -> AnnotatedElementUtils.findMergedAnnotation(method,BetterJobCron.class));
            }catch (Exception e){
                log.error("acquire method annoted by BetterJobCron error,cause : {}" , e);
            }
            if (betterJobCronMethodMap == null || betterJobCronMethodMap.isEmpty()) {
                continue;
            }

            for(Map.Entry<Method, BetterJobCron> entry:betterJobCronMethodMap.entrySet()){
                Method method = entry.getKey();
                BetterJobCron betterJobCron = entry.getValue();
                String jobName = betterJobCron.name();
                JobInfo jobInfo = new JobInfo(betterJobCron);
                method.setAccessible(true);
                CommandHandler.registryJobCommand(jobName,new AnnotationJobCommand(jobInfo,method,bean));
            }
        }

    }
}
