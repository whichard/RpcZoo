# RpcZoo

我手写的RPC小框架开源了！可以作为Java爱好者学习Netty过后的小实践，学习RPC框架如Dubbo的小总结、学习ZooKeeper的Java客户端Curator的好帮手！
主要内容包括：
1. TCP通信：基于netty实现了一套自定义的远程服务调用，将其异步通信包装为同步；目前采用FastJson做序列化和反序列化
2. 注册中心：基于Zookeeper的Java客户端Curator实现了服务注册与发现
3. 多版本支持：基于自定义注解实现了服务端同时运行多个版本的服务，消费者可调用任意版本
4 负载均衡：目前做了两种基础的软负载均衡：Random和RoundRobin
欢迎大家使用和提建议！也欢迎大家Pull Request过来哇，我会虚心采纳的！

## StartUp
我已经编写了简单的服务端和消费者端Demo放在/demo路径下，启动ZooKeeper服务器，设置好相应的配置，然后先后打开服务端和消费者端进程，就可以感受到简单的Hello World了！

## Todo
- 添加一致性Hash负载均衡
- 采用Kyro二进制序列化替换FastJson，提升序列化和反序列化的性能
- 将项目的2.0版本做成SpringBoot Starter
- 目前传输协议部分没有深挖，未来考虑实现Dubbo长连接等多种协议模式

## Wiki
### 调用流程
消费者根据服务接口获得对应的代理对象，然后直接调用接口的方法即可获得返回结果，可以实现像调用本地服务一样调用远程服务；
本地调用端主要通过动态代理的方式来实现上述功能，调用接口方法的时候，其代理对象实现了具体的网络通讯细节，将接口名、方法名、方法参数等请求信息发送给远程服务端并等待远程服务端的返回信息；
远程服务端根据请求信息通过反射获得具体的服务实现类，执行实现类的相应方法后并将调用结果返回给调用端；
调用端接收到返回值，代理对象将其封装为返回结果给消费者， 整个远程调用即结束。

### 序列化与反序列化
#### 消费者端
1. 方法调用首先通过动态代理方法获得类名、方法名、参数数组并将其封装为RpcRequest对象并使用FastJson序列化为Json字符串并通过Netty发送给服务端
2. 等待服务端处理，回发处理结果，将收到的结果反序列化得到RpcResponse对象，抽取出结果返回给调用。
#### 服务端
首先将接收到的字符串反序列化转为RpcRequest对象，根据RpcRequest对象的服务名、方法名、参数数组从handlerMap中获得对应的服务实现类；然后通过反射的方式获得其方法的调用结果，最终将调用结果RpcResponse对象转为Json字符串后通过Netty回发给消费者端。
