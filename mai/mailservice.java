package com.mai;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.slf4j.Logger;
import java.util.*;
import java.io.*;
import org.springframework.core.io.FileSystemResource;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.slf4j.LoggerFactory;
@Service
public class mailservice implements Mailser{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;
    public void SendSimpleEmail(String to,String subject,String content)
    {
    	SimpleMailMessage message=new SimpleMailMessage();
    	message.setFrom(from);
    	message.setTo(to);
    	message.setSubject(subject);
    	message.setText(content);
    	try {
    		mailSender.send(message);
    		logger.info("发送简单邮件成功！");
    	}catch (Exception e)
    	{
            logger.error("发送简单邮件时发生异常！", e);
    	}
    }
    public void SendHtmlEmail(String to,String subject,String content)
    {
        MimeMessage message = mailSender.createMimeMessage();
        try {
    	MimeMessageHelper helper=new MimeMessageHelper(message,true);
    	helper.setFrom(from);
    	helper.setTo(to);
    	helper.setSubject(subject);
    	helper.setText(content,true);
    	mailSender.send(message);
    	logger.info("发送html文件成功！");
        }catch (Exception e)
        {
          logger.error("发送html文件异常！",e);	
        }
    }
    public void SendAttachedEmail(String to,String subject,String content,String url)
    {
    	MimeMessage message=mailSender.createMimeMessage();
    	try {
    		MimeMessageHelper helper=new MimeMessageHelper(message,true);
    		helper.setFrom(from);
    		helper.setTo(to);
    		helper.setSubject(subject);
    		FileSystemResource file = new FileSystemResource(new File(url));
    		String name=url.substring(url.lastIndexOf(File.separator));
            helper.addAttachment(name, file);
    		helper.setText(content,true);
            mailSender.send(message);
            logger.info("带附件的邮件发送成功");
    	}catch (Exception e)
    	{
    		logger.error("添加附件邮件发送异常！",e);
    	}
    }
    public void SendInlineEmail(String to,String subject,String content,String imageurl,String pid)
    {
    	MimeMessage message=mailSender.createMimeMessage();
    	try {
    		MimeMessageHelper helper =new MimeMessageHelper(message,true);
    		helper.setFrom(from);
    		helper.setTo(to);
    		helper.setSubject(subject);
            helper.setText(content,true);
            FileSystemResource file=new FileSystemResource(new File(imageurl));
    		System.out.println(file);
            helper.addInline(pid,file);
            mailSender.send(message);
            logger.info("带静态资源的邮件发送成功！");
    	}catch (Exception e)
    	{
    		logger.error("带静态资源的邮件发送异常！",e);
    	}
    }
}
