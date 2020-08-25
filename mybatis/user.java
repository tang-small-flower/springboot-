package com.mybatis;
import java.io.Serializable;
public class user implements Serializable { 
	private static final long serialVersionUID = 1L; 
	private Long id;
	private String userName; 
	private String passWord; 
	private id_card idcard;
	public user() { 
		super();
       } 
	public user(String userName, String passWord,id_card idcard) { 
		super(); 
		this.userName = userName; 
		this.passWord = passWord;
		this.idcard=idcard;
      } 
	public user(String userName, String passWord) { 
		super(); 
		this.userName = userName; 
		this.passWord = passWord;
      } 
	public void setidcard(id_card idcard)
	{
		this.idcard=idcard;
	}
	public id_card getidcard()
	{
		return idcard;
	}
	public Long getId() { 
		return id;
     } 
	public void setId(Long id) { 
		this.id = id;
     } 
	public String getUserName() { 
		return userName;
     } 
	public void setUserName(String userName) { 
		this.userName = userName;
     } 
	public id_card getcard()
	{
		return this.idcard;
	}
	public String getPassWord() { 
		return passWord;
    } 
	public void setPassWord(String passWord) {  
	    this.passWord = passWord;
    }
    public String toString() { 
    	return "userName " + this.userName + ", pasword " +  this.passWord+"-"+this.idcard;
    }
}













