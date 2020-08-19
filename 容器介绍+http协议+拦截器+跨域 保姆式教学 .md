

```python
/*又是递归式学习的一天
1、web 容器、spring 容器、servlet容器、spring mvc 容器的关系？https://www.cnblogs.com/jieerma666/p/10805966.html 
    servlet对象    https://www.cnblogs.com/best/p/9295216.html
    servlet容器组件 
    servlet和spring mvc对比   https://www.cnblogs.com/haolnu/p/7294533.html
    各个容器的创建过程
2、servlet和tomcat的关系
   tomcat请求过程
3、参数方法中的HttpServletRequest对象、HttpServletResponse对象是由谁创建的？  http://www.360doc.com/content/10/0713/20/495229_38798294.shtml
4、http协议  请求报文 与  响应报文
5、springboot配置拦截器
6、跨域
*/
```


```python
1、web 容器、spring 容器、servlet容器、spring mvc 容器的关系  参考：https://www.cnblogs.com/jieerma666/p/10805966.html   
   web容器的作用：管理servlet（通过servlet容器），以及监听器(Listener)和过滤器(Filter)
   Servlet容器的作用：管理servlet对象 负责处理客户请求，当客户请求来到时，Servlet容器获取请求，然后调用某个Servlet，并把Servlet的执行结果返回给客户。每个Servlet即对应每个开发的spring mvc服务程序
   Spring容器的作用：管理service和dao
   SpringMVC容器的作用： 管理controller对象


   什么是Servlet对象？简单来说就是我们编写的一个个业务逻辑
   Servlet对象的生命周期：
     1）调用init方法初始化Servlet。init方法：当服务器创建一个serlvet的时候，会去调用init方法。当我们第一次去访问一个servlet的时候，会去创建这个servlet对象。并且只会创建一次。如果配置了load-on-startup 表示服务器启动的时候就创建servlet实例。
     2）调用Servlet中的service方法，处理请求操作。service方法：客户端每一次请求，tomcat都会去调用servcie方法。处理用户的请求。并且给其响应。每一次请求都会调用servcie方法。
     3）调用destroy方法执行销毁。destroy 方法：当服务器销毁一个servlet的时候，会调用里面的destory方法。当我们的web服务器，正常关闭的时候，会去调用destroy方法。否则不会调用destroy的方法。
  如果加入WEB服务器的过程那么：https://www.cnblogs.com/best/p/9295216.html
     ①Web服务器首先检查是否已经装载并创建了该Servlet的实例对象。如果是，则直接执行第④步，否则，执行第②步。
     ②装载并创建该Servlet的一个实例对象。 
     ③调用Servlet实例对象的init()方法。
     ④创建一个用于封装HTTP请求消息的HttpServletRequest对象和一个代表HTTP响应消息的HttpServletResponse对象，然后调用Servlet的service()方法并将请求和响应对象作为参数传递进去。
     ⑤WEB应用程序被停止或重新启动之前，Servlet引擎将卸载Servlet，并在卸载之前调用Servlet的destroy()方法。 

  Servlet容器中的组件最常用的有这么几类：
     1）请求对象ServletRequest和HttpServletRequest 其中HttpServletRequest extends ServletRequest 由Servlet容器产生，作为参数传递给Servlet实例化后的service方法
     2）响应对象ServletResponse和HttpServletResponse 其中HttpServletResponse extends ServletResponse 由Servlet容器产生对象，具有访问HTTP标头和cookie的方法
     3）Servlet配置对象，ServletConfig，Servlet通过该对象过去初始化信息以及ServletContext对象
     4）ServletContext对象：通过他来访问当前web提供的各种资源
  主要由两个java包组成，javax.servlet和javax.servlet.http前者定义了servlet接口以及相关的通用接口和类，后者定义了与HTTP协议有关的，其中Servlet接口最为核心，所有的servlet必须实现这一个接口，定义了init，service，destroy，getServletConfig（返回一个ServletConfig对象，所有的servlet）,getServletInfo（返回字符串，servlet创建者，版本信息等）
  抽象类GenericServlet，实现了Servlet接口，ServletConfig接口，Serializable接口，这样所有的Servlet就可以通过ServletConfig接口拥有获得自己相关属性的方法，getInitParameter，getInitParameterNames，另外getServletContext也是实现Config接口需要的，ServletConfig 初始化一个servlet对象时，创建一个ServletConfig对象，包含了这个Servlet初始化的参数信息，一个Servlet的ServletConfig不能被另一个servlet访问，我们通过ServletConfig对象就可以得到当前servlet的初始化参数信息。https://blog.csdn.net/durenniu/article/details/81066817
<servlet>
  <servlet-name>...</servlet-name>
  <servlet-class>...</servlet-class>
  <init-param>
    <param-name>...</param-name>
    <param-value>...</param-value>
  </init-param>
</servlet>
<servlet-mapping></servlet-mapping>
  HttpServlet抽象类 HttpServlet extends GenericServlet implements Serializable,它分别实现了对应方法doGet（由服务器调用，允许servlet处理get请求--通过service方法），doPost，doDelete，从源码上看他实现了service方法，并且重载了方法。
  选取源码的一部分作为展示：https://tool.oschina.net/apidocs/apidoc?api=javaEE6
private static final String METHOD_GET="GET";
protected void service(HttpServletRequest req,HttpServletResponse res) throws ServletExcepion,IOEception{
    String method=req.getMethod();
    if(method.equals(METHOD_GET))
    {
        long lastModified =getLastModified(req);
        if(lastModified==-1)
        {
            //servlet doesn't support if-modified since,no reason
            doGet(req,resp);
        }
    }
}
  ServletRequest与HttpServletRequest HttpServletRequest extends ServletRequest ,且HttpServletRequest比ServletRequest多了一些针对于Http协议的方法。如getHeader (String name)， getMethod () ，getSession () 等等。
  ServletResponse与HttpServletResponse HttpServletResponse extends ServletResponse,且HttpServletResponse添加了设置响应头，addCookie，addHeader的方法
  ServletContext是Servlet和Servlet容器的接口，容器启动一个Web应用时都会创建唯一一个ServletCOntext对象，管理访问容器的各种资源，这个对象是全局唯一的，工程内部所有的servlet都共享这个对象，服务器启动的时候创建，服务器关闭的时候销毁，因为这是全局应用程序对象，全局共享对象。
    1)ServletContext是一个域对象，用于在不同动态资源（servlet）之间传递与共享数据。setAttribute（name，value） getAttribute（name） removeAttribute
    2)读取全局参数和新方法getServletContext().getInitParameter(name)//通过参数名获取参数值     getServletContext().getInitParameterNames();//获取所有参数名称列表
    3)搜索当前工程目录下资源文件 getServletContext().getRealPath(path)根据相对路径获取服务器上资源的绝对路径  getServletContext().getResourceAsStream(path),根据相对路径获取服务器上资源的输入字节流
    4)可以获取当前工程名字    getServletContext().getContextPath()；

  Servlet对象的运行原理：
  我们定义一个Servlet
    
package com.tust.hello;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
public class Hello implements Servlet{
@Override
public void destroy() {
// TODO Auto-generated method stub
    System.out.println("Servlet销毁了！");
}
@Override
public ServletConfig getServletConfig() {
// TODO Auto-generated method stub
      return null;
}
@Override
public String getServletInfo() {
// TODO Auto-generated method stub
      return null;
}
@Override
public void init(ServletConfig arg0) throws ServletException {
// TODO Auto-generated method stub
      System.out.println("ServerConfig 初始化了");
}
@Override
public void service(ServletRequest arg0, ServletResponse arg1)
throws ServletException, IOException {
// TODO Auto-generated method stub
      System.out.println("hello servlet service方法被调用");
}
}

  并在web.xml中配置servlet的访问路径
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" id="WebApp_ID" version="2.5">
  <display-name>tust</display-name>
  <servlet>
    <servlet-name>Hello</servlet-name>
    <servlet-class>com.tust.hello</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>Hello</servlet-name>
    <url-pattern>/hello</url-pattern>
  </servlet-mapping>
  <welcome-file-list>
  <welcome-file>index.html</welcome-file>
  <welcome-file>index.htm</welcome-file>
  <welcome-file>index.jsp</welcome-file>
  <welcome-file>default.html</welcome-file>
  <welcome-file>default.htm</welcome-file>
  <welcome-file>default.jsp</welcome-file>
  </welcome-file-list>
</web-app>
  假如我们访问http://localhost:8080/tust/hello，会发生什么呢？  https://blog.csdn.net/weixin_40396459/article/details/81706543
    1）首先客户端通过ip localhost找到对应的服务器
    2）通过访问的端口8080找到对应的程序
    3）通过访问相应的地址访问项目名tust访问程序
    4）通过访问字符串/hello，找到对应的Servlet名字Hello，再找到对应的实现类com.tust.Hello
  原来，我们得自己去解析HTTP请求，然后做出回应，现在由Servlet代劳，封装转化为HttpServletRequest对象，servlet只需要getxxx方法就可以获取请求信息。

  看到这段代码会不会觉得眼熟呢？
<servlet>  
        <servlet-name>spmvc</servlet-name> 
         <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
          <servlet-name>spmvc</servlet-name>
          <url-pattern>*.do</url-pattern>
</servlet-mapping>
  这是在spring项目中我们经常使用的一段配置，上面这段xml的意思就是，所有的以.do结尾的action都让org.springframework.web.servlet.DispatcherServlet这个类来处理。load-on-startup元素标记容器是否在启动的时候就加载这个servlet(实例化并调用其init()方法)。这时servlet不用初始化，而是交给容器去处理。
  Spring主要也是通过DispatcherServlet实现了Servlet这个接口，又叫前端控制器，来自前端的请求会先到达这里，它负责到后台去匹配合适的handler。DispatcherServlet的主要工作流程如下：
    1）前端请求到达DispatcherServlet。
    2）前端控制器请求HandlerMappering 查找Handler。
    3）如果查找到存在的处理器，进一步去调用service和dao层
    4）返回结果再到controller层，渲染具体的视图，返回结果给页面。
  spring框架做了什么？我们通过@RequestMapping(url)代替了<servlet-mapping>中的<url-pattern>设置路由,@Controller代替了<servlet-class> ，我们不需要再手写servlet，不用在xml里费时费力，开发效率大大提升了

  各容器的创建过程：
    1）TOMCAT启动，Servlet容器随即启动，然后读取server.xml配置文件，启动里面配置的web应用，为每个应用创建一个“全局上下文环境”（ServletContext）；
    2）创建Spring容器实例。调用web.xml中配置的ContextLoaderListener，初始化WebApplicationContext上下文环境（即IOC容器），加载context­param指定的配置文件信息到IOC容器中。WebApplicationContext在ServletContext中以键值对的形式保存。
    3）创建SpringMVC容器实例。调用web.xml中配置的servlet-class，为其初始化自己的上下文信息，并加载其设置的配置信息到该上下文中。将WebApplicationContext设置为它的父容器。
    4）此后的所有servlet的初始化都按照3步中方式创建，初始化自己的上下文环境，将WebApplicationContext设置为自己的父上下文环境。当Spring在执行ApplicationContext的getBean时，如果在自己context中找不到对应的bean，则会在父ApplicationContext中去找。


2、servlet和tomcat的关系 参考：https://blog.csdn.net/baidu_36583119/article/details/79642407   https://blog.csdn.net/u013212754/article/details/102831895  https://www.cnblogs.com/wjlwo2ni/p/10825835.html  https://www.cnblogs.com/wansw/p/10228973.html http://www.360doc.com/content/17/0721/17/41344223_673115790.shtml
  由1我们知道，Tomcat是一个比较流行的Web服务器，具有处理HTML的能力，同时他还是一个Servlet容器

  Tomcat的内部基本容器构成：https://blog.csdn.net/u013212754/article/details/102831895
  Tomcat最顶层的容器室Server， 代表整个Tomcat服务器，一个Server可以包含一个或者多个Service。一个Service包含一个或者多个Connetor，但是只能包含一个Container。Connector主要用来处理连接相关的事，比如网络套接字Socket的监听、请求request的接收和应答response的发送。这里的Container是一个抽象的概念，一个接口。其实是指一个Service只能包含一个Engine–Engine继承自Container。
  Connector处理请求与内部结构： https://blog.csdn.net/u013212754/article/details/103344686 容器配置和请求监听和请求到达后在Connector中的流转，直到请求进入Container的处理边界 
  一个Connector中有一个ProtocolHandler，不同ProtocolHandler接口实现代表不同的协议，比如protocol="HTTP/1.1"或者protocol=“org.apache.coyote.http11.Http11NioProtocol”。默认实现的协议主要有两种，Ajp或者HTTP。HTTP协议不用说，Ajp是Apache JServ Protocol的缩写，主要用于Apache前端服务器的通讯，特点是长连接，不需要每次断开连接再建立连接，开销比较小
  ProtocolHandler会包含Endpoint、Processor和Adapter的实现。Endpoint主要用来管理Socket，监听TCP的请求和发送应答。Processor用来实现HTTP协议。Adapter，顾名思义，是作为适配器将请求适配到Container容器。
  container只有四种分别是Engine，Host，COntext和Wrapper
  Engine：Host表示一个虚拟主机，一个引擎可以包含多个Host。用户通常不需要创建自定义的Host，因为Tomcat给出的Host接口的实现（类StandardHost）提供了重要的附加功能。
  Context：一个Context表示了一个Web应用程序，运行在特定的虚拟主机中。什么是Web应用程序呢？在Sun公司发布的Java Servlet规范中，对Web应用程序做出了如下的定义：“一个Web应用程序是由一组Servlet、HTML页面、类，以及其他的资源组成的运行在 Web服务器上的完整的应用程序。它可以在多个供应商提供的实现了Servlet规范的Web容器中运行”。一个Host可以包含多个Context（代 表Web应用程序），每一个Context都有一个唯一的路径。用户通常不需要创建自定义的Context，因为Tomcat给出的Context接口的 实现（类StandardContext）提供了重要的附加功能。
  Wrapper，每个wrapper只有一个Servlet，而每个Servlet即对应每个开发的spring mvc服务程序。所以我们开发的spring mvc服务程序都是通过Wrapper加载到tomcat中。Context和Wrapper是动态添加的，我们在tomcat的指定目录下每添加一个war包，tomcat加载war包时，就可以添加Context和Servlet。

  tomcat请求过程：https://www.cnblogs.com/hggen/p/6264475.html
  tomcat容器动态加载过程：https://blog.csdn.net/u013212754/article/details/103203836
  tomcat容器生命周期标准：
    tomcat的容器包括Server、service，Connector，Engine，Host，Context，Wrapper，它们都继承同一个管理生命周期的接口Lifecycle，    
    这个接口类定义了这些容器的初始化、启动、停止和销毁的标准生命周期方法，定义了这些容器所处生命周期的状态事件名和状态事件监听器的管理。
    Lifecycle接口类的通用实现是在LifecycleBase类中，容器都继承自LifeCycleBase类，LifecycleBase类为容器提供了基本生命周期方法的一般实现和生命周期状态监听器的管理实现。

  Tomcat服务器接受客户请求并做出响应的过程如下：
    1）客户端（通常都是浏览器）访问Web服务器，发送HTTP请求。
    2）Web服务器接收到请求后，传递给Servlet容器。
    3）Servlet容器加载Servlet，产生Servlet实例后，向其传递表示请求和响应的对象。
    4）Servlet实例使用请求对象得到客户端的请求信息，然后进行相应的处理。
    5）Servlet实例将处理结果通过响应对象发送回客户端，容器负责确保响应正确送出，同时将控制返回给Web服务器。
 
  关于Tomcat具体内容后续会补上，内容太多太杂递归层数太深了，啊真的太难了

3、参数方法中的HttpServletRequest对象、HttpServletResponse对象是由谁创建的  参考：https://blog.csdn.net/QinqinTaylor/article/details/85709698
   其实这个问题在问题一当中我们已经解决了，是sevlet容器在创建一个servlet实例化后，创建一个用于封装HTTP请求消息的HttpServletRequest对象和一个代表HTTP响应消息的HttpServletResponse对象，并把它作为参数代入servlet的service方法中的   http://www.360doc.com/content/10/0713/20/495229_38798294.shtml
4、http协议  请求报文 与  响应报文
  什么是http协议：　HTTP是一个属于应用层的面向对象的协议，由于其简捷、快速的方式，适用于分布式超媒体信息系统。它于1990年提出，经过几年的使用与发展，得到不断地完善和扩展。目前在WWW中使用的是HTTP/1.0的第六版，HTTP/1.1的规范化工作正在进行之中，而且HTTP-NG(Next Generation of HTTP)的建议已经提出。
  http协议的主要特点：
    1）支持客户/服务器模式。 
    2）简单快速：客户向服务器请求服务时，只需传送请求方法和路径。请求方法常用的有GET、HEAD、POST。每种方法规定了客户与服务器联系的类型不同。由于HTTP协议简单，使得HTTP服务器的程序规模小，因而通信速度很快。
    3）灵活：HTTP允许传输任意类型的数据对象。正在传输的类型由Content-Type加以标记。
    4）无连接：无连接的含义是限制每次连接只处理一个请求。服务器处理完客户的请求，并收到客户的应答后，即断开连接。采用这种方式可以节省传输时间。
    5）无状态：HTTP协议是无状态协议。无状态是指协议对于事务处理没有记忆能力。缺少状态意味着如果后续处理需要前面的信息，则它必须重传，这样可能导致每次连接传送的数据量增大。另一方面，在服务器不需要先前信息时它的应答就较快。
  三次握手四次挥手：https://baijiahao.baidu.com/s?id=1654225744653405133&wfr=spider&for=pc
  http协议请求和响应流程：
    1）用户点击www.baidu.com/index.html
    2)服务器分析URL
    3）浏览器向DNS（将域名转化为IP地址）请求解析www.baidu.com获得IP地址返回浏览器
    4）浏览器与服务器建立TCP连接
    5）浏览器请求文档GET /index.html
    6）服务器给出响应，将文档发送给浏览器
    7）释放TCP连接
    8）浏览器显示index.html内容

  HTTP请求报文：一个HTTP请求报文由请求行（request line）、请求头部（header）、空行和请求数据4个部分组成，下图给出了请求报文的一般格式。 、
     请求方法 空格 URL 空格 协议版本 回车符 换行符  这是请求行 例如  GET /index.html HTTP/1.1 
     头部字段名:值 回车符 换行符  这是请求头
          典型的请求头有：
              User-Agent：产生请求的浏览器类型。
              Accept：客户端可识别的内容类型列表。
              Host：请求的主机名，允许多个域名同处一个IP地址，即虚拟主机
     回车符 换行符
    请求数据
        请求数据不在GET方法中使用，而是在POST方法中使用。POST方法适用于需要客户填写表单的场合。与请求数据相关的最常使用的请求头是Content-Type和Content-Length。

  HTTP响应报文也由三个部分组成，分别是：状态行、消息报头、响应正文
    常见状态代码、状态描述的说明如下。
      200 OK：客户端请求成功。
      400 Bad Request：客户端请求有语法错误，不能被服务器所理解。
      401 Unauthorized：请求未经授权，这个状态代码必须和WWW-Authenticate报头域一起使用。
      403 Forbidden：服务器收到请求，但是拒绝提供服务。
      404 Not Found：请求资源不存在，举个例子：输入了错误的URL。
      500 Internal Server Error：服务器发生不可预期的错误。
      503 Server Unavailable：服务器当前不能处理客户端的请求，一段时间后可能恢复正常，举个例子：HTTP/1.1 200 OK（CRLF）。
  一个响应报文的例子：
   HTTP/1.1 200 OK
   Date: Sat, 12 Aug 2020 23:59:59 GMT
   Content-Type: text/html;charset=ISO-8859-1
   Content-Length: 122
    
  ＜html＞ 
  ＜head＞ 
  ＜title＞Wrox Homepage＜/title＞
  ＜/head＞
  ＜body＞
  ＜!-- body goes here --＞
  ＜/body＞
  ＜/html＞
  http请求GET和POST的区别：
    1)GET提交，请求的数据会附在URL之后（就是把数据放置在HTTP协议头＜request-line＞中），以?分割URL和传输数据，多个参数用&连接;例如：login.action?name=hyddd&password=idontknow&verify=%E4%BD%A0 %E5%A5%BD。如果数据是英文字母/数字，原样发送，如果是空格，转换为+，如果是中文/其他字符，则直接把字符串用BASE64加密，得出如： %E4%BD%A0%E5%A5%BD，其中％XX中的XX为该符号以16进制表示的ASCII。
      POST提交：把提交的数据放置在是HTTP包的包体＜request-body＞中。上文示例中红色字体标明的就是实际的传输数据
      因此，GET提交的数据会在地址栏中显示出来，而POST提交，地址栏不会改变
    2)传输数据的大小：
      首先声明,HTTP协议没有对传输的数据大小进行限制，HTTP协议规范也没有对URL长度进行限制。 而在实际开发中存在的限制主要有：
      GET:特定浏览器和服务器对URL长度有限制，例如IE对URL长度的限制是2083字节(2K+35)。对于其他浏览器，如Netscape、FireFox等，理论上没有长度限制，其限制取决于操作系统的支持。
      因此对于GET提交时，传输数据就会受到URL长度的限制。
      POST:由于不是通过URL传值，理论上数据不受限。但实际各个WEB服务器会规定对post提交数据大小进行限制，Apache、IIS6都有各自的配置。
    3)安全性：
      POST的安全性要比GET的安全性高。注意：这里所说的安全性和上面GET提到的“安全”不是同个概念。上面“安全”的含义仅仅是不作数据修改，而这里安全的含义是真正的Security的含义，比如：通过GET提交数据，用户名和密码将明文出现在URL上，因为(1)登录页面有可能被浏览器缓存， (2)其他人查看浏览器的历史纪录，那么别人就可以拿到你的账号和密码了，

5、Spring Boot配置拦截器
  1）自定义拦截器，实现HandlerInterceptor这个接口。这个接口包括三个方法，preHandle是请求执行前执行的，postHandler是请求结束执行的，但只有preHandle方法返回true的时候才会执行，afterCompletion是视图渲染完成后才执行，同样需要preHandle返回true，该方法通常用于清理资源等工作。
  2）注册拦截器， 与三种方法，作用是确定拦截器和拦截的URL
    i.需要继承WebMvcConfigureSupport并重写addInterceptor方法
    ii.继承WebMvcConfigurerAdapter重写addInterceptor方法 +@EnableWebMvc
    iii.实现WebMvcConfigurer接口+@EnableWebMvc
  通用问题：public void addInterceptors(InterceptorRegistry registry)  参数是谁创建并注入的？
  回答：WebMvcConfigurationSupport在执行requestMappingHandlerMapping中执行了getInterceptors中建立的，同时调用addIntorceptor的方法并传入参数
...
public class WebMvcConfigurationSupport implements ApplicationContextAware, ServletContextAware {
    @Nullable
	private List<Object> interceptors;
    	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping mapping = createRequestMappingHandlerMapping();
		mapping.setOrder(0);
		mapping.setInterceptors(getInterceptors());//注意下文
		mapping.setContentNegotiationManager(mvcContentNegotiationManager());
		mapping.setCorsConfigurations(getCorsConfigurations());
		PathMatchConfigurer configurer = getPathMatchConfigurer();
		Boolean useSuffixPatternMatch = configurer.isUseSuffixPatternMatch();
		Boolean useRegisteredSuffixPatternMatch = configurer.isUseRegisteredSuffixPatternMatch();
		Boolean useTrailingSlashMatch = configurer.isUseTrailingSlashMatch();
		if (useSuffixPatternMatch != null) {
			mapping.setUseSuffixPatternMatch(useSuffixPatternMatch);
		}
		if (useRegisteredSuffixPatternMatch != null) {
			mapping.setUseRegisteredSuffixPatternMatch(useRegisteredSuffixPatternMatch);
		}
		if (useTrailingSlashMatch != null) {
			mapping.setUseTrailingSlashMatch(useTrailingSlashMatch);
		}
		UrlPathHelper pathHelper = configurer.getUrlPathHelper();
		if (pathHelper != null) {
			mapping.setUrlPathHelper(pathHelper);
		}
		PathMatcher pathMatcher = configurer.getPathMatcher();
		if (pathMatcher != null) {
			mapping.setPathMatcher(pathMatcher);
		}
		return mapping;
	}
	/**
	 * Protected method for plugging in a custom subclass of
	 * {@link RequestMappingHandlerMapping}.
	 * @since 4.0
	 */
	protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
		return new RequestMappingHandlerMapping();
	}
}
protected final Object[] getInterceptors() {
		if (this.interceptors == null) {
			InterceptorRegistry registry = new InterceptorRegistry();//成功找到啦
			addInterceptors(registry);
			registry.addInterceptor(new ConversionServiceExposingInterceptor(mvcConversionService()));
			registry.addInterceptor(new ResourceUrlProviderExposingInterceptor(mvcResourceUrlProvider()));
			this.interceptors = registry.getInterceptors();
		}
		return this.interceptors.toArray();
	}
	/**
	 * Override this method to add Spring MVC interceptors for
	 * pre- and post-processing of controller invocation.
	 * @see InterceptorRegistry
	 */
	protected void addInterceptors(InterceptorRegistry registry) {
	}
...
  下面两种注册拦截器的方式需要一些知识储备啦（递归学习嘛） https://blog.csdn.net/Farrell_zeng/article/details/107065100  https://www.jianshu.com/p/c5c1503f5367 https://www.jianshu.com/p/d47a09532de7 https://www.jianshu.com/p/c5c1503f5367
    @EnableWebMvc注解：注解快捷配置Spring Webmvc的一个注解。在使用该注解后配置一个继承于WebMvcConfigurerAdapter的配置类即可配置好Spring Webmvc
    @EnableWebMvc的源码，可以发现该注解就是为了引入一个DelegatingWebMvcConfiguration Java 配置类。并翻看DelegatingWebMvcConfiguration的源码会发现该类似继承于WebMvcConfigurationSupport的类
    我们再来说说DelegatingWebMvcConfigureation:   https://blog.csdn.net/andy_zhang2007/article/details/87348073
    它有WebMvcConfigurerComposite，是收集管理所有我们定义的所有WebWebMvcConfigurer的
    @EnableWebMvc注解与@Configuration注解一样使用，在spring容器启动时，ConfigurationClassPostProcessor这个BeanFactoryPostProcessor会查找使用了@Configuration注解的类，并处理该类上的其他注解，包括处理@EnableWebMvc注解。
    @EnableWebMvc注解通过@Import导入了DelegatingWebMvcConfiguration类，DelegatingWebMvcConfiguration继承于WebMvcConfigurationSupport，且也使用了@Configuration注解。则ConfigurationClassPostProcessor会继续处理DelegatingWebMvcConfiguration，包括注册DelegatingWebMvcConfiguration内部使用@Bean注解的方法，即上面列举的这些请求处理的核心组件，对应的BeanDefinition到内部的BeanFactory中。
...
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {
	// WebMvcConfigurerComposite 其实就是对多个 WebMvcConfigurer 的一个组合,
	// 从命名就可以看出这一点
	// WebMvcConfigurerComposite 自身也实现了接口 WebMvcConfigurer,
	// 问 : 为什么要组合多个 WebMvcConfigurer 然后自己又实现该接口 ?
	// 答 : 这么做的主要目的是在配置时简化逻辑。调用者对 WebMvcConfigurerComposite
	// 可以当作一个 WebMvcConfigurer 来使用，而对它的每个方法的调用都又会传导到
	// 它所包含的各个 WebMvcConfigurer 。
	private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();
	// 注入一组WebMvcConfigurer，这些WebMvcConfigurer由开发人员提供，或者框架其他部分提供，已经
	// 以bean的方式注入到容器中
	@Autowired(required = false)
	public void setConfigurers(List<WebMvcConfigurer> configurers) {
		if (!CollectionUtils.isEmpty(configurers)) {
			this.configurers.addWebMvcConfigurers(configurers);
		}
	}
	// 以下各个方法都是对WebMvcConfigurationSupport提供的配置原材料定制回调方法的覆盖实现，
	// 对这些方法的调用最终都转化成了对configurers的方法调用，从而实现了定制化缺省Spring MVC 
	// 配置的作用
	@Override
	protected void configurePathMatch(PathMatchConfigurer configurer) {
		this.configurers.configurePathMatch(configurer);
	}
  ...
  回调函数是什么？：https://www.zhihu.com/question/19801131  https://www.jianshu.com/p/67190bdce647
  WebMvcConfigurer该类是通过 "回调" 的方式来进行自定义化 Spring MVC 相关配置，大多数情况下，我们通过实现它的 抽象类 WebMvcConfigurerAdapter 来配置，因为它有WebMvcConfigurer 接口所有方法的一个空的实现，WebMvcConfigurer 接口中的所有方法都是 WebMvcConfigurationSupport 中提供给子类实现的空方法。
  为什么说是通过为什么是说它是通过回调来实现的自定义配置？ 用DelegatingWebMvcConfiguration 和 WebMvcConfigurerComposite 类来解释，在 WebMvcConfigurerComposite 收集到我们所有的定义的所有实现接口WebConfigurer的类，调用方法addInterceptor就依次调用了所有WebMvcConfiguer 接口的实现类的该方法
整个项目代码
第一种方法：（不建议）
弊端：
   i.WebMvcConfigurationSupport 在整个应用程序中只会生效一个，如果用户已经实现了 WebMvcConfigurationSupport，则 DelegatingWebMvcConfiguration 将不会生效，换句话来说，WebMvcConfigurer 的所有实现类将不会生效。而在Spring 中，如果类路径上不存在 WebMvcConfigurationSupport 的实例，则将会默认实现WebMvcConfigurerAdapter、DelegatingWebMvcConfiguration 来自定义mvc 配置。
   ii.如果用户重写默认的配置，如果对原理不是很清楚地开发者不小心重写了默认的配置，springmvc可能相关功能就无法生效，是一种不安全的行为
...
package com.tust.fir;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
public class interceptor implements HandlerInterceptor {
    /**
     * preHandle在执行Controller之前执行，返回true，则继续执行Contorller
     * 返回false则请求中断。
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)

            throws Exception {
        //只有返回true才会继续向下执行，返回false取消当前请求 
        long startTime = System.currentTimeMillis();
        httpServletRequest.setAttribute("startTime", startTime);
        return true;
    }
    /**
     * postHandle是在请求执行完，但渲染ModelAndView返回之前执行
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
            ModelAndView modelAndView) throws Exception {
        long startTime = (Long) httpServletRequest.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        StringBuilder sb = new StringBuilder(1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        sb.append("-----------------------").append(date).append("-------------------------------------\n");
        sb.append("URI       : ").append(httpServletRequest.getRequestURI()).append("\n");
        sb.append("CostTime  : ").append(executeTime).append("ms").append("\n");
        sb.append("-------------------------------------------------------------------------------");
        System.out.println(sb.toString());
    }
    /**
     * afterCompletion是在整个请求执行完毕后执行
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Object o, Exception e) throws Exception {
    }
}
...
...
package com.tust.fir;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
@Configuration
public class interceptorregis extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new interceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
...
开启tomcat，随便访问一个网页在控制台会输出时间，URL，以及CostTime
-----------------------2020-08-18 22:32:15-------------------------------------
URI       : /student/findname
CostTime  : 249ms
-------------------------------------------------------------------------------
第二种方法：The type WebMvcConfigurerAdapter is deprecated 因为版本问题这个类已经弃用 https://blog.csdn.net/Mercuriooo/article/details/104258338
第三种方法：（建议）
...
package com.tust.fir;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@EnableWebMvc
public class interceptorregis implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new interceptor()).addPathPatterns("/**");
    }
}
...
6、跨境
  （1）什么是跨域
         凡是发送请求url的协议、域名、端口三者之间任意一与当前页面地址不同即为跨域   参考：https://www.cnblogs.com/rainman/archive/2011/02/20/1959325.html
  （2）为什么跨域限制？https://zhuanlan.zhihu.com/p/28562290
         防止CSRF攻击，首先我们要知道什么事CSRF：https://www.cnblogs.com/hyddd/archive/2009/04/09/1432744.html
         你这可以这么理解CSRF攻击：攻击者盗用了你的身份，以你的名义发送恶意请求。CSRF能够做的事情包括：以你名义发送邮件，发消息，盗取你的账号，甚至于购买商品，虚拟货币转账......造成的问题包括：个人隐私泄露以及财产安全。
         但是这却给我们的开发带来了不变，而且在实际生产环境中，肯定会有很多台服务器之间交互，地址和端口都可能不同，怎么办？
  （3）如何安全的实现跨境资源共享？
         i.在拦截器preHandle中设置请求头Access-Control-Allow-Origin为“*”或者设置为和request相同的Origin。
...
     public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)

            throws Exception {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        long startTime = System.currentTimeMillis();
        httpServletRequest.setAttribute("startTime", startTime);
        return true;
    }
...
          ii.springmvc通过@CrossOrigin注解设置跨域请求    @CrossOrigin(origins = "*",methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT}) origins表示哪些域名可以跨域访问这个方法 
...
@CrossOrigin(origins = "http://domain2.com", maxAge = 3600)
@RestController
@RequestMapping("/account")
public class AccountController {

    @GetMapping("/{id}")
    public Account retrieve(@PathVariable Long id) {
        // ...
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        // ...
    }
}
...
    @CrossOrigin不起作用的原因：
    1）是springMVC的版本要在4.2或以上版本才支持@CrossOrigin
    2）非@CrossOrigin没有解决跨域请求问题，而是不正确的请求导致无法得到预期的响应，导致浏览器端提示跨域问题。
    1）在Controller注解上方添加@CrossOrigin注解后，仍然出现跨域问题，解决方案之一就是：
       在@RequestMapping注解中没有指定Get、Post方式，具体指定后，问题解决。
    

    
```
