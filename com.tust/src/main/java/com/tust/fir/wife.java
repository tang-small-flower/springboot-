package com.tust.fir;
import java.io.Serializable;
import javax.persistence.*;
import java.util.*;
@Entity
@Table(name="wife")
public class wife implements Serializable{
	private static final long serialVersionUID = 4L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="wifeid")
	private Integer id;
	@Column
	private String name;
	@OneToOne(mappedBy="mywife")
	private husband myhusband;
	public wife()
	{
		super();
	}
	public Integer getid()
	{
		return id;
	}
	public void setid(Integer id)
	{
		this.id=id;
	}
	public void sethusband(husband h)
	{
		this.myhusband=h;
	}
	public husband gethusband()
	{
		return myhusband;
	}
	public String getname()
	{
		return name;
	}
	public void setname(String name)
	{
		this.name=name;
	}
	public wife(Integer id,String name)
	{
		this.id=id;
		this.name=name;
	}
	public String toString()
	{
		return name+"-"+id;
	}

}
