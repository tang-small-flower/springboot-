

```python
Spring Data JPA 实现多表关联查询(主要描述级联的操作) 
1、Hibernate级联  
>>（1）单向与双项关联是什么？参考：https://blog.csdn.net/xiangwanpeng/article/details/53457124  
>>>>(a)单向关联：只需要类A知道类B，而不需要类B知道类A    
>>>>(b)双向关联：类A需要知道类B，而类B也需要知道类A  
>>>>总结：单向关联是我中有你，你中无我；双向关联是我中有你，你中有我。  
>>（2）级联是什么？  
>>>>级联是用来设计一对多关系的。例如一个表存放老师的信息:表A（姓名，性别，年龄），姓名为主键。还有一张表存放老师所教的班级信息:表B
（姓名，班级）。他们通过姓名来级联。级联的操作有级联更新，级联删除等。在启用一个级联更新选项后，就可在存在相匹配的外键值的前提下更改一个主键值。
系统会相应地更新所有匹配的外键值。如果在表A中将姓名为张三的记录改为李四，那么表B中的姓名为张三的所有记录也会随着改为李四。级联删除与更新相类似。
如果在表A中将姓名为张三的记录删除，那么表B中的姓名为张三的所有记录也将删除。  
>>>>再比如我们有两个实体主人admin和猫cat，一个主人可能有多个猫，一个猫只能有一个主人，我们想做这样一个事情，可不可以在数据库的cat中给定一个外键adminid
（reference 数据库admin中的adminid）的情况下，我写一个操作只向数据库添加一只猫的信息（名字，主人，宠物id），怎样能够同时向数据库插入主人的信息呢？
>>>>如果还不太懂的话我再举个例子，两个实体学校school和学生student,school的数据库中包含schoolname和id两列，student的数据库中包含studentid和studentname
和schoolname（外键reference school.schoolname）三列，我们想进行这样的操作，在保存新建的学校的同时新建的学生也被保存或者在保存新建的学生的同时保存新建的学
校。  
>>>>如果再不清楚，我们就拿最简单的wife和husband来讲，一夫一妻制，我们想要完成这样一件事，新婚夫妇登记时用一个操作向数据库插入妻子信息的同时也插入
丈夫的信息。  
>>（3）关于维护方和被维护方，参考https://blog.csdn.net/u011302734/article/details/76868090  
>>>>哪个实体声明了外键哪个实体就是维护方，一般在一对多的情况下多的一方是维护方，分维护方和被维护方有什么用呢？在保存数据时，总是先保存的是没有维护关
联关系的那一方的数据，后保存维护了关联关系的那一方的数据。这么理解可能太抽象，我们用例子说明一下，有两个实体，admin主人和cat猫，一个主人可能有多
个猫，但一个猫只能有一个主人，也就是说主人和猫之间是一对多的关系，我们选择多的一方猫当维护端，主人为被维护端，那么如果我们在维护端设置级联，执行插入
操作时候,我们可以省略向admin实体内属性值中添加cat，数据库也会先在admin的表中插入定义的admin，然后插入猫的信息。在这个过程中，我们并没有save admin主人（被
维护方）的信息但却保存了被维护方的数据。
...
            admin admin = new admin();//被维护方
            admin.setAdminUserName("admin");
            admin.setAdminPassWord("123");
            admin.setAdminNickName("yoyo");
            Cat cat = new Cat();//维护方
            cat.setcatName("tom");
            cat.setAdmin(admin);
           //下面一句可有可无
          //admin.getCatList().add(cat);
            catrepository.save(cat);//保存结果：双方都插入了对象，并且维护方cat有外键adminid并不是null，也就是记录了admin和cat的联系
          //控制台输出的
          //Hibernate: insert into tab_admin (admin_nick_name, admin_nicpic, admin_pass_word, admin_user_name, admin_id) values (?, ?, ?, ?, ?)
          //Hibernate: insert into tab_cat (admin_id, cat_name) values (?, ?)
...
>>（4）常用的注释：
>>>>(a)mappedBy参数指定由另一方担当维护方，加入mappedBy属性有什么好处呢？可以在查删改操作避免生成一张中间表。  
>>>>(b)@OneToOne(mappedBy=”user”)        指定了OneToOne的关联关系，mappedBy指定由对方来进行维护关联关系，user为自己在对方类中的属性名  
>>>>(c)@JoinColumn(name=”pid”)　　       指定外键的名字 pid  
>>>>(d)@ManyToMany(mappedBy=”teachers”)　　表示由另外一方来进行维护，teacher为自己在对方类中的属性名
>>>>(e)@JoinTable(name=”t_teacher_course”, joinColumns={ @JoinColumn(name=”cid”)}, inverseJoinColumns={ @JoinColumn(name = “tid”) })
因为多对多之间会通过一张中间表来维护两表直接的关系，所以通过 JoinTable 这个注解来声明，name就是指定了中间表的名字，JoinColumns是一个 
@JoinColumn类型的数组，表示的是我这方在对方中的外键名称，我方是Course，所以在对方外键的名称就是 rid，inverseJoinColumns也是一个 
@JoinColumn类型的数组，表示的是对方在我这放中的外键名称，对方是Teacher，所以在我方外键的名称就是 tid  
>>>>(f)@OneToMany(mappedBy=”role”)      指定了一对多的关系，mappedBy=”role”指定了由多方来维护关联关系，mappedBy指的是多方对这一方的
依赖的属性，(注意：如果没有指定由谁来维护关联关系，则系统会给我们创建一张中间表)  
>>>>(g)@ManyToOne(fetch=FetchType.LAZY)   指定了多对一的关系，fetch=FetchType.LAZY属性表示在多的那一方通过延迟加载的方式加载对象(默
认不是延迟加载)  
>>>>(h)@OneToMany(mappedBy="shcool",cascade={CascadeType.PERSIST})其中cascade关系有以下几种：all: 所有情况下均进行关联操作，即save-update和delete。
；none: 所有情况下均不进行关联操作，这是默认值；save-update: 在执行save/update/saveOrUpdate时进行关联操作。 delete: 在执行delete 时进行关联操作。
；all-delete-orphan: 当一个节点在对象图中成为孤儿节点时，删除该节点；  
>>（5）双向关联：  
>>>>@OneToOne (wife作为被维护方，husband作为维护方有外键联系被维护方)  
>>>>>>拿wife和husband做例子(趁机解释双向关联的含义，husband中有wife属性且wife中有husband属性)    
>>>>>>>>i.如果双方都不设置cascade，也就是none: 所有情况下均不进行关联操作，这是默认值。  
>>husband实体  
...
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
	@OneToOne
	@JoinColumn(name="mywife",referencedColumnName ="wifeid")//name是外键的名字，referencedColumnName是数据库中对方的列的名字
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
...
>>wife实体  
...
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
...
...
package com.tust.fir;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface husband_repository extends JpaRepository<husband,Integer>{
        public husband findByName(String name);
}
...
...
package com.tust.fir;
import org.springframework.data.jpa.repository.JpaRepository;
public interface wife_repository extends JpaRepository<wife,Integer>{
	public wife findByName(String name);

}
...
...
package com.tust.fir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
@RestController()
@RequestMapping("getwife")
public class wife_husband_controll {
	@Autowired
	husband_repository husbandre;
	@Autowired
	wife_repository wifere;
    @Transactional
    @RequestMapping("/addpair")
    public void addpair()
    {
    	wife me=new wife();
    	me.setname("唐小花");
    	husband myhusband=new husband();
    	myhusband.setname("吴邪");
    	myhusband.setwife(me);
    	wifere.save(me);          //此时可以实现两个表都插入，且外键不为null
                                  //Hibernate: insert into wife (name) values (?)
                                  //Hibernate: insert into husband (mywife, name) values (?, ?)
    	husbandre.save(myhusband);
    }
    /*只有被维护方设置联级无法删除   以下都无法*/
    @Transactional
    @RequestMapping("/wifedelete")
    public void dele()
    {
    	husband myhusband=husbandre.findByName("吴邪");
    	wife me=myhusband.getwife();
    	//myhusband.setwife(null);
    	wifere.delete(me);
    	husbandre.delete(myhusband);
    }
    @Transactional
    @RequestMapping("/husbanddelete")
    public void delet()
    {
    	husband myhusband=husbandre.findByName("吴邪");
        wife me=myhusband.getwife();
        me.sethusband(null);
    	husbandre.delete(myhusband);
    }
    @Transactional
    @RequestMapping("/wifedeletewith")
    public void del()
    {
    	husband myhusband=husbandre.findByName("吴邪");
    	wife me=myhusband.getwife();
    	myhusband.setwife(null);
    	wifere.delete(me);
    }
}
...
>>>>>>>>ii如果维护方添加级联，设置cascade为CascadeType.ALL
>>husband实体
...
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
...
...
package com.tust.fir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
@RestController()
@RequestMapping("getwife")
public class wife_husband_controll {
	@Autowired
	husband_repository husbandre;
	@Autowired
	wife_repository wifere;
    @Transactional
    @RequestMapping("/husbandaddpair")
    public void addpair()
    {
    	wife me=new wife();
    	me.setname("唐小花");
    	husband myhusband=new husband();
    	myhusband.setname("吴邪");
    	/*me.sethusband(myhusband);可有可无*/
    	myhusband.setwife(me);
    	husbandre.save(myhusband);//实现了级联，我们在插入维护方husband信息前先插入了被维护方wife的信息：Hibernate: insert into wife (name) values (?)  Hibernate: insert into husband (mywife, name) values (?, ?)
    }
    @Transactional
    @RequestMapping("/wifeaddpair")
    public void add()
    {
    	wife me=new wife();
    	me.setname("唐小花");
    	husband myhusband=new husband();
    	myhusband.setname("吴邪");
    	me.sethusband(myhusband);
    	myhusband.setwife(me);
    	wifere.save(me);//由于被维护方没有设置级联，那么当操作被维护方时，维护方不受影响，也就是只插入了wife表中，没有顺带着husband Hibernate: insert into wife (name) values (?)
    }
     @Transactional
    @RequestMapping("/wifedelete")
    public void dele()
    {
    	husband myhusband=husbandre.findByName("吴邪");
    	wife me=myhusband.getwife();
    	myhusband.setwife(null);//若不加这一句在删除wife的同时会报错，加了这句wife会被delete掉但husband不会，在数据库中他的wife为null
                                //Hibernate: select husband0_.husbandid as husbandi1_0_, husband0_.mywife as mywife3_0_, husband0_.name as name2_0_ from husband husband0_ where husband0_.name=? 
                                //Hibernate: select wife0_.wifeid as wifeid1_7_0_, wife0_.name as name2_7_0_, husband1_.husbandid as husbandi1_0_1_, husband1_.mywife as mywife3_0_1_, husband1_.name as name2_0_1_ from wife wife0_ left outer join husband husband1_ on wife0_.wifeid=husband1_.mywife where wife0_.wifeid=?
                                //Hibernate: select husband0_.husbandid as husbandi1_0_1_, husband0_.mywife as mywife3_0_1_, husband0_.name as name2_0_1_, wife1_.wifeid as wifeid1_7_0_, wife1_.name as name2_7_0_ from husband husband0_ left outer join wife wife1_ on husband0_.mywife=wife1_.wifeid where husband0_.mywife=?
                                //Hibernate: update husband set mywife=?, name=? where husbandid=?
                                //Hibernate: delete from wife where wifeid=?
    	wifere.delete(me);
    }
    @Transactional
    @RequestMapping("/husbanddelete")
    public void delet()
    {
    	husband myhusband=husbandre.findByName("吴邪");
    	husbandre.delete(myhusband);//从两个表中删除
    }
}
...
>>若将husband中的cascade=CascadeType.ALL设置移除，在wife实体中更改为@OneToOne(name="mywife",cascade=CascadeType.ALL)
...
package com.tust.fir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
@RestController()
@RequestMapping("getwife")
public class wife_husband_controll {
	@Autowired
	husband_repository husbandre;
	@Autowired
	wife_repository wifere;
	/*被维护方设置级联且save维护方*/
    @Transactional
    @RequestMapping("/husbandaddpair")
    public void addpair()
    {
    	wife me=new wife();
    	me.setname("唐小花");
    	husband myhusband=new husband();
    	myhusband.setname("吴邪");
    	me.sethusband(myhusband);
    	myhusband.setwife(me);
    	husbandre.save(myhusband);//我猜测由于通过外键查找不到唐小花的id，数据库中没有，又没有设置级联不能先插入wife的id，所以插入失败，报错
    }
    /*如果两个实体类都set对方，并且save一方配置级联那么就可以实现正常插入两个表外键不外null*/
    @Transactional
    @RequestMapping("/wifeaddpair")
    public void add()
    {
    	wife me=new wife();
    	me.setname("唐小花");
    	husband myhusband=new husband();
    	myhusband.setname("吴邪");
    	me.sethusband(myhusband);
    	myhusband.setwife(me);//如果不加这一句，那么最终插入到husband中的数据不完整，外键为null
    	wifere.save(me);//两个实体双方都set到对方的属性中，即使wife不是维护端（但wife设置了级联），也可以完整插入两个表中且外键不为null
    }
}
...
>>两个都加cascade=CascadeType.ALL
...
package com.tust.fir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
@RestController()
@RequestMapping("getwife")
public class wife_husband_controll {
	@Autowired
	husband_repository husbandre;
	@Autowired
	wife_repository wifere;
	/*维护方设置级联那么只需要将被维护类set到维护类中就可以实现两个表都插入且外键不为null了*/
    @Transactional
    @RequestMapping("/husbandaddpair")
    public void addpair()
    {
    	wife me=new wife();
    	me.setname("唐小花");
    	husband myhusband=new husband();
    	myhusband.setname("吴邪");
    /*	me.sethusband(myhusband);可省略*/
    	myhusband.setwife(me);
    	husbandre.save(myhusband);
    }
    /*被维护方如果设置级联但只将维护类set到被维护类，确实能将两个表都插入，但外键为null*/
    @Transactional
    @RequestMapping("/wifeaddpair")
    public void add()
    {
    	wife me=new wife();
    	me.setname("唐小花");
    	husband myhusband=new husband();
    	myhusband.setname("吴邪");
    	me.sethusband(myhusband);
    	wifere.save(me);
    }
    /*被维护端如果设置级联，并且双方实体类都set了对方的属性，无论是不是被维护端，都可以插入两个表且外键不为null*/
    @Transactional
    @RequestMapping("/wifeaddpairwith")
    public void a()
    {
    	wife me=new wife();
    	me.setname("唐小花");
    	husband myhusband=new husband();
    	myhusband.setname("吴邪");
    	me.sethusband(myhusband);
    	myhusband.setwife(me);
    	wifere.save(me);
    }
    /*都设置级联不论从哪一方删除，都会连带着对方一起从数据库中消失*/
    @Transactional
    @RequestMapping("/wifedelete")
    public void dele()
    {
    	husband myhusband=husbandre.findByName("吴邪");
    	wife me=myhusband.getwife();
    	/myhusband.setwife(null);可省略/
    	wifere.delete(me);
    }
    @Transactional
    @RequestMapping("/husbanddelete")
    public void delet()
    {
    	husband myhusband=husbandre.findByName("吴邪");
    	husbandre.delete(myhusband);
    }
}
...
>>>>@OneToMany与@ManyToOne 一般我们选取多的那一端作为维护端，并在其中声明reference少的一端的外键  
>>>>>>我们举例子猫和主人（多对一）猫作为维护端，主人作为被维护端。  
>>两端都设置级联
>>猫的实体  
...
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
...
>>人的实体  
...
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
...
...
package com.tust.fir;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
public interface cat_repository extends JpaRepository<Cat,Integer>{
       public Cat findByCatName(String catname);
}
...
...
package com.tust.fir;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
public interface admin_repository extends JpaRepository<admin,Integer>{
      public admin findByAdminUserName(String username);
}
...
...
package com.tust.fir;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
@RestController
@RequestMapping("test1")
public class admin_cat_controll {
	@Autowired
	private admin_repository adminre;
	@Autowired
    private cat_repository catre;
    /*被维护方只set了维护方而维护方没有set被维护方，且在被维护方设置了级联，两个表都插入但外键为null*/
	@RequestMapping("/addAdmin")
    @Transactional
    public void addAdmin() {
        admin ad = new admin();
        ad.setAdminUserName("admin");
        ad.setAdminPassWord("123");
        ad.setAdminNickName("yoko");
        Cat cat = new Cat();
        cat.setcatName("Tom");
        Cat cat1 = new Cat();
        cat1.setcatName("Miki");
        ad.getCatList().add(cat);
        ad.getCatList().add(cat1);
        //保存结果：双方都插入了对象，但是维护方（cat）没有外键
        adminre.save(ad);
    }
    /*维护方只需要set被维护方，且维护方设置了级联，就可以插入两个表且外键不为null*/
	    @RequestMapping("/addCat")
	    @Transactional
	    public void addCat() {
	        admin admin = new admin();//被维护方
	        admin.setAdminUserName("admin6");
	        admin.setAdminPassWord("123");
	        admin.setAdminNickName("yo");
	        Cat cat = new Cat();//维护方
	        cat.setcatName("iji");
	        cat.setAdmin(admin);
	        /*admin.getCatList().add(cat);可有可无*/
	        catre.save(cat);//保存结果：双方都插入了对象，并且维护方有外键
	    }
    /*不管是维护端还是被维护端只要两个实体互相set，并且保证save的设置了级联就可以插入且外键不为null*/
	    @RequestMapping("/addAdminCat")
	    @Transactional
	    public void addAdminCat() {
	        admin admin = new admin();
	        admin.setAdminUserName("admin23");
	        admin.setAdminPassWord("123232");
	        admin.setAdminNickName("yoko223");
	        Cat cat = new Cat();
	        cat.setcatName("Tom2223");
	        cat.setAdmin(admin);
	        Cat cat1 = new Cat();
	        cat1.setcatName("Miki2223");
	        cat1.setAdmin(admin);
	        admin.getCatList().add(cat);
	        admin.getCatList().add(cat1);
	        //保存结果：设置了双向关联，即便插入对象是被维护方，2张表都有数据并且维护方（cat）中有外键
	        adminre.save(admin);
	    }
	    @RequestMapping("/deleteCat")
	    @Transactional
	    public void deleteCat() {
	        Cat cat = catre.findByCatName("Miki2");
	        System.out.println(cat);
	        //由于二级缓存机制，要清空级联对象中的自己对象，要不无法删除，不会有删除语句
	        // cat.getAdmin().getCatList().remove(cat);
	        //可以看到在进行上一步设置后，成功有了删除语句delete from tab_cat where catId=?
	        //删除结果：由于未设置级联删除，即便删除的是维护方对象，也只删除了自己，不能删除Admin
	        catre.delete(cat);
	    }
	    @RequestMapping("/deleteAdmin")
	    @Transactional
	    public void deleteAdmin() {
	        admin admin3 = adminre.findByAdminUserName("admin3");
	        System.out.println(admin3);
	        //虽然删除的是被维护端对象，但是设置了级联删除，能顺利删除两张表的数据，这里没有缓存机制作祟
	        adminre.delete(admin3);
	    }
}
...
>>@ManyToMany  
>>>>多对多的关系，有一边要放弃维护,不然在保存时，会造成死循环
...
package com.tust.fir;
import java.io.Serializable;
import javax.persistence.*;
import java.util.*;
@Entity
@Table(name="student")
public class student implements Serializable{
	private static final long serialVersionUID=6L;
	private Integer studentid;//学生ID  
    private String studentName;//学生姓名  
    private Set<teacher> teachers = new HashSet<teacher>();//对应的教师集合  
  
    public student() {  
    }  
  
    public student(String studentName) {  
        this.studentName = studentName;  
    }  
  
    @Id  
    @GeneratedValue  
    public Integer getStudentid() {  
        return studentid;  
    }  
  
    public void setStudentid(Integer studentid) {  
        this.studentid = studentid;  
    }  
  
    @Column(nullable = false, length = 32)  
    public String getStudentName() {  
        return studentName;  
    }  
  
    public void setStudentName(String studentName) {  
        this.studentName = studentName;  
    }  
      
    /* 
     * @ManyToMany 注释表示Student是多对多关系的一边，mappedBy 属性定义了Student 为双向关系的维护端 
     */  
    @ManyToMany(mappedBy = "students")  
    public Set<teacher> getTeachers() {  
        return teachers;  
    }  
  
    public void setTeachers(Set<teacher> teachers) {  
        this.teachers = teachers;  
    }  
}
...
...
package com.tust.fir;
import java.io.Serializable;
import javax.persistence.*;
import java.util.*;
@Entity
@Table(name="teacher")
public class teacher implements Serializable{
	private static final long serialVersionUID=7L;
	private Integer teacherid;// 教师ID  
    private String teacherName;// 教师姓名  
    private Set<student> students = new HashSet<student>();// 对应的学生集合  
  
    public teacher() {  
  
    }  
  
    public teacher(String teacherName) {  
        this.teacherName = teacherName;  
    }  
  
    @Id  
    @GeneratedValue  
    public Integer getTeacherid() {  
        return teacherid;  
    }  
  
    public void setTeacherid(Integer teacherid) {  
        this.teacherid = teacherid;  
    }  
  
    @Column(nullable = false, length = 32)  
    public String getTeacherName() {  
        return teacherName;  
    }  
  
    public void setTeacherName(String teacherName) {  
        this.teacherName = teacherName;  
    }  
    /* 
     * @ManyToMany 注释表示Teacher 是多对多关系的一端。 
     * @JoinTable 描述了多对多关系的数据表关系，name属性指定中间表名称。 
     * joinColumns 定义中间表与Teacher 表的外键关系，中间表Teacher_Student的Teacher_ID 列是Teacher 表的主键列对应的外键列。 
     * inverseJoinColumns 属性定义了中间表与另外一端(Student)的外键关系。 
     */  
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)  
    @JoinTable(name = "Teacher_Student",   
            joinColumns ={@JoinColumn(name = "teacher_ID", referencedColumnName = "teacherid") },   
            inverseJoinColumns = { @JoinColumn(name = "student_ID", referencedColumnName = "studentid")   
    })  
    public Set<student> getStudents() {  
        return students;  
    }  
  
    public void setStudents(Set<student> students) {  
        this.students = students;  
    }  
      
    public void addStudent(student student) {  
        if (!this.students.contains(student)) {//检测在该散列表中某些键是否映射到指定值,value 查找的值。如果某些键映射到该散列表中的值为true，否则false  
            this.students.add(student);  
        }  
    }  
  
    public void removeStudent(student student) {  
        this.students.remove(student);  
    }  
}
...
...
package com.tust.fir;
import org.springframework.data.jpa.repository.JpaRepository;
public interface t_repo extends JpaRepository<teacher,Integer>{

}
...
...
package com.tust.fir;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface t_repo extends JpaRepository<teacher,Integer>{
             public teacher findByTeacherName(String name);
             @Query(nativeQuery=true,value="select t.teacher_name,t.teacherid,GROUP_CONCAT(s.student_name) from teacher t join l on t.teacherid=l.teacher_id join student s on l.student_id=s.studentid where t.teacher_name like %?1% group by t.teacherid")
             public List<Object[]> findbyteachernamelike(String name);
}
...
...
package com.tust.fir;
import org.springframework.data.jpa.repository.JpaRepository;
public interface s_repo extends JpaRepository<student,Integer>{
             public student findByStudentName(String name);
}
...
...
package com.tust.fir;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
@RestController
@RequestMapping("student")
public class student_teacher_controller {
	@Autowired
	t_repo tre;
	@Autowired
	s_repo sre;
	@RequestMapping("/addteacher")
    @Transactional
    public void addAdmin() {
        student s1 = new student();
        s1.setStudentName("唐小花");
        student s2=new student();
        s2.setStudentName("吴邪");
        teacher t1=new teacher();
        t1.setTeacherName("嫄姐");
        teacher t2=new teacher();
        t2.setTeacherName("老邓头");
        t1.addStudent(s1);
        t2.addStudent(s2);
        t2.addStudent(s1);
        tre.save(t1);
        tre.save(t2);
    }
    /*只删除了教师表中的嫄姐与关系表中与嫄姐联系的关系，student表不受影响*/
	@RequestMapping("/deletest")
	@Transactional
	public void deletestudentandteacher()
	{
		 teacher t=tre.findByTeacherName("嫄姐");
		 for(student s:t.getStudents())
		 {
			 s.deleteteacher(t);
		 }
		 tre.delete(t);
	}
	/*教师表删除，学生表不变，联系表删除与该教师有关的记录*/
	@RequestMapping("/deletestwith")
	@Transactional
	public void deleteteacher()
	{
		 teacher t=tre.findByTeacherName("嫄姐");
		 tre.delete(t);
		 /*Hibernate: select teacher0_.teacherid as teacheri1_5_, teacher0_.teacher_name as teacher_2_5_ from teacher teacher0_ where teacher0_.teacher_name=?
           Hibernate: delete from l where teacher_id=?
           Hibernate: delete from teacher where teacherid=?*/
	}
	/*sre端没有设置级联，且没有从教师端删除学生报错*/
	@RequestMapping("/dstudent")
	@Transactional
	public void deletestudent()
	{
		 student s=sre.findByStudentName("唐小花");
		 sre.delete(s);
	}
	/*在teacher端删除了学生，delete成功，删除了student表唐小花与l表联系表与唐小花有联系的所有内容，而教师表不变*/
	@RequestMapping("/dstudentw")
	@Transactional
	public void deletestudentd()
	{
		 student s=sre.findByStudentName("唐小花");
		 for(teacher t:s.getTeachers())
		 {
			 t.removeStudent(s);
		 }
		 sre.delete(s);
	}
	/*Hibernate: select student0_.studentid as studenti1_2_, student0_.student_name as student_2_2_ from student student0_ where student0_.student_name=?
      Hibernate: select teachers0_.student_id as student_2_1_0_, teachers0_.teacher_id as teacher_1_1_0_, teacher1_.teacherid as teacheri1_5_1_, teacher1_.teacher_name as teacher_2_5_1_ from l teachers0_ inner join teacher teacher1_ on teachers0_.teacher_id=teacher1_.teacherid where teachers0_.student_id=?
      Hibernate: select students0_.teacher_id as teacher_1_1_0_, students0_.student_id as student_2_1_0_, student1_.studentid as studenti1_2_1_, student1_.student_name as student_2_2_1_ from l students0_ inner join student student1_ on students0_.student_id=student1_.studentid where students0_.teacher_id=?
      Hibernate: delete from l where teacher_id=? and student_id=?
      Hibernate: delete from student where studentid=?*/
    @RequestMapping("/findname")
	@Transactional
	public List<Object[]> find(String name)
	{
		 return tre.findbyteachernamelike(name);//http://localhost:8080/student/findname?name=老  返回[["老邓头",29,"吴邪"],["老邓头",33,"唐小花,吴邪"]]
	}
}
...
>>>>特点：  
>>>>>>在双方都设置级联的情况下  
>>>>>>1、在维护方添加被维护方到维护方的某个属性，并最终save维护方结果都插入；  
>>>>>>2、在被维护方插入维护方到被维护方的某个属性，并最终save被维护方，结果外键为null；  
>>>>>>3、如果相互插入对方到自己的某个属性，save任意方，都插入；  
>>>>>>有一方没设置级联的情况下  
>>>>>>1、如果互相插入对方到自己的某个属性，save设置级联的一方，都插入   
>>>>>>2、如果维护方是设置级联的一方，添加被维护方到自己的某个属性，save维护方，都插入    
>>>>>>3、如果被维护方是设置级联的一方，添加维护方到自己的某个属性，并最终save维护方，外键为null   
>>>>总结：    
>>>>>>若维护方有设置级联，并最终想save维护方且保证两个表都插入且外键联系也完整，那么只需要将被维护方添加到自己的某个属性中，save即可  
>>>>>>若被维护方设置级联，并最终想save被维护方且保证两个表都插入且外键完整，那么需要维护和被维护双方相互添加对方到自己属性，save即可
>>>>理解：  
关于缓存以及生命周期等问题，我做了一个小实验https://blog.csdn.net/yingxiake/article/details/50968059  
...
/*我把cat命名为iji，设置主人并保存，然后定义另一只猫c，设置名字和主人，修改cat猫的名字为tang，最后save c的信息，最终cat猫插入到数据库中的名字为tang
结论：  
情况1.修改user对象的属性，方法后续还有对数据库的操作，则该user对象的更改会被自动提交到数据库进行保存  
情况2.修改user对象的属性，方法后续没有对数据库的操作，则该user对象的更改不会被自动提交到数据库*/
@RequestMapping("/addCat")
	    @Transactional
	    public void addCat() {
	        admin admin = new admin();//被维护方
	        admin.setAdminUserName("admin6");
	        admin.setAdminPassWord("123");
	        admin.setAdminNickName("yo");
	        Cat cat = new Cat();//维护方
	        cat.setcatName("iji");
	        cat.setAdmin(admin);
	        admin.getCatList().add(cat);
	        catre.save(cat);//保存结果：双方都插入了对象，并且维护方有外键
	        cat.setcatName("tang");
	        Cat c=new Cat();
	        c.setcatName("okk");
	        c.setAdmin(admin);
	        catre.save(c);
	    }
...
>>（6）单向关联：https://blog.csdn.net/wild46cat/article/details/51760559   (与双向差不多简单一点就不细说啦)  
2、创建一个结果集的接口来接收连表查询后的结果参考：https://blog.csdn.net/johnf_nash/article/details/80587204  
```
