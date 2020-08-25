package com.mybatis;
import java.io.Serializable;
import java.util.*;
public class teacher implements Serializable{
	private static final long serialVersionUID=14L;
	private long id;
	private String name;
	private List<student> list;
	public teacher()
	{
		super();
	}
	public teacher(String name)
	{
		this.name=name;
	}
	public teacher(long id,String name,List<student> list)
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
	public List<student> getlist()
	{
		return list;
	}
	public void setlist(List<student> list)
	{
		this.list=list;
	}
	public String toString()
	{
		return id+"-"+name+list;
	}

}
