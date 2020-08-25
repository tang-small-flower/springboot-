package com.mybatis;
import java.io.Serializable;
public class id_card implements Serializable{
	private static final long serialVersionUID=9L;
	private long id;
	private String code;
	public id_card(String code,long id)
	{
		super();
		this.code=code;
		this.id=id;
	}
	public id_card(String code)
	{
		super();
		this.code=code;
	}
	public id_card()
	{
		super();
	}
	public long getid()
	{
		return id;
	}
	public void setid(long id)
	{
		this.id=id;
	}
	public void setcode(String code)
	{
		this.code=code;
	}
	public String getcode()
	{
		return this.code;
	}
	public String toString()
	{
		return id+"-"+code;
	}
}
