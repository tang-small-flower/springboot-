
Spring Boot连接MySQL数据库,使用Hibernate进行连接MySQL数据库的操作。  
1、向pom.xml中添加需要的依赖  
>>(1)spring-boot-starter-data-jpa 
>>>>对于传统关系型数据库(二维表)，Spring Boot使用JPA（Java Persistence API）资源库来实现对数据库的操作，简单来说，JPA就是为POJO（Plain Ordinary Java Object）提供持久化的标准规范，即将Java普通对象通过对象关系映射（Object Relational Mapping，ORM）持久化到数据库中。  
>>>>(a)什么是JPA,JPA的作用是什么？  
>>>>>>JPA （Java Persistence API）Java持久化API。是一套Sun公司Java官方制定的ORM 方案,是规范，是标准 。补充：ORM（Object Relational Mapping）对象关系映射，在操作数据库之前，先把数据表与实体类关联起来，然后通过实体类的对象操作（增删改查）数据库表，因此JPA的作用就是通过对象操作数据库的，不用编写sql语句。  
>>>>(b)市场上主流的JPA框架：  
>>>>>>Hibernate （JBoos）、EclipseTop（Eclipse社区）、OpenJPA （Apache基金会）。其中Hibernate是众多实现者之中，性能最好的。
>>>>(c)数据库是用sql操作的，那用对象操作，由谁来产生SQL？   
>>>>>>JPA实现框架
>>>>(d)JPA的结构为了方便大家更深入的了解，我搜了两种方式配置Spring Data JPA的方法。（在springboot没出来前的配置，单纯说Persistence，EntityManagerFactory、EntityManager、Entity的关系）
>>>>>>（i）参考https://www.jianshu.com/p/c23c82a8fcfc
...
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
/*
 * 注意：spring-data-jpa2.x版本需要spring版本为5.x
 * 否则会报Initialization of bean failed; nested exception is java.lang.AbstractMethodError错误
 * 参考：https://stackoverflow.com/questions/47558017/error-starting-a-spring-application-initialization-of-bean-failed-nested-excep
 * 搭配方案：spring4+spring-data-jpa1.x或spring5+spring-data-jpa2.x
 */
@Configuration
// 借助spring data实现自动化的jpa repository，只需编写接口无需编写实现类
// 相当于xml配置的<jpa:repositories base-package="com.example.repository" />
// repositoryImplementationPostfix默认就是Impl
// entityManagerFactoryRef默认就是entityManagerFactory
// transactionManagerRef默认就是transactionManager
@EnableJpaRepositories(basePackages = {"com.example.repository"},
        repositoryImplementationPostfix = "Impl",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager")
@EnableTransactionManagement    // 启用事务管理器
public class SpringDataJpaConfig {

    // 配置jpa厂商适配器（参见spring实战p320）
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        // 设置数据库类型（可使用org.springframework.orm.jpa.vendor包下的Database枚举类）
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        // 设置打印sql语句
        jpaVendorAdapter.setShowSql(true);
        // 设置不生成ddl语句
        jpaVendorAdapter.setGenerateDdl(false);
        // 设置hibernate方言
        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
        return jpaVendorAdapter;
    }

    // 配置实体管理器工厂
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        // 注入数据源
        emfb.setDataSource(dataSource);
        // 注入jpa厂商适配器
        emfb.setJpaVendorAdapter(jpaVendorAdapter);
        // 设置扫描基本包
        emfb.setPackagesToScan("com.example.entity");
        return emfb;
    }

    // 配置jpa事务管理器
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        // 配置实体管理器工厂
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
 }
...
>>>>>>（ii）参考https://www.jianshu.com/p/6d38a6c561f8
>>>>>>>1、总配置文件persistence.xml存储框架需要的信息，必须放在classpath/META-INF文件夹里面 
...
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.0" xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd ">
  <persistence-unit name="mysql-jpa">
  <class>com.example.repository</class>
<properties> 
<property name="hibernate.connection.driver_class" value="com.mysql.jdbc.Driver" /> 
<property name="hibernate.connection.url" value="jdbc:mysql://localhost:3306/jpa" /> 
<property name="hibernate.connection.username" value="root" /> 
<property name="hibernate.connection.password" value="root" /> 
<!--可选配置--> 
<!--控制台打印sql语句--> 
<property name="hibernate.show_sql" value="true" /> 
<property name="hibernate.format_sql" value="true" /> 
</properties> 
  </persistence-unit>
</persistence>
...
>>>>>>>2、Persistence持久类对象来读取总配置文件，创建实体管理工厂对象 
...
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtils {

//同一个应用中，应该保证只有一个实例工厂。
public static EntityManagerFactory emf = createEntityManagerFactory();
//获得实体管理工厂
private static EntityManagerFactory createEntityManagerFactory(){
EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql-jpa");
return emf;
}

//获得实体管理类对象
public static EntityManager getEntityManger(){
EntityManager entityManager = emf.createEntityManager();
return entityManager;
 }
}
...
>>>>>>3、EntityTransaction 由 EntityManager manager = JPAUtils.getEntityManger();EntityTransaction transaction = manager.getTransaction();创建
>>>>JPA的结构通过上边两个例子大家应该可以看到，Persistence creates EntityManagerFactory,EntityManagerFactory configured by Persistence Unit,EntityManagerFactory creates EntityManager,Entitymanager creates EntityTransaction,最终EntityManager manages entity。
>>(2）mysql-connector-java
>>>>连接mysql数据库
...
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>   
...

2、application.properties添加数据库配置  
>>（1）在src/main/resource 中建立文件夹config 在config下建立applic.properties文件   
>>database名称spring_boot 时区设置serverTimezone=GMT%2B8  
>>username password 驱动   
>>hibernate.hbm2ddl.auto参数主要用于：自动创建|更新|验证数据库表结构。如果不是此方面的需求建议set value="none"  
>>>>create：每次加载hibernate时都会删除上一次的生成的表，然后根据你的model类再重新来生成新表，哪怕两次没有任何改变也要这样执行，这就是导致数据库表数据丢失的一个重要原因。  
>>>>create-drop ：每次加载hibernate时根据model类生成表，但是sessionFactory一关闭,表就自动删除。  
>>>>update：最常用的属性，第一次加载hibernate时根据model类会自动建立起表的结构（前提是先建立好数据库），以后加载hibernate时根据 model类自动更新表结构，即使表结构改变了但表中的行仍然存在不会删除以前的行。要注意的是当部署到服务器后，表结构是不会被马上建立起来的，是要等 应用第一次运行起来后才会。  
>>>>validate ：每次加载hibernate时，验证创建数据库表结构，只会和数据库中的表进行比较，不会创建新表，但是会插入新值。  
>>数据库方言 是否在控制台输出sql语句
...
spring.datasource.url=jdbc:mysql://localhost:3306/spring_boot?serverTimezone=GMT%2B8 
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.hbm2ddl.auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.show-sql= true
...

3、编写代码  
>>在项目src/main/java下建立包com.tust.sec将启动类放于这个包下，子包有entity、controll、以及dao,放置什么顾名思义。
>>（1）实体类
>>>>（a）为什么要实现Serializable接口？  
>>>>>>Serializable是java.io包中定义的、用于实现Java类的序列化操作而提供的一个语义级别的接口。Serializable序列化接口没有任何方法或者字段，只是用
于标识可序列化的语义。实现了Serializable接口的类可以被ObjectOutputStream转换为字节流，同时也可以通过ObjectInputStream再将其解析为对象。例如，我
们可以将序列化对象写入文件后，再次从文件中读取它并反序列化成对象，也就是说，可以使用表示对象及其数据的类型信息和字节在内存中重新创建对象。  
而这一点对于面向对象的编程语言来说是非常重要的，因为无论什么编程语言，其底层涉及IO操作的部分还是由操作系统其帮其完成的，而底层IO操作都是以字节
流的方式进行的，所以写操作都涉及将编程语言数据类型转换为字节流，而读操作则又涉及将字节流转化为编程语言类型的特定数据类型。而Java作为一门面向对
象的编程语言，对象作为其主要数据的类型载体，为了完成对象数据的读写操作，也就需要一种方式来让JVM知道在进行IO操作时如何将对象数据转换为字节流，以
及如何将字节流数据转换为特定的对象，而Serializable接口就承担了这样一个角色。
>>>>>>序列化是指把对象转换为字节序列的过程，我们称之为对象的序列化，就是把内存中的这些对象变成一连串的字节(bytes)描述的过程。  
>>>>>>而反序列化则相反，就是把持久化的字节文件数据恢复为对象的过程。那么什么情况下需要序列化呢?大概有这样两类比较常见的场景：  
>>>>>>>>1)、需要把内存中的对象状态数据保存到一个文件或者数据库中的时候，这个场景是比较常见的，例如我们利用mybatis框架编写持久层insert对象数据到
数据库中时;  
>>>>>>>>2)、网络通信时需要用套接字在网络中传送对象时，如我们使用RPC协议进行网络通信时;
>>>>>>这个serialVersionUID是用来辅助对象的序列化与反序列化的，建议自定义，默认的serialVersinUID对于class的细节非常敏感，反序列化时可能会导致
InvalidClassException这个异常原则上序列化后的数据当中的serialVersionUID与当前类当中的serialVersionUID一致，那么该对象才能被反序列化成功。这个
serialVersionUID的详细的工作机制是：在序列化的时候系统将serialVersionUID写入到序列化的文件中去，当反序列化的时候系统会先去检测文件中的
serialVersionUID是否跟当前的文件的serialVersionUID是否一致，如果一直则反序列化成功，否则就说明当前类跟序列化后的类发生了变化，比如是成员变量的数量或者是类型发生了变化，那么在反序列化时就会发生crash，并且回报出错误。
>>>>(b)常用注解  
>>>>>>1)@Entity声明该实体类是一个JPA标准的实体类  
>>>>>>2)@Table指定实体类关联的表，注意如果不写表名，默认使用类名对应表名。  
>>>>>>3)@Column指定实体类属性对应的表字段，如果属性和字段一致，可以不写  
>>>>>>4)@Id声明属性是一个OID，对应的一定是数据库的主键字段  
>>>>>>5)@GenerateValue声明属性（Object ID）的主键生成策略  
>>>>>>6)@SequenceGenerate使用SEQUENCE策略时，用于设置策略的参数  
>>>>>>7)@TableGenerate使用TABLE主键策略时，用于设置策略的参数  
>>>>>>8)@JoinTable关联查询时，表与表是多对多的关系时，指定多对多关联表中间表的参数。  
>>>>>>9)@JoinColumn关联查询时，表与表是一对一、一对多、多对一以及多对多的关系时，声明表关联的外键字段作为连接表的条件。必须配合关联表的注解一
起使用  
>>>>>>10)@OneToMany关联表注解，表示对应的实体和本类是一对多的关系  
>>>>>>11)@ManyToOne关联表注解，表示对应的实体和本类是多对一的关系  
>>>>>>12)@ManyToMany关联表注解，表示对应的实体和本类是多对多的关系  
>>>>>>13)@OneToOne关联表注解，表示对应的实体和本类是一对一的关系  
...
package entity
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class entity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "username")
    private String userName;
    @Column(name = "password")
    private String passWord;
    public entity() {
        super();
    }
    public entity(String userName, String passWord) {
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
}
...

>>（2）dao层  
>>>>（a）SpringDataJpa的继承结构  参考https://www.cnblogs.com/wanghj-15/p/11070844.html
>>>>>>1)SimpleJpaRepository（自动生成的dao层实现类，SpringDataJPA自动生成的，内部实现所有抽象方法）继承JpaRepository；
>>>>>>2)我们的自定义接口entityrepo继承JpaRepository，由SimpleJpaRepository实现；
>>>>>>3)JpaRepository继承PagingAndSortingRepository（分页和排序功能）
>>>>>>4)PagingAndSortingRepository继承CrudRepository（完成基本的增删查改方法）
>>>>>>5)CrudRepository继承Repository（为了统一所有Repository类型，没有任何功能）
>>>>>>6）所有的都Repository都虚继承Serializable（用于实例化实体类）
>>>>（b）注意：  
>>>>>>1)JpaRespository、PagingAndSortingRepository、CrudRepository三个接口都添加了@NoRepositoryBean注解【只要添加这个注解，Spring在扫描到有这个注解的接口的时候就不用生成实现类，就不用创建bean对象。
>>>>>>2)我们自定义的IEmployeeRepository接口没有添加这个注解，那么Spring在扫描到这个接口的时候，就会给这个接口创建对象，但是这个接口却没有实现类，那SpringDataJpa就会利用动态代理技术给这个接口自动生成一个实现类，然后进行动态编译、类加载、反射来创建对象，最后保存到Spring容器中，这样就不用自己手动写Dao层实现类也可以获得Dao层接口的对象，然后使用@Autowired进行自动注入了。  
>>>>>>3)其实SpringDataJPA内部已经写了一个实现类实现了JPARepository接口，已经实现了JpaRespository、PagingAndSortingRepository、CrudRepository这三个接口中的所有抽象方法，SpringDataJPA自动帮我们生成的实现类会自动：实现IEmployeeRepository接口，继承SimpleJapRepository类，最终我们得到（注入）的那个对象其实本质是SimpleJapRepository类的子类对象.。  
>>>>>>4) 因为SpringDataJPA自动帮我们生成的类继承了SimpleJPARepository类，并且实现了IEmployeeRepository接口，所以创建对象之后就可以调用直接JpaRespository、PagingAndSortingRepository、CrudRepository这三个接口中的所有方法了，因为SimpleJPARepository类已经帮我们将所有抽象方法都实现了。  
>>>>（c）Repository接口支持的两种查询方式：  
>>>>>>(i)基于方法名称的命名规则查询
>>>>>>>>内置在Spring Data存储库基础结构中的查询构建器机制对于在存储库实体上构建约束查询很有用。该机制条前缀find…By，read…By，query…By，count…By，和get…By从所述方法和开始解析它的其余部分,不用手动写数据库操作语句。  
>>>>>>>>>>支持的keyword以及举例(具体看官方文档https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-property-expressions
)：  
>>>>>>>>>>>>And findByLastnameAndFirstname   sql:select * from user where Lastname=? and Firstname=?   
>>>>>>>>>>>>Or findByLastnameOrFirstname     sql:select *from user where Lastname=? or Firstname=?   
>>>>>>>>>>>>Is,Equals findByFirstname,findByFirstnameIs,findByFirstnameEquals  sql:select * from user where Firstname=?   
>>>>>>>>>>>>Between findByStartDateBetween  sql:select *from user where StartDate between ? and ?  
>>>>>>>>>>>>LessThan findByAgeLessThan    sql:select *　from user where Age<?  
>>>>>>>>>>>>LessThanEqual findByAgeLessThanEqual  sql:select * from user where Age<=?  
>>>>>>>>>>>>GreaterThan findByAgeGreaterThanEqual  sql:select * from user where Age>=?  
>>>>>>>>>>>>After  findByStartDateAfter  sql:select * from user where StartDate >?  
>>>>>>>>>>>>Before findByStartDateBefore  sql:select * from user where StartDate<?  
>>>>>>>>>>>>IsNull findByAgeIsNull  sql:select * from user where age is null    
>>>>>>>>>>>>IsNotNull,NotNull findByAge(Is)NotNull  sql:select * from user where age is not null    
>>>>>>>>>>>>Like  findByFirstnameLike  sql:select * from user where Firstname like ?  
>>>>>>>>>>>>NotLike findByFirstnameNotLike  sql:select * from user where Firstname not like ?  
>>>>>>>>>>>>StartingWith findByFirstnameStartingWith  sql:select *　from user where Firstname  like '?%'  
>>>>>>>>>>>>EndingWith findByFirstnameEndingWith  sql:select *　from user where Firstname  like '%?'  
>>>>>>>>>>>>Containing findByFirstnameContaining  sql:select * from user where Firstname like '%?%'  
>>>>>>>>>>>>OrderBy findByAgeOrderByLastnameDesc  sql:select * from user where Age=? order by Lastname desc   
>>>>>>>>>>>>Not findByLastnameNot  sql:select * from user where Lastname <> ?  
>>>>>>>>>>>>In findByAgeIn(Collection<Age> ages)  sql:select * from user where age in (?)  
>>>>>>>>>>>>NotIn findByAgeNotIn(Collection<Age> ages)  sql:select * from user where age not in (?)    
>>>>>>>>>>>>True findByActiveTrue()  sql:select * from user where Active=true   
>>>>>>>>>>>>False findByActiveFalse()   sql:select * from user where Active=false 
>>>>>>>>>>>>top  findTop10ByUsername(String username, Sort sort)  findTop10ByUsername(String username, Pageable pageable) sql:select top 10 from user where  username=? 分页或是排序  
>>>>>>>>>>>>first  findFirst10ByUsername(String username, Sort sort)  
>>>>>>(ii)基于@Query注解查询:@Query 注解的使用非常简单，只需在声明的方法上面标注该注解，同时提供一个 JPQL 查询语句即可,@Query支持hql和原生sql两种方式，默认是hql，hql就是语句中用的是实体名字和实体属性，原生sql用的表名字和表字段，我只提供hql的写法。  
>>>>>>>>(1)参数使用了占位置符 ?1  代表第一个参数   ?2代表第二个  ,方法中的参数顺序不能乱
>>>>>>>>>>@Query("select e from entity e where e.id=?1")  
>>>>>>>>>>entity findbyid(long id);  
>>>>>>>>(2)@Param代替参数占位符,@Query中要填充的参数用 :name 表示，方法里的参数顺序可以打乱，按照name进行填充  
>>>>>>>>>>@Query("select e from entity e where e.id=:id and e.userName=:name")  
>>>>>>>>>>entity findbyidandusername(@Param("id") long id,@Param("name") String name);
>>>>>>>>(3)@Query不支持insert操作  
>>>>>>>>(4)使用update或者delete时候必须有@Modyfying，默认开启的事务只是可读的，更新操作加入@Modifying 就会关闭可读，在Service或者controll层方法前加@Transactional表示事务。
>>>>>>>>(5)@Query+分页排序参考：https://www.cnblogs.com/chuangqi/p/11261482.html
...
package dao;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import entity.entiry;
public interface entityrepo extends JpaRepository<entity, Long> {
    entity findByUserName(String userName);
    @Query("select e from entity e where e.id=?1")            //entity是我们定义的实体类不是表名，包括id也是实体类里的属性
    entity findbyid(long id);
    @Query("select e from entity e where e.userName like %?1%")
    List<entity> queryLike1(String name);
    @Query("select e from entity e where e.id=:id and e.userName=:name")
    entity findbyidandusername(@Param("id") long id,@Param("name") String name);
    @Modifying
    @Query("update entity e set e.passWord = ?1 where e.userName = ?2")
    int setfirstname(String firstname, String lastname);
    @Modifying
    @Query("delete from entity e where e.id = :id")
    void delete(@Param("id") long id);
}
...

>>(3)controll层（原谅我偷个小懒省略service层）
...
package controll;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;
import dao.entityrepo;
@RestController
@RequestMapping("user")
public class entitycontroll {
	   @Autowired
	   private entityrepo entityr;
	   @RequestMapping("/getAllUser")
	   @ResponseBody
	   public List<entity> findAll() {
	       List<entity> list = new ArrayList<entity>();
	       list = entityr.findAll();
	       return list;
	   }

	   @RequestMapping("/getByUserName")
	   @ResponseBody
	   public entity getByUserName(String userName) {
	       entity e = entityr.findByUserName(userName);
	       return e;
	   }
	   @RequestMapping("/getbyid")
	   @ResponseBody
	   public entity getbyid(long id)
	   {
		   entity e=entityr.findbyid(id);
		   return e;
	   }
	   @RequestMapping("/querylike1")
	   @ResponseBody
	   public List<entity> queryLike1(String name)
	   {
		   List<entity> e=entityr.queryLike1(name);
		   return e;
	   }
	   @RequestMapping("/findbyidandusername")
	   @ResponseBody
	   public entity findbyidandusername(long id, String name)
	   {
		   entity e=entityr.findbyidandusername(id, name);
		   return e;
	   }
	   @RequestMapping("/setfirstname")
	   @ResponseBody
	   @Transactional
	   public int setfirstname(String firstname, String lastname) {
		   int e=entityr.setfirstname(firstname, lastname);
		   return e;
	   }
	   @RequestMapping("/delete")
	   @ResponseBody
	   @Transactional
	   public void delete(long id)
	   {
		   entityr.delete(id);
	   }
	    public String hi(){
	        return "hello";
	    }
}
...

>>(4)com.tust.sec包内写启动类  
...
package com.tust.sec;  
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication   
public class firstblood {
	public static void main(String[] args) {
		  SpringApplication.run(firstblood.class, args);
	}
}
...


```python
4、开启mysql服务+建表+填充数据  
>>(1)电脑开始的搜索框中输入cmd调出终端（如果没开启mysql服务先从  服务里开启服务）  
>>(2)终端中输入mysql -hlocalhost -uroot -p  然后输入密码  
>>(3)我们配置的database是spring_boot 所以在终端中输入  create database spring_boot;  回车  
>>(4)输入 use spring_boot; 回车  
>>(5)我们在@table里配置的表名是user,列有id(自增),username,password @column中定义的，按照他建表
...
create table user(
    id int not null auto_increment primary key,
    username varchar(20),
    password varchar(20)
);
...
>>(6)插入2条数据
...
insert into user values(1,'唐晓花',123456);
insert into user values(2,'唐花',1234456);
...
>>(7)检验一下  
...
select *　from user;
...
```


```python
5、验证  右键启动类run as java application
http://localhost:8080//user/getAllUser  
http://localhost:8080/user/getByUserName?userName=唐晓花  
http://localhost:8080/user/getbyid?id=1  
http://localhost:8080/user/delete?id=2  
等等  
```
