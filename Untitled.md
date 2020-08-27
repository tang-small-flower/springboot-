
                                            三台机器实现大数据集群搭建与应用
一、基础环境搭建
1、基础搭建
  1.1 使用连接工具连接节点，更改本地源
  1.2 配置hosts文件（三台机器都执行）
  1.3 关闭防火墙（三台机器都执行）
  1.4 时间同步
  1.5 配置ssh免密
2、安装JDK
3、安装zookeeper
4、安装hadoop
4.1 解压安装包，配置环境变量
4.2 配置hadoop各组件
5、安装hbase
6、安装hive
  6.1slave2上安装MySQL server
  6.2创建工作路径，解压安装包
  6.3slave1中建立文件
  6.4解决版本冲突和jar包依赖问题。
  6.5Slave1作为服务器端配置hive
  6.6Master作为客户端配置hive
  6.7成功启动Hive
7、Spark安装
  7.1安装scala环境
  7.2安装spark
<mark style=background-color:red>改变高亮颜色</mark> 



