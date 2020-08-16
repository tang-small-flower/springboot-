package com.tust.fir;
import java.io.Serializable;
import javax.persistence.*;
import java.util.*;
@Entity//交给jpa管理实体，并设置映射到数据库的表名
@Table(name = "tab_admin")
public class admin implements Serializable{
	private static final long serialVersionUID = 3L;
	@Id //主键
    @Column(name = "adminId",unique = true,nullable = false) //name属性不写默认和字段一样
    @GeneratedValue
    private Integer adminId;
	public void setadminId(Integer id)
	{
		this.adminId=id;
	}
    public Integer getadminId()
    {
    	return adminId;
    }
    @Column(length = 100,unique = true)
    private String adminUserName;
    public void setAdminUserName(String username)
	{
		this.adminUserName=username;
	}
    public String getAdminUserName()
    {
    	return adminUserName;
    }
    @Column(length = 100)
    private String AdminPassWord;
    public void setAdminPassWord(String password)
	{
		this.AdminPassWord=password;
	}
    public String getAdminPassWord()
    {
    	return AdminPassWord;
    }
    @Column(length = 100)
    private String AdminNickName;
    public void setAdminNickName(String username)
	{
		this.AdminNickName=username;
	}
    public String getAdminNickName()
    {
    	return AdminNickName;
    }
    @Column(length = 255)
    private String AdminNicpic;
    public void setAdminNicpic(String username)
	{
		this.AdminNicpic=username;
	}
    public String getAdminNicpic()
    {
    	return AdminNicpic;
    }
    //一的一方
    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Cat> catList = new ArrayList<Cat>();
    public List<Cat> getCatList()
	{
		return catList;
	}
    public admin()
    {
    	super();
    }
    public admin(Integer id,String username,String name,String s)
    {
    	super();
    	this.adminId=id;
    	this.AdminNickName=username;
    	this.AdminPassWord=name;
    	this.AdminNicpic=s;
    }
    public String toString()
    {
    	return adminId+"-"+AdminNickName+"-"+AdminPassWord+"-"+AdminNicpic;
    }
}
