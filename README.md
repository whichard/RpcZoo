# RpcZoo

我手写的RPC小框架开源了！可以作为Java爱好者学习Netty过后的小实践，学习RPC框架如Dubbo的小总结、学习ZooKeeper的Java客户端Curator的好帮手！
主要内容包括：
1. TCP通信：基于netty实现了一套自定义的远程服务调用；目前采用FastJson做序列化和反序列化
2. 注册中心：基于Zookeeper的Java客户端Curator实现了服务注册与发现
3. 多版本支持：基于自定义注解实现了服务端同时运行多个版本的服务，消费者可调用任意版本
4 负载均衡：目前做了两种基础的软负载均衡：Random和RoundRobin
欢迎大家使用和提建议！也欢迎大家Pull Request过来哇，我会虚心采纳的！

## Todo
- 添加一致性Hash负载均衡
- 采用Kyro二进制序列化替换FastJson，提升序列化和反序列化的性能
- 将项目的2.0版本做成SpringBoot Starter
- 目前传输协议部分没有深挖，未来考虑实现Dubbo长连接等多种协议模式
