package com.sec;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class scheme{

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        return new ThreadPoolTaskScheduler();
    }

    private ScheduledFuture<?> future;
    @RequestMapping("/")
    @ResponseBody
    public String home(){
        return "home";
    }

    ///方法一

    @RequestMapping("/startCron")
    @ResponseBody
    public String startCron(){
        System.out.println("x0");
        //threadPoolTaskScheduler.shutdown();
        long t=1;
        Say s=new Say();
        s.t=t;
        future = threadPoolTaskScheduler.schedule(s, new CronTrigger("*/5 * * * * *"));
        System.out.println("x1");
        return "x";
    }

    @RequestMapping("/stopCron")
    @ResponseBody
    public String stopCron(){
        System.out.println("stop >>>>>");
        if(future != null) {
            future.cancel(true);
        }
        System.out.println("stop <<<<<");
        return "stop cron";
    }

    @RequestMapping("/startCron10")
    @ResponseBody
    public String startCron10(){
        System.out.println("x100");
        long t=2;
        Say s=new Say();
        s.t=t;
        future = threadPoolTaskScheduler.schedule(s, new CronTrigger("*/12 * * * * *"));
        return "x10";
    }


    ///方法二  执行的时间有时间间隔所以不是特别准确

    private String cronStr = "*/5 * * * * *";
    @RequestMapping("/startCron1")
    @ResponseBody
    public String startCron1(){
        System.out.println("startCron1 >>>>");
        threadPoolTaskScheduler.schedule(new Say(), new Trigger(){
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext){
            	System.out.println(triggerContext.lastActualExecutionTime());
            	System.out.println(cronStr);
                return new CronTrigger(cronStr).nextExecutionTime(triggerContext);
            }
        });
        System.out.println("startCron1 <<<<");
        return "*****";
    }


    @PostConstruct
    public void start(){
        startCron1();
    }

    @RequestMapping("/changeCronStr")
    @ResponseBody
    public String changeCronStr(){
        cronStr = "*/12 * * * * *";
        System.out.println("change "  + cronStr);
        return  cronStr;
    }

    @RequestMapping("/changeCronStr5")
    @ResponseBody
    public String changeCronStr5(){
        cronStr = "*/5 * * * * *";
        System.out.println("change "  + cronStr);
        return  cronStr;
    }


    public static void main(String[] args) {
        SpringApplication.run(scheme.class, args);
    }
}
