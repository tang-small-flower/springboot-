package com.mai;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.*;
import org.thymeleaf.TemplateEngine;
@RestController
public class controller {
	@Autowired
	private mailservice myservice;
    @Autowired
	private TemplateEngine templateEngine;
	
	private static String to="996682486@qq.com";
	private static String subject="test";
	@RequestMapping("/sendsimple")
	public void sendsimple()
	{
		String content="hello,this is a simple example";
		myservice.SendSimpleEmail(to, subject, content);
	}
	@RequestMapping("/sendhtml")
	public void sendhtml()
	{
		String content="<body>"+
	    "<h1>hello</h1>"+
		"<h2>i'm a html example</h2>"+
	    "</body>";
		myservice.SendHtmlEmail(to, subject, content);
	}
	@RequestMapping("/sendattatch")
    public void sendatta()
    {
		String content="this is a example with attatchments";
		String url="C:\\Users\\DELL\\Desktop\\springboot定时任务.md";
    	myservice.SendAttachedEmail(to, subject, content,url);
    }
	@RequestMapping("/sendinline")
	public void sendin()
	{
		String pid="001";
		String imageurl="C:\\Users\\DELL\\Pictures\\output.png";
	    String content="<html><body>这是有图片的邮件：<img src=\'cid:" + pid + "\' ></body></html>";
	    myservice.SendInlineEmail(to, subject, content, imageurl, pid);
	}
	@RequestMapping("/sendtemplate")
	public void sendtemplate()
	{
		String name="唐小花";
		Context context=new Context();
	    context.setVariable("name", name);
	    String emailContent = templateEngine.process("emailTemplate", context);
	    myservice.SendHtmlEmail(to,subject,emailContent);
	}
}
