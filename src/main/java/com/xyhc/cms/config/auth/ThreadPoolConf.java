package com.xyhc.cms.config.auth;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * The type Thread pool conf.
 *
 * @Author fanjia2
 * @Date: 2021 /3/8 11:33
 * @Description:
 * @version:V1.0 用到线程池的时候还需要创建Executors ，spring中有十分优秀的支持， 就是注解@EnableAsync就可以使用多线程，@Async加在线程任务的方法上（需要异步执行的任务）， 定义一个线程任务，通过spring提供的ThreadPoolTaskExecutor就可以使用线程池
 **/
@Configuration
@EnableAsync
public class ThreadPoolConf {

    /**
     * Thread pool task executor thread pool task executor.
     *
     * @return the thread pool task executor
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数，默认为1
        executor.setCorePoolSize(10);
        // 设置最大线程数，默认为Integer.MAX_VALUE
        executor.setMaxPoolSize(50);
        // 设置队列容量，一般需要设置值大于等于notifyScheduledMainExecutor.maxNum；默认为Integer.MAX_VALUE
        // executor.setQueueCapacity(2);
        // 设置线程池维护线程所允许的空闲时间，默认为60s
        executor.setKeepAliveSeconds(300);
        executor.setDaemon(true);
        // 设置默认线程名称
        executor.setThreadNamePrefix("jw-thread-");
        // 设置拒绝策略rejection-policy：当pool已经达到max size的时候，如何处理新任务 CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

}
