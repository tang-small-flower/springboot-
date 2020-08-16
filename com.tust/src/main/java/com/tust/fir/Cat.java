package com.tust.fir;
import java.io.Serializable;
import javax.persistence.*;
import java.util.*;
@Entity//交给jpa管理实体，并设置映射到数据库的表名
@Table(name = "tab_cat")
public class Cat implements Serializable{
	   private static final long serialVersionUID = 2L;
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer catId;
	    @Column(nullable = false)
	    private String catName;
	    public void setcatId(Integer id)
		{
			this.catId=id;
		}
	    public Integer getCatId()
	    {
	    	return catId;
	    }
	    public void setcatName(String name)
		{
			this.catName=name;
		}
	    public String getcatName()
	    {
	    	return catName;
	    }
	    public Cat()
	    {
	    	super();
	    }
	    //多的一方
	    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.EAGER)//设置了级联增/改/刷新
	    @JoinColumn(name = "adminId",referencedColumnName = "adminId")
	    private admin admin;
	    public void setAdmin(admin admin)
	    {
	       this.admin=admin;
	    }
	    public admin getAdmin()
	    {
	    	return admin;
	    }
	    public Cat(Integer t,String s)
	    {
	    	super();
	    	this.catId=t;
	    	this.catName=s;
	    }
	    public String toString()
	    {
	    	return catId+"-"+catName+"-"+admin;
	    }
}
