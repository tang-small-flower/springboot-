

```python
/*springboot设置定时任务
  Cron表达式
*/
```


```python
1、springboot定时任务场景：
   在项目开发过程中，经常需要定时任务来做一些内容，比如定时进行数据统计（阅读量统计），数据更新（生成每天的歌单推荐）等。
2、Cron表达式： https://www.cnblogs.com/sgh1023/p/10246893.html
   Cron表达式是一个字符串，字符串以5或6个空格隔开，分为6或7个域，每一个域代表一个含义，Cron有如下两种语法格式：
     1）Seconds Minutes Hours DayofMonth Month DayofWeek Year
     2）Seconds Minutes Hours DayofMonth Month DayofWeek
   Corn从左到右（用空格隔开）：秒 分 小时 月份中的日期 月份 星期中的日期 年份
   每一个域都使用数字，但还可以出现如下特殊字符，具体含义如下： 
     *：表示匹配该域的任意值。假如在Minutes域使用*, 即表示每分钟都会触发事件。
     ?：只能用在DayofMonth和DayofWeek两个域。它也匹配域的任意值，但实际不会。因为DayofMonth和DayofWeek会相互影响。例如想在每月的20日触发调度，不管20日到底是星期几，则只能使用如下写法： 13 13 15 20 * ?, 其中最后一位只能用？，而不能使用*，如果使用*表示不管星期几都会触发，实际上并不是这样。
     -：表示范围。例如在Minutes域使用5-20，表示从5分到20分钟每分钟触发一次 
     /：表示起始时间开始触发，然后每隔固定时间触发一次。例如在Minutes域使用5/20,则意味着5分钟触发一次，而25，45等分别触发一次. 
     ,：表示列出枚举值。例如：在Minutes域使用5,20，则意味着在5和20分每分钟触发一次。 
     L：表示最后，只能出现在DayofWeek和DayofMonth域。如果在DayofWeek域使用5L,意味着在最后的一个星期四触发。 
     W:表示有效工作日(周一到周五),只能出现在DayofMonth域，系统将在离指定日期的最近的有效工作日触发事件。例如：在 DayofMonth使用5W，如果5日是星期六，则将在最近的工作日：星期五，即4日触发。如果5日是星期天，则在6日(周一)触发；如果5日在星期一到星期五中的一天，则就在5日触发。另外一点，W的最近寻找不会跨过月份 。
     LW:这两个字符可以连用，表示在某个月最后一个工作日，即最后一个星期五。 
     #:用于确定每个月第几个星期几，只能出现在DayofMonth域。例如在4#2，表示某月的第二个星期三。
3、实现定时任务：
   i.@Schedule(Cron="xxxxxxx")与@EnableScheduling
     缺点：@scheduled注解可以方便的设定定时任务，但是对于定时参数需要变化的情况就会很不方便，如果要实现更改定时参数，就要停止服务，更改参数，重新部署。 
...
package com.sec;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableScheduling
public class sec {
	public static void main(String[] args) {
		  SpringApplication.run(sec.class, args);
	}
}
...
...
package com.sec;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class schema {
	  private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    @Scheduled(cron = "*/5 * * * * ?")
	    // 5s执行一次,秒-分-时-天-月-周-年
	    public void test() throws Exception {
	        System.out.println(simpleDateFormat.format(new Date()) + "定时任务执行咯");
	    }
}
...
    ii.对于这种需求， 可以利用TaskScheduler借口来实现,实现方法有两种  https://blog.csdn.net/f641385712/article/details/89454494
        1)启动定时,关闭定时，使用新参数启动定时
        2)使用自定义的Trigger启动定时，更改参数
        ThreadPoolTaskScheduler：线程池任务调度类，能够开启线程池进行任务调度。ThreadPoolTaskScheduler.schedule()方法会创建一个定时计划ScheduledFuture，在这个方法需要添加两个参数，Runnable（线程接口类） 和CronTrigger（定时任务触发器）
        TriggerContext该接口表示触发的上下文。它能够获取上次任务原本的计划时间/实际的执行时间以及实际的完成时间
public interface TriggerContext {
	// 上次预计的执行时间
	@Nullable
	Date lastScheduledExecutionTime();
	// 上次真正执行时间
	@Nullable
	Date lastActualExecutionTime();
	// 上次完成的时间
	@Nullable
	Date lastCompletionTime();

}
       Trigger接口用于计算任务的下次执行时间。它的接口定义如下：
public interface Trigger {
	//获取下次执行时间
	@Nullable
	Date nextExecutionTime(TriggerContext triggerContext);
}

       CronTrigger是Trigger的实现类顾名思义，它通过Cron表达式来生成调度计划。比如：scheduler.schedule(task, new CronTrigger("0 15 9-17 * * MON-FRI"));
       TaskSchedulerSpring任务调度器的核心接口，定义了执行定时任务的主要方法，主要根据任务的不同触发方式调用不同的执行逻辑，其实现类都是对JDK原生的定时器或线程池组件进行包装，并扩展额外的功能。
public interface TaskScheduler {

	// 提交任务调度请求 
	// Runnable task：待执行得任务
	// Trigger trigger：使用Trigger指定任务调度规则
	@Nullable
	ScheduledFuture<?> schedule(Runnable task, Trigger trigger);

	// @since 5.0  这里使用的Instant 类，其实最终也是转换成了Date
	default ScheduledFuture<?> schedule(Runnable task, Instant startTime) {
		return schedule(task, Date.from(startTime));
	}
	//  提交任务调度请求   startTime表示它的执行时间
	//  注意任务只执行一次，使用startTime指定其启动时间  
	ScheduledFuture<?> schedule(Runnable task, Date startTime);

	// @since 5.0
	default ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Instant startTime, Duration period) {
		return scheduleAtFixedRate(task, Date.from(startTime), period.toMillis());
	}
	// 使用fixedRate的方式提交任务调度请求    任务首次启动时间由传入参数指定 
	// task　待执行的任务　 startTime　任务启动时间    period　两次任务启动时间之间的间隔时间，默认单位是毫秒
	ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period);

	// @since 5.0
	default ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Duration period) {
		return scheduleAtFixedRate(task, period.toMillis());
	}
	// 使用fixedRate的方式提交任务调度请求 任务首次启动时间未设置，任务池将会尽可能早的启动任务
	// task 待执行任务 
	// period 两次任务启动时间之间的间隔时间，默认单位是毫秒
	ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period);

	// @since 5.0
	default ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Instant startTime, Duration delay) {
		return scheduleWithFixedDelay(task, Date.from(startTime), delay.toMillis());
	}
	//  使用fixedDelay的方式提交任务调度请求  任务首次启动时间由传入参数指定 
	// delay 上一次任务结束时间与下一次任务开始时间的间隔时间，单位默认是毫秒 
	ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay);
	// @since 5.0
	default ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Duration delay) {
		return scheduleWithFixedDelay(task, delay.toMillis());
	}
	// 使用fixedDelay的方式提交任务调度请求 任务首次启动时间未设置，任务池将会尽可能早的启动任务 
	// delay 上一次任务结束时间与下一次任务开始时间的间隔时间，单位默认是毫秒 
	ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay);

}



    ThreadPoolTaskScheduler是TaskScheduler的实现类，包装Java Concurrent中的ScheduledThreadPoolExecutor类，大多数场景下都使用它来进行任务调度。
    先放ScheduledThreadPoolExecutor（JDK全新定时器调度）的构造：  具体： https://blog.csdn.net/f641385712/article/details/83717639
    ScheduledThreadPoolExecutor(int corePoolSize) //使用给定核心池大小创建一个新定定时线程池 
    ScheduledThreadPoolExecutor(int corePoolSize, ThreadFactorythreadFactory) //使用给定的初始参数创建一个新对象，可提供线程创建工厂
    几个重要API：
    // 执行一次
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit);
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

    // 周期性执行
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,long initialDelay, long delay, TimeUnit unit);
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,long initialDelay, long period, TimeUnit unit);

public class ThreadPoolTaskScheduler extends ExecutorConfigurationSupport
		implements AsyncListenableTaskExecutor, SchedulingTaskExecutor, TaskScheduler {
	...
	// 默认的size 是1
	private volatile int poolSize = 1;
	private volatile boolean removeOnCancelPolicy = false;
	@Nullable
	private volatile ErrorHandler errorHandler;
	// 内部持有一个JUC的ScheduledExecutorService 的引用
	@Nullable
	private ScheduledExecutorService scheduledExecutor;
	...
	
	// 初始化线程池的执行器~~~~ 该方法的父类是ExecutorConfigurationSupport
	// 它定义了一些线程池的默认配置~~~~~
	@Override
	protected ExecutorService initializeExecutor(
			ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
		// 我们发现，如果set PoolSize，那么它的size就是1
		this.scheduledExecutor = createExecutor(this.poolSize, threadFactory, rejectedExecutionHandler);

		if (this.removeOnCancelPolicy) {
			if (this.scheduledExecutor instanceof ScheduledThreadPoolExecutor) {
				((ScheduledThreadPoolExecutor) this.scheduledExecutor).setRemoveOnCancelPolicy(true);
			} else {
				logger.info("Could not apply remove-on-cancel policy - not a Java 7+ ScheduledThreadPoolExecutor");
			}
		}

		return this.scheduledExecutor;
	}
	// 就是new一个ScheduledThreadPoolExecutor  来作为最终执行任务的执行器
	protected ScheduledExecutorService createExecutor(
			int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {

		return new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
	}
	...
	
	//获取当前活动的线程数 委托给ScheduledThreadPoolExecutor来做得
	public int getActiveCount() {
		if (this.scheduledExecutor == null) {
			// Not initialized yet: assume no active threads.
			return 0;
		}
		return getScheduledThreadPoolExecutor().getActiveCount();
	}

	// 显然最终就是交给ScheduledThreadPoolExecutor去执行了~~~
	// 提交执行一次的任务
	// submit\submitListenable方法表示：提交执行一次的任务，并且返回一个Future对象供判断任务状态使用
	@Override
	public void execute(Runnable task) {
		Executor executor = getScheduledExecutor();
		try {
			executor.execute(errorHandlingTask(task, false));
		}
		catch (RejectedExecutionException ex) {
			throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
		}
	}
	...
}

...
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
        long t=1;
        Say s=new Say();
        s.t=t;
        future = threadPoolTaskScheduler.schedule(s, new CronTrigger("*/5 * * * * *"));
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
...
...
package com.sec;
import java.util.Date;
public class Say implements Runnable {
	public long t=0;
    @Override
    public void run(){
        System.out.println(t+"trial"+ new Date());
    }
}
...
```
