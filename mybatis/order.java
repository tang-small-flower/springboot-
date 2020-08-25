package com.mybatis;
import java.io.Serializable;
public class order implements Serializable{
	private static final long serialVersionUID=12L;
	private long id;
	private String name;
	public order()
	{
		super();
	}
	public order(String name)
	{
		super();
		this.name=name;
	}
	public order(long id,String name)
	{
		super();
		this.id=id;
		this.name=name;
	}
	public void setid(long id)
	{
		this.id=id;
	}
	public void setname(String name)
	{
		this.name=name;
	}
	public long getid()
	{
		return id;
	}
	public String getname()
	{
		return name;
	}
	public String toString()
	{
		return id+"-"+name;
	}

}
