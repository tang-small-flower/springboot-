package com.sec;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class schema {
	  private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 //   @Scheduled(cron = "*/5 * * * * ?")
	    // 5s执行一次,秒-分-时-天-月-周-年
	    public void test() throws Exception {
	        System.out.println(simpleDateFormat.format(new Date()) + "定时任务执行咯");
	    }
}