package com.tust.fir;
import java.io.Serializable;
import javax.persistence.*;
import java.util.*;
@Entity
@Table(name="husband")
public class husband implements Serializable{
    private static final long serialVersionUID=5L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="husbandid")
	private Integer id;
	@Column
	private String name;
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="mywife",referencedColumnName ="wifeid")
	private wife mywife;
	public void setwife(wife w)
	{
		mywife=w;
	}
	public wife getwife()
	{
		return mywife;
	}
	public husband()
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
	public String getname()
	{
		return name;
	}
	public void setname(String name)
	{
		this.name=name;
	}
	public husband(Integer id,String name)
	{
		this.id=id;
		this.name=name;
	}
	public String toString()
	{
		return name+"-"+id+"-"+mywife;
	}
}
