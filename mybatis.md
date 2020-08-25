
springboot+mybatis
/* 传统方式编写xml文件  (一对一单向，一对多单向，多对多单双向)  问题解决：不同表列名冲突，赋给实体的数值不对
  基于注解  （举例一对多）
  基于插件自动生成  （举例）
*/


```python
1、java持久层框架mybatis与hibernate的区别
   mybatis是半自动映射框架，因为mybatis需要手动配置POJO、SQL和映射关系
   hibernate是一个全表映射框架，只需要提供POJP和映射关系即可
   mybatis是一个小巧、方便、高效、简单、直接半自动化的持久层框架，而hibernate是一个强大的方便、高效、复杂、间接、全自动化持久层框架。
2、mybatis的工作原理：
 （1）读取mybatis配置文件，mybatis-config.xml，配置了mybatis运行环境等信息，例如数据库连接信息
 （2）加载映射文件，映射文件即SQL映射文件，配置了操作数据库的SQL语句，需要在mybatis-config.xml中加载，可以加载多个，每个文件对应数据库中的一张表
 （3）构造会话工厂，通过环境等配置信息构造会话工厂SqlSessionFactory
 （4）由会话工厂创建SqlSession对象，他包含执行SQL语句的所有方法
 （5）mybatis底层定义了一个Executor接口来操作数据库，将SqlSession中的参数动态的生成要执行的SQL语句同时负责查询缓存的维护
 （6）MappedStatement对象，Executor接口的执行方法有一个MappedStatement类型的参数，该类型的参数是对映射信息进行封装，用于存储要映射的SQL语句的id、参数等信息。
 （7）输入参数映射->MappedStatement->输出结果映射
3、编码实现：
  1）添加依赖：
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
      </dependency>
      <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.0.0</version>
      </dependency>
      <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
      </dependency>
    <dependency>
   <groupId>org.slf4j</groupId>
   <artifactId>slf4j-log4j12</artifactId>
   <version>1.7.6</version>
   </dependency>
    </dependencies>
  2）向application.properties中添加配置
         mybatis.config-location=classpath:mapper/mybatis_config.xml  // src/main/resources/mapper/mybatis_config.xml
         mybatis.mapper-locations=classpath:mapper/*Mapper.xml
         mybatis.type-aliases-package=com.mybatis
         spring.datasource.driverClassName = com.mysql.cj.jdbc.Driver
         spring.datasource.url =  jdbc:mysql://localhost:3306/spring_boot?serverTimezone=GMT%2B8
         spring.datasource.username = root
         spring.datasource.password = 123456
         
         log4j.logger.com.ibatis=DEBUG
         log4j.logger.com.ibatis.common.jdbc.SimpleDataSource=DEBUG
         log4j.logger.com.ibatis.common.jdbc.ScriptRunner=DEBUG
         log4j.logger.com.ibatis.sqlmap.engine.impl.SqlMapClientDelegate=DEBUG
         log4j.logger.Java.sql.Connection=DEBUG
         log4j.logger.java.sql.Statement=DEBUG
         log4j.logger.java.sql.PreparedStatement=DEBUG
  3）编写mybatis全局配置文件
  MyBatis 的配置文件包含了会深深影响 MyBatis 行为的设置（settings）和属性（properties）信息。关于配置文件的详解请看 https://mybatis.org/mybatis-3/zh/configuration.html。
  <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config  3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
      <settings> 
        <!-- 打印查询语句 -->
        <setting name="logImpl" value="STDOUT_LOGGING" />
    </settings>
  </configuration>
  /*补充mybatis 配置文件中别名以及namespace：
    MyBatis中如果每次配置类名都要写全称也太不友好了，我们可以通过在主配置文件中配置别名，就不再需要指定完整的包名了。
    <configuration>  
      <typeAliases>  
        <typeAlias type="com.domain.Student" alias="Student"/>  
      </typeAliases>  
     ......  
    </configuration>
     但是如果每一个实体类都这样配置还是有点麻烦这时我们可以直接指定package的名字， mybatis会自动扫描指定包下面的javabean，并且默认设置一个别名，默认的名字为： javabean 的首字母小写的非限定类名来作为它的别名（其实别名是不去分大小写的）。也可在javabean 加上注解@Alias 来自定义别名， 例如： @Alias(student)
     <typeAliases>  
       <package name="com.domain"/>  
     </typeAliases>
    在MyBatis中，Mapper中的namespace用于绑定Dao接口的，即面向接口编程。
    它的好处在于当使用了namespace之后就可以不用写接口实现类，业务逻辑会直接通过这个绑定寻找到相对应的SQL语句进行对应的数据处理
    <mapper namespace="com.domain.Student">    
      <select id="selectById" parameterType="int" resultType="student">    
         select * from student where id=#{id}    
      </select>    
    </mapper>  
   */
  4）建立实体类User
package com.mybatis;
import java.io.Serializable;
public class user implements Serializable { 
	private static final long serialVersionUID = 1L; 
	private Long id;
	private String userName; 
	private String passWord; 
	public user() { 
		super();
       } 
	public user(String userName, String passWord) { 
		super(); 
		this.userName = userName; 
		this.passWord = passWord;
      } 
	public Long getId() { 
		return id;
     } 
	public void setId(Long id) { 
		this.id = id;
     } 
	public String getUserName() { 
		return userName;
     } 
	public void setUserName(String userName) { 
		this.userName = userName;
     } 
	public String getPassWord() { 
		return passWord;
    } 
	public void setPassWord(String passWord) {  
	    this.passWord = passWord;
    }
    public String toString() { 
    	return "userName " + this.userName + ", pasword " +  this.passWord;
    }
}
   5）添加映射文件
我们来说一下resultMap和resultType
resultType可以把查询结果封装到pojo类型中，但必须pojo类的属性名和查询到的数据库表的字段名一致。 
如果sql查询到的字段与pojo的属性名不一致，则需要使用resultMap将字段名和属性名对应起来，进行手动配置封装，将结果映射到pojo中
resultMap可以实现将查询结果映射为复杂类型的pojo，比如在查询结果映射对象中包括pojo和list实现一对一查询和一对多查询。 
如果数据库我们的列名为username，而实体类中我们对应的属性为userName，我们就可以使用resultMap进行桥梁对应，就像下面写的一样，注意一点映射实体类型不区分大小写
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.mybatis.usermapper">
      <resultMap id="BaseResultMap" type="com.mybatis.user" >
        <id column="id" property="id" jdbcType="BIGINT" />
        <result column="userName" property="userName" jdbcType="VARCHAR" />
        <result column="passWord" property="passWord" jdbcType="VARCHAR" />
    </resultMap>

    <!--查询所有-->
    <select id="getAll" resultType="user"> select * from user </select>

    <!--保存用户-->
   <insert id="saveUser" useGeneratedKeys="true" keyProperty="id"> insert into user(username,password)  values(#{userName},#{passWord}) </insert>

    <!--通过Id删除用户-->
    <delete id="deleteUserById"> delete from user where id =#{id} </delete>

    <!--更新该用户信息-->
    <update id="updateUser"> update user set userName = #{userName},passWord =  #{passWord}
       where id = #{id} </update>
</mapper>
    6）映射类
package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface usermapper {
    public List<user> getAll();
    public void saveUser(user user);
    public void deleteUserById(Long id);
    public void updateUser(@Param("id") Long id,  @Param("userName") String userName,
            @Param("passWord") String passWord);
}
    7）service层
package com.mybatis;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class userserviceimp {
	@Autowired
	private usermapper mapper;
	 public List<user> getAllUser()
	 {
		 return mapper.getAll();
	 }
	    public void saveUser(user user)
	    {
	    	mapper.saveUser(user);
	    }
	    public void deleteUserById(Long id)
	    {
	    	mapper.deleteUserById(id);
	    }
	    public void updateUser(Long id, String userName, String  passWord)
	    {
	    	mapper.updateUser(id, userName, passWord);
	    }
}
     8）controll层
package com.mybatis;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
@RestController
public class usercontroll {
	@Autowired
    private userservice service ;
	@RequestMapping("/find")          //localhost:8080/find
	public List<user> getAllUser()
	{
		return service.getAllUser();
	}
	 @RequestMapping("/save")       //localhost:8080/save?userName=唐小花&passWord=123456
	    public void saveUser(user user)
	    {
		    service.saveUser(user);
	    }
	@RequestMapping("/delete")     //localhost:8080/delete?id=1
	    public void deleteUserById(Long id)
	    {
		     service.deleteUserById(id);
	    }
	@RequestMapping("/update")    //localhost:8080/id=1&userName=唐花&passWord=1423556
	    public void updateUser(Long id, String userName, String  passWord)
	    {
		   service.updateUser(id, userName, passWord);
	    }
	
}
建表可以参考 :  
create table user(
    id int primary key auto_increment,
    username varchar(20),
    password varchar(20)
);

关于联级问题：
在Hibernate中，我们可以使用级联保存、删除，而iBATIS并没有提供该功能特性，此时我们需要自己保存关联的对象数据，如果有需要可以选择用spring aop
mybatis支持联级查询
       
我们先来讲讲1对1联级查询
单向1：（一个实体属性中含有另一个实体，而另一个实体的属性中没有这个实体）执行1个sql语句，多表连接查询
有两个实体user和id_card，是一对一的关系
建表参考
create table idcard(
    id int primary key auto_increment,
    code varchar(20)
)；
create table user(
    id int primary key auto_increment,
    username varchar(20),
    password varchar(20),
    cardid int references idcard(id)
);

package com.mybatis;
import java.io.Serializable;
public class user implements Serializable { 
	private static final long serialVersionUID = 1L; 
	private Long id;
	private String userName; 
	private String passWord; 
	private id_card idcard;
	public user() { 
		super();
       } 
	public user(String userName, String passWord,id_card idcard) { 
		super(); 
		this.userName = userName; 
		this.passWord = passWord;
		this.idcard=idcard;
      } 
	public user(String userName, String passWord) { 
		super(); 
		this.userName = userName; 
		this.passWord = passWord;
      } 
	public void setidcard(id_card idcard)
	{
		this.idcard=idcard;
	}
	public id_card getidcard()
	{
		return idcard;
	}
	public Long getId() { 
		return id;
     } 
	public void setId(Long id) { 
		this.id = id;
     } 
	public String getUserName() { 
		return userName;
     } 
	public void setUserName(String userName) { 
		this.userName = userName;
     } 
	public id_card getcard()
	{
		return this.idcard;
	}
	public String getPassWord() { 
		return passWord;
    } 
	public void setPassWord(String passWord) {  
	    this.passWord = passWord;
    }
    public String toString() { 
    	return "userName " + this.userName + ", pasword " +  this.passWord+"-"+this.idcard;
    }
}

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
我们需要在查询user的同时查到它相对应的id_card的信息，外键在user表中，所以我们只需要编写 usermapper就好啦

package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface usermapper {
    public List<user> getAll();
    public void saveUser(user user);
    public void deleteUserById(Long id);
    public void updateUser(@Param("id") Long id,  @Param("userName") String userName,
            @Param("passWord") String passWord);
}

相对应的mapper文件

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.mybatis.usermapper">
      <resultMap id="re1" type="com.mybatis.user" >             //type：实体类
        <id column="id" property="id" jdbcType="BIGINT" />      //id ：主键 property：实体类属性名 column：表中对应的名字
        <result column="username" property="userName" jdbcType="VARCHAR" />  
        <result column="password" property="passWord" jdbcType="VARCHAR" />
        <association property="idcard" javaType="com.mybatis.id_card">   //property：实体类中javaType类的成员名字   javaType：对应实体类
          <id property="id" column="id"></id>       //property：javaType类中 属性名，column：相对应表中的列名 
          <result property="code" column="code" jdbcType="VARCHAR"></result>
        </association>
    </resultMap>
    
    <!--查询所有-->
    <select id="getAll" resultMap="re1"> select user.*,idcard.code from user left join idcard on user.cardid=idcard.id </select>

    <!--保存用户-->
   <insert id="saveUser" useGeneratedKeys="true" keyProperty="id"> insert into user(username,password)  values(#{userName},#{passWord}) </insert>

    <!--通过Id删除用户-->
    <delete id="deleteUserById"> delete from user where id =#{id} </delete>

    <!--更新该用户信息-->
    <update id="updateUser"> update user set username = #{userName},password =  #{passWord}
       where id = #{id} </update>
</mapper>

service层
package com.mybatis;
import java.util.*;
public interface userservice {
	 public List<user> getAllUser();
	    public void saveUser(user user);
	    public void deleteUserById(Long id);
	    public void updateUser(Long id, String userName, String  passWord);
}

package com.mybatis;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class userserviceimp implements userservice{
	@Autowired
	private usermapper mapper;
	 public List<user> getAllUser()
	 {
		 return mapper.getAll();
	 }
	    public void saveUser(user user)
	    {
	    	mapper.saveUser(user);
	    }
	    public void deleteUserById(Long id)
	    {
	    	mapper.deleteUserById(id);
	    }
	    public void updateUser(Long id, String userName, String  passWord)
	    {
	    	mapper.updateUser(id, userName, passWord);
	    }
}
 
controll层
package com.mybatis;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
@RestController
public class usercontroll {
	@Autowired
    private userservice service ;
	@RequestMapping("/find")          //localhost:8080/find
	public List<user> getAllUser()
	{
		return service.getAllUser();
	}
	 @RequestMapping("/save")       //localhost:8080/save?userName=唐小花&passWord=123456
	    public void saveUser(user user)
	    {
		    service.saveUser(user);
	    }
	@RequestMapping("/delete")     //localhost:8080/delete?id=1
	    public void deleteUserById(Long id)
	    {
		     service.deleteUserById(id);
	    }
	@RequestMapping("/update")    //localhost:8080/id=1&userName=唐花&passWord=1423556
	    public void updateUser(Long id, String userName, String  passWord)
	    {
		   service.updateUser(id, userName, passWord);
	    }
	
}
       

单向2执行两个sql语句（子查询）
在之前两个实体基础上我们为id_card添加sql执行语句
mapper类
package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface id_cardmapper {
     public id_card getbyid(long id);
}

       
id_cardmapper.xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.mybatis.id_cardmapper">
    <select id="getbyid" resultType="com.mybatis.id_card"> select * from idcard where id=#{id} </select>
</mapper>


其他不变在usermapper中修改
    <resultMap id="re1" type="com.mybatis.user" >
        <id column="id" property="id" jdbcType="BIGINT" />
        <result column="username" property="userName" jdbcType="VARCHAR" />  
        <result column="password" property="passWord" jdbcType="VARCHAR" />
        <association property="idcard" column="cardid" javaType="com.mybatis.id_card" select="com.mybatis.id_cardmapper.getbyid" />
    </resultMap> 
    <!--column user表外键，property user中的idcard 
       执行起来相当于
       ==>  Preparing: select * from user 
       ==> Parameters: 
       <==    Columns: id, username, password, cardid
        <==        Row: 1, 唐小花, 123456, 1
       ====>  Preparing: select * from idcard where id=? 
       ====> Parameters: 1(Integer)
      <====    Columns: id, code
      <====        Row: 1, 啊哈
      <====      Total: 1
      <==      Total: 1      -->
    <!--查询所有法二需要id_cardmapper-->
    <select id="getAll" resultMap="re1"> select * from user </select>

一对多的情况（提供两种写法，同上，一个是执行一个sql，一个执行两个）
两个实体 orders customer，多对一的关系
/*出现问题：
MyBatis两张表字段名相同， 会导致bean属性都映射为第一个表的列
<mapper namespace="com.mybatis.customermapper">
    <resultMap type="com.mybatis.customer" id="re1">
      <id property="id" column="id" />
      <result property="name" column="name"/>
      <collection property="list" ofType="com.mybatis.order">
         <id property="id" column="id"></id>
         <result property="name" column="name"/>
      </collection>
    </resultMap>
    <select id="getAll" resultMap="re1"> select customer.*,ord.id,ord.name from customer left join ord on ord.cid=customer.id </select>
</mapper>

{"id":1,"name":"唐小花","orders":[{"id":1,"name":"唐小花"}]}
解决方法：通过设置别名的方式让其产生区别
<mapper namespace="com.mybatis.customermapper">
    <resultMap type="com.mybatis.customer" id="re1">
      <id property="id" column="id" />
      <result property="name" column="name"/>
      <collection property="list" ofType="com.mybatis.order">
         <id property="id" column="uid"></id>
         <result property="name" column="uname"/>
      </collection>
    </resultMap>
    <select id="getAll" resultMap="re1"> select customer.*,ord.id as uid,ord.name as uname from customer left join ord on ord.cid=customer.id </select>
</mapper>
{"id":1,"name":"唐小花","orders":[{"id":1,"name":"麻辣鸡腿堡"},{"id":2,"name":"麦辣鸡翅"},{"id":3,"name":"冰可乐"}]}
*/
建表：
create table customer(
    id int primary key auto_increment,
    name varchar(20)
);
create table ord(
    id int primary key auto_increment,
    name varchar(20),
    cid int references customer(id)
);
insert into customer(name) values('唐小花');
insert into ord(name,cid) values('麦辣鸡腿堡',1);
insert into ord(name,cid) values('麦辣鸡翅',1);
insert into ord(name,cid) values('冰可乐',1);
两个实体类：
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

编写mapper以及xml文件
package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface customermapper {
	public List<customer> getAll();
}

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<!-- 法一执行一个sql  
<mapper namespace="com.mybatis.customermapper">
    <resultMap type="com.mybatis.customer" id="re1">
      <id property="id" column="id" />
      <result property="name" column="name"/>
      <collection property="list" ofType="com.mybatis.order"> <!--ofType list中元素的类型 property 我们定义的Private List<order> list中的list-->
         <id property="id" column="uid"></id>
         <result property="name" column="uname"/>
      </collection>
    </resultMap>
    <select id="getAll" resultMap="re1"> select customer.*,ord.id as uid,ord.name as uname from customer left join ord on ord.cid=customer.id </select>
</mapper>
                                                                     -->
 <!-- 法二执行两个sql 需要ordermapper -->
 <mapper namespace="com.mybatis.customermapper">
    <resultMap type="com.mybatis.customer" id="re1">
      <id property="id" column="id" />
      <result property="name" column="name"/>
      <collection property="list" column="id" ofType="com.mybatis.order" select="com.mybatis.ordermapper.findbyid" /> <!--column 将customer表中的id值传给findbyid-->
    </resultMap>
    <select id="getAll" resultMap="re1"> select * from customer</select>    
<!-- ==>  Preparing: select * from customer 
     ==> Parameters: 
     <==    Columns: id, name
     <==        Row: 1, 唐小花
     ====>  Preparing: select id,name from ord where cid=? 
     ====> Parameters: 1(Integer)
     <====    Columns: id, name
     <====        Row: 1, 麻辣鸡腿堡
     <====        Row: 2, 麦辣鸡翅
     <====        Row: 3, 冰可乐
     <====      Total: 3
     <==      Total: 1 -->
</mapper>

package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ordermapper {
    public order findbyid(long id);
}

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.mybatis.ordermapper">
    <select id="findbyid" resultType="com.mybatis.order"> select id,name from ord where cid=#{id} </select>
</mapper>

省略service层直接写controll层
package com.mybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;
@RestController
public class customer_ordercontroll {
	@Autowired
	private customermapper mapper;
	@RequestMapping("/findo")
	public List<customer> get1()
	{
		return mapper.getAll();
	}
}

最后我们再来说一说多对多（只提供一种方法）
student与teacher多对多的关系
建表
create table stu(
    id int primary key auto_increment,
    name varchar(20)
) ;
create table tea(
    id int primary key auto_increment,
    name varchar(20)
);
create table stu_tea(
    sid int references stu(id),
    tid int references tea(id)
);

insert into stu(name) values ('唐小花');
insert into stu(name) values ('吴邪');
insert into stu(name) values ('张乱差');
insert into tea(name) values ('嫄姐');
insert into tea(name) values ('老邓头');
insert into st values(1,1);
insert into st values(1,2);
insert into st values(2,1);
insert into st values(3,2);

实体类：


package com.mybatis;
import java.io.Serializable;
public class student implements Serializable{
	private static final long serialVersionUID=14L;
	private long id;
	private String name;
	public student()
	{
		super();
	}
	public student(String name)
	{
		this.name=name;
	}
	public student(long id,String name)
	{
		this.id=id;
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
	public void setid(long id)
	{
		this.id=id;
	}
	public void setname(String name)
	{
		this.name=name;
	}
	public String toString()
	{
		return id+"-"+name;
	}

}

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

mapper

package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
@Mapper
public interface teachermapper {
        public List<teacher> findall(); 
}

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.mybatis.teachermapper">
    <resultMap type="com.mybatis.teacher" id="re1">
      <id property="id" column="id"/>
      <result property="name" column="name"/>
      <collection property="list" ofType="com.mybatis.student">
      <id property="id" column="uid"/>
      <result property="name" column="uname"/>
      </collection>
    </resultMap>
    <select id="findall" resultMap="re1"> select t.id,t.name,s.id as uid,s.name as uname from tea t join stu_tea on t.id=stu_tea.tid join stu s on stu_tea.sid=s.id </select>
</mapper>

controll层

package com.mybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;
@RestController
public class teacher_studentcontroll {
	@Autowired
    teachermapper mapper;
	@RequestMapping("/findall")
	public List<teacher> getteachers()
	{
		return mapper.findall();       //[{"id":2,"name":"老邓头","list":[{"id":1,"name":"唐小花"},{"id":3,"name":"张乱差"}]},{"id":1,"name":"嫄姐","list":[{"id":1,"name":"唐小花"},{"id":2,"name":"吴邪"}]}]
	}
}

以上都是单向关联（A类中包含B类，B类中不包含A类），我们最后再来讨论一下多对多的双向关联（还是teacher_student的例子） 参考：https://www.jianshu.com/p/8b4e4b22f46e
实体类：

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

mapper

package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
@Mapper
public interface teachermapper {
        public List<teacher> findall(); 
}

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.mybatis.teachermapper">
    <resultMap type="com.mybatis.teacher" id="re1">
      <id property="id" column="id"/>
      <result property="name" column="name"/>
    </resultMap>
     <resultMap type="com.mybatis.teacher" id="re2" extends="re1">
      <collection property="list" resultMap="com.mybatis.studentmapper.re1">
      </collection>
    </resultMap>
    <select id="findall" resultMap="re2"> select t.id,t.name,s.id as uid,s.name as uname from tea t join stu_tea on t.id=stu_tea.tid join stu s on stu_tea.sid=s.id </select>
</mapper>

package com.mybatis;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;
@Mapper
public interface studentmapper {
    public List<student> getalls();
}

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"  "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.mybatis.studentmapper">
    <resultMap type="com.mybatis.student" id="re1">
      <id property="id" column="uid"/>
      <result property="name" column="uname"/>
    </resultMap>
     <resultMap type="com.mybatis.student" id="re2" extends="re1">
      <collection property="list" resultMap="com.mybatis.teachermapper.re1">
      </collection>
    </resultMap>
    <select id="getalls" resultMap="re2"> select t.id,t.name,s.id as uid,s.name as uname from tea t join stu_tea on t.id=stu_tea.tid join stu s on stu_tea.sid=s.id </select>
</mapper>

controll层

package com.mybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;
@RestController
public class teacher_studentcontroll {
	@Autowired
    teachermapper mapper;
	@Autowired
	studentmapper smapper;
	@RequestMapping("/findall")
	public List<teacher> getteachers()
	{
		return mapper.findall();      //[{"id":2,"name":"老邓头","list":[{"id":1,"name":"唐小花","list":null},{"id":3,"name":"张乱差","list":null}]},{"id":1,"name":"嫄姐","list":[{"id":1,"name":"唐小花","list":null},{"id":2,"name":"吴邪","list":null}]}]s
	}
	@RequestMapping("findallt")
	public List<student> getstudents()
	{
		return smapper.getalls();   //[{"id":1,"name":"唐小花","list":[{"id":2,"name":"老邓头","list":null},{"id":1,"name":"嫄姐","list":null}]},{"id":2,"name":"吴邪","list":[{"id":1,"name":"嫄姐","list":null}]},{"id":3,"name":"张乱差","list":[{"id":2,"name":"老邓头","list":null}]}]
	}
}

基于注解的方式可以省略mapper.xml文件参考：https://blog.csdn.net/qq_41744145/article/details/99735305
@Insert:实现新增
@Update:实现更新
@Delete:实现删除
@Select:实现查询
@Result:实现结果集封装
@Results:可以与@Result 一起使用，封装多个结果集
@ResultMap:实现引用@Results 定义的封装
@One:实现一对一结果集封装
@Many:实现一对多结果集封装
@SelectProvider: 实现动态 SQL 映射
@CacheNamespace:实现注解二级缓存的使用

比如我们之前order与customer多对一的关系
public interface ordermapper {
	@Select("select id,name from ord where cid=#{id}")
    public order findbyid(long id);
}

public interface customermapper {
	@Select("select * from customer")
	@Results(id="re1",value= {
			@Result(id=true,column = "id",property = "id"),
			@Result(column = "name",property = "name"),
			// many(对多)
			@Result(column = "id",property = "list",             //传递参数customer的id
				many=@Many(
					select="com.mybatis.ordermapper.findbyid",  //获取"从表"的数据时,调用的方法来获取
					fetchType= FetchType.EAGER  //使用"EAGER",表示立即加载
				)
			)
	}
			)
	public customer getAll();
}

在文章的最后介绍一下通过Mybatis-generator插件快速实现表的增删改查等基本操作
这个插件自动生成Dao接口、实体类模型、mapping映射文件(xml) ,将生成的代码复制到项目工程中，把更多精力放在业务逻辑上
参考：https://www.cnblogs.com/QQ931697811/p/5190911.html
pom.xml

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>springboot</groupId>
  <artifactId>com.tust</artifactId>
  <packaging>war</packaging>
  <version>0.0.1-SNAPSHOT</version>
  <name>com.tust Maven Webapp</name>
  <url>http://maven.apache.org</url>
  <dependencies>
   <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
     <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
     </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version><!--$NO-MVN-MAN-VER$-->
      <scope>test</scope>
    </dependency>
     <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.0.0</version>
      </dependency>
    <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
       </dependency>  
        <dependency>
        	<groupId>org.springframework.boot</groupId>
        	<artifactId>spring-boot-starter-mail</artifactId>
        </dependency>        
<dependency>
   <groupId>org.slf4j</groupId>
   <artifactId>slf4j-log4j12</artifactId>
   <version>1.7.6</version>
</dependency>
        <dependency>
        	<groupId>ognl</groupId>
        	<artifactId>ognl</artifactId>
        	<version>3.0.8</version>
        </dependency>
  </dependencies>                        <!--以下是添加部分 com.tust是项目名-->
  <build>
    <finalName>com.tust</finalName>
    <plugins>
    	<plugin>
    		<groupId>org.mybatis.generator</groupId>
    		<artifactId>mybatis-generator-maven-plugin</artifactId>
    		<version>1.3.2</version>
    		 <!--作为DOM对象的配置,配置项因插件而异 -->
                <configuration>
                    <!--允许移动生成的文件 -->
                    <verbose>true</verbose>
                    <!-- 是否覆盖 -->
                    <overwrite>true</overwrite>
                    <!-- 自动生成的配置 -->
                    <configurationFile>src/main/resources/mybatis-generator.xml</configurationFile>
                </configuration>
    	</plugin>
    </plugins>
  </build>                                   <!--以上是添加部分-->
  <parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>2.0.1.RELEASE</version>
</parent>
</project>

resource下建立mybatis-generator.xml
<?xml version="1.0" encoding="UTF-8" ?>
      <!DOCTYPE generatorConfiguration 
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
      <generatorConfiguration>
         <!--数据库驱动路径-->
        <classPathEntry location="F:\maven\respository\mysql\mysql-connector-java\5.1.46\mysql-connector-java-5.1.46.jar" />
      <context id="DB2Tables" targetRuntime="MyBatis3">
       <commentGenerator>
         <property name="suppressAllComments" value="true"/>
       </commentGenerator>
       <jdbcConnection driverClass="com.mysql.jdbc.Driver"
             connectionURL="jdbc:mysql://localhost:3306/spring_boot"
             userId="root" password="15264365756">
         </jdbcConnection>
       <javaTypeResolver>
          <property name="forceBigDecimals" value="false"/>
       </javaTypeResolver>
       <!--域模型层,生成的目标包,项目目标源文件-->
       <javaModelGenerator targetPackage="com.transmateSchool.www.domain" targetProject="src/main/java">
           <property name="enableSubPackages" value="true"/>
           <property name="trimStrings" value="true"/>
       </javaModelGenerator>
        <!--XML映射文件,生成的位置（目标包）,源代码文件夹-->
       <sqlMapGenerator targetPackage="sqlmap" targetProject="src/main/resources">
            <property name="enableSubPackages" value="true"/>
      </sqlMapGenerator>
        <!--XML对应的Mapper类-->
       <javaClientGenerator type="XMLMAPPER" targetPackage="com.mybatis.mapper" targetProject="src/main/java">
         <property name="enableSubPackages" value="true"/>
       </javaClientGenerator>
         <!--下面是数据库表名和项目中需要生成类的名称，建议和数据库保持一致，如果有多个表，添加多个节点即可-->

       <table  tableName="stu" domainObjectName="student" enableCountByExample="false" enableSelectByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" />
       <table  tableName="tea" domainObjectName="teacher" enableCountByExample="false" enableSelectByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" />
        <table  tableName="stu_tea" domainObjectName="student_teacher" enableCountByExample="false" enableSelectByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" />
     </context>
       
 </generatorConfiguration>

右键项目run as maven build  ->在goals中输入mybatis-generator:generate
       
[INFO] Introspecting table tea
[INFO] Introspecting table stu_tea
[INFO] Generating Record class for table stu
[INFO] Generating Mapper Interface for table stu
[INFO] Generating SQL Map for table stu
[INFO] Generating Record class for table tea
[INFO] Generating Mapper Interface for table tea
[INFO] Generating SQL Map for table tea
[INFO] Generating Record class for table stu_tea
[INFO] Generating Mapper Interface for table stu_tea
[INFO] Generating SQL Map for table stu_tea
[INFO] Saving file studentMapper.xml
[INFO] Saving file teacherMapper.xml
[INFO] Saving file student_teacherMapper.xml
[INFO] Saving file student.java
[INFO] Saving file studentMapper.java
[INFO] Saving file teacher.java
[INFO] Saving file teacherMapper.java
[INFO] Saving file student_teacher.java
[INFO] Saving file student_teacherMapper.java
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.625 s
[INFO] Finished at: 2020-08-25T12:41:36+08:00
[INFO] ------------------------------------------------------------------------













































































































































































```
