package com.mybatis;
import java.util.*;
import java.io.Serializable;
public class customer implements Serializable{
	private static final long serialVersionUID=11L;
	private long id;
	private String name;
	private List<order> list;
	public customer()
	{
		super();
	}
	public customer(long id,String name,List<order> list)
	{
		this.id=id;
		this.name=name;
		this.list=list;
	}
	public customer(String name,List<order> list)
	{
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
	public List<order> getorders()
	{
		return this.list;
	}
	public void setname(String name)
	{
		this.name=name;
	}
	public void setid(long id)
	{
		this.id=id;
	}
	public void setlist(List<order> list)
	{
		this.list=list;
	}
	public String toString()
	{
		return id+"-"+name+"-"+list;
	}

}
