package com.sec;
import java.util.Date;
public class Say implements Runnable {
	public long t=0;
    @Override
    public void run(){
        System.out.println(t+"trial"+ new Date());
    }
}
