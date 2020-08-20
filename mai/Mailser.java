package com.mai;
public interface Mailser {
   public void SendSimpleEmail(String to,String subject,String content);
   public void SendHtmlEmail(String to,String subject,String content);
   public void SendAttachedEmail(String to,String subject,String content,String url);
   public void SendInlineEmail(String to,String subject,String content,String imageurl,String pid);
}
