
1、打开Eclipse新建一个maven项目,选择默认存储位置->选择最后一项maven-archetype-webapp,填写自定义Artifact Id。  
 >> 有三个重要的文件，应用程序代码位于src/main/java，资源在src/main/resources目录里，测试代码在src/test/java里  
 >>如果我们缺少src/main/java 和src/test/java 文件 右键项目->properties->Java Build Path ->Source->add folder    
 >>如果添加报错那就把 excluded 中的东西全都移除再试一下add folder  
  
2、修改pom.xml文件  
>>（1）引入起步依赖spring-boot-starter-web  
>>>>（i）什么是起步依赖，起步依赖有什么用？  
>>>>>>起步依赖的核心就是对pom文件的配置优化，Spring Boot版本升级的过程也是一个pom配置不断优化的过程，通俗来讲就是把一套工具组装在一起构造了一个简单的模板，你可以添加新的配置也可以更换配置，极大减少了开发过程中一些问题。  
>>>>>>举个例子来说，如果不用Spring Boot，你会向项目里添加哪些依赖呢？要用spring mvc的话你需要哪些Spring依赖呢?你还记得Thymeleaf的Group和Artifact ID吗？你应该用哪个版本的Spring Data JPA呢？他们放在一起兼容吗？  
>>>>>>Spring Boot通过众多起步依赖降低了项目依赖的复杂度，本质上是一个maven项目对象模型POM，定义了对其他库的传递依赖，这些东西加起来支持某项功能，举例来说你想实现一个web应用程序，与其向项目文件中添加一堆单独的库依赖，不如声明这是一个Web应用程序，添加spring-boot-starter-web。那我们具体来看看spring-boot-starter-web到底传递了什么依赖，双击打来spring-boot-starter-web的pom.xml，我们发现他的provides: spring-webmvc,spring-web,jackson-databind，比起手动添加单独的库，这样快速又不怕不兼容的依赖的添加方法谁不爱呢？  
>>>>>>一般以spring-boot-starter-* 作为命名前缀，如果想以Thymeleaf为Web视图，JPA来实现数据持久化，那么我们可以直接添加spring-boot-starter-thymeleaf和spring-boot-starter-jpa,为了方便测试，我们还需要Spring Boot上下文运行集成测试的库，因此需要添加spring-boot-starter-test起步依赖，上面所说的四个起步依赖就等价于一大把独立的库，这些传递依赖涵盖了Spring MVC，Spring Data JPA，Thymeleaf等内容  

>>>>（ii）遇到不需要的依赖如何剔除掉给项目瘦身呢？    
>>>>>>如果开发过程中有这样一种情况：Spring Boot的Web起步传递依赖Jackson JSON 库，如果构建传统面向用户的Web程序用不到Jackson时，引入它不会有什么坏处，但排除它的传递依赖可以为你的项目瘦身。就可以在pom.xml里添加spring-boot-starter-web时添加exclusion（排除在外）。  
```
         <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
           <exclusions>
               <exclusion><groupId>com.fasterxml.jackson.core</groupId></exclusion>
           </exclusions>
         </dependency>
```
>>>>（iii）如何替换起步依赖传递依赖的版本号？  
>>>>>>可以在pom.xml文件中直接引入，Maven总会使用最近的依赖，就是说你在pom.xml文件中添加的依赖会覆盖传递依赖所引入的依赖，比如想覆盖Web起步依赖中Jackson2.3.4为Jackson2.4.3,就可以在pom.xml中添加：  
```
          <dependency>
           <groupId>com.fasterxml.jackson.core</groupId>
           <artifactId>jackson-databind</artifactId>
           <version>2.4.3</version>
         </dependency>
```  
>>(2）如果想使用起步依赖，那么必须在pom.xml文件中添加如下依赖，标记该项目为Spring Boot项目，Maven的用户可以通过继承spring-boot-starter-parent项目来获得一些合理的默认配置。这个parent提供了以下特性：  
>>>>1、默认使用Java 8  
>>>>2、使用UTF-8编码  
>>>>3、一个引用管理的功能，在dependencies里的部分配置可以不用填写version信息，这些version信息会从spring-boot-dependencies里得到继承。  
>>>>4、识别过来资源过滤（Sensible resource filtering.）  
>>>>5、识别插件的配置（Sensible plugin configuration (exec plugin, surefire, Git commit ID, shade).）  
>>>>6、能够识别application.properties和application.yml类型的文件，同时也能支持profile-specific类型的文件（如： application-foo.properties and application-foo.yml，这个功能可以更好的配置不同生产环境下的配置文件)。  
>>>>7、maven把默认的占位符${…}改为了@..@

```
        <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.4.3.RELEASE</version>
        </parent>
```

```
//pom.xml里添加的依赖，我们就简单写一个网页，不需要太多起步依赖
<dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
     <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
</dependency>
<parent>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-parent</artifactId>
<version>1.4.3.RELEASE</version>
</parent>



//我的pom.xml文件全部配置
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
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <build>
    <finalName>com.tust</finalName>
  </build>
  <parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>1.4.3.RELEASE</version>
</parent>
</project>
```
>>3、编写代码  
>>（1）我们需要一个启动类（项目入口）这个类必须包含main函数，执行SpringApplication.run(启动类.class)静态方法，有三种写法  


```
    （i）在src/main/java里新建一个包com.tust.fir,在包里新建类firstblood
         package com.tust.fir;
         import org.springframework.boot.SpringApplication;
         import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
         import org.springframework.web.bind.annotation.RequestMapping;
         import org.springframework.web.bind.annotation.RestController;
         @RestController   //@RestController =@Controller +@ResponseBody(需要返回JSON，XML或自定义mediaType内容到页面) 
         @EnableAutoConfiguration 
        //@EnableAutoConfiguration可以帮助SpringBoot应用将所有符合条件的@Configuration配置都加载到当前SpringBoot创建并使用的IoC容器。
         public class firstblood {
             @RequestMapping("/")           //路由
             String home() {
               return "Hello World!";
             }
             public static void main(String[] args) {
                 SpringApplication.run(firstblood.class, args);
            }
         }
        //右键run as java application (项目内嵌tomcat) 打开浏览器，访问 http://localhost:8080/
```


```
    （ii）(a)在src/main/java里新建一个包com.tust.fir,在包里新建类firstblood
          import org.springframework.boot.SpringApplication;
          import org.springframework.boot.autoconfigure.SpringBootApplication;
          @SpringBootApplication   
        //这里主要关注@SpringBootApplication注解，它包括三个注解：
        //@Configuration：表示将该类作用springboot配置文件类。
        //@EnableAutoConfiguration:表示程序启动时，自动加载springboot默认的配置。
        //@ComponentScan:表示程序启动是，自动扫描当前包及子包下所有类。
          public class firstblood {
          public static void main(String[] args) {
          SpringApplication.run(firstblood.class, args);
               }
          }
           
            
            
        (b)在包里建立类hello//表现层注意写与启动类在同包或者子包下
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;
        @RestController
        public class hello {
        @RequestMapping("/")
        public String hi(){
           return "hi";
            }
         }
          
   //右键run as java application (项目内嵌tomcat) 打开浏览器，访问 http://localhost:8080/
```


```
  (iii)   (a)在src/main/java里新建一个包com.tust.fir,在包里新建类firstblood
         package com.tust.fir;
         import org.springframework.boot.SpringApplication;
         import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
         import org.springframework.context.annotation.ComponentScan;
         @ComponentScan       //@ComponentScan(basePackages = "xxx") 扫描包内的类
         @EnableAutoConfiguration    //表示程序启动时，自动加载springboot默认的配置
         public class firstblood {
             public static void main(String[] args) {
		       SpringApplication.run(firstblood.class, args);
	                 }
         }

           
            
            
        (b)在包里或者包外（需要将上边@ComponentScan中加入搜索的包）建立类hello
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;
        @RestController
        public class hello {
        @RequestMapping("/")
        public String hi(){
           return "hi";
            }
         }
       //右键run as java application (项目内嵌tomcat) 打开浏览器，访问 http://localhost:8080/
```



>>4、常见异常处理  
>>（i）找不到或无法加载主类  

>>>>project->clean  刷新重新run  
>>>>project clean是IDE对已经生成的class文件的删除操作，体现在文件系统上是只删除了Target目录中classes文件夹中所有内容！  
>>>>（也可以说是删除了IDE自动编译的所有内容）  

>>（ii）控制台报错：Address already in use: bind 即8080端口占用  
>>>>打开终端输入 netstat -ano|findstr "8080"查找进行ip 显示最后的一串数字  
>>>>taskkill /pid 进程号 -f  
