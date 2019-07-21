# RpcZoo

手写一个简单rpc框架，示意图如下：
![](http://ww1.sinaimg.cn/large/e069f60egy1g57a7n6j94j20nw0dm74u.jpg)

实现的主要内容包括：

1. 基于netty实现了一套自定义远程调用；
2. 基于Zookeeper实现了服务的自动注册与发现；
3. 实现了服务的多版本支持与负载均衡。
