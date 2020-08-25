package com.mybatis;
import java.io.Serializable;
import java.util.*;
public class student implements Serializable{
	private static final long serialVersionUID=14L;
	private long id;
	private String name;
	private List<teacher> list;
	public student()
	{
		super();
	}
	public student(String name)
	{
		this.name=name;
	}
	public student(long id,String name,List<teacher> list)
	{
		this.id=id;
		this.name=name;
		this.list=list;
	}
	public long getid()
	{
		return id;
	}
	public String getname()
	{
		return name;
	}
	public void setid(long id)
	{
		this.id=id;
	}
	public void setname(String name)
	{
		this.name=name;
	}
	public void setlist(List<teacher> list)
	{
		this.list=list;
	}
	public List<teacher> getlist()
	{
		return list;
	}
	public String toString()
	{
		return id+"-"+name+list;
	}

}
