# redis-client-benchmark


对典型的 redis 的 Java 客户端，[Jedis](https://github.com/redis/jedis)、[Lettuce](https://github.com/lettuce-io/lettuce-core)、
[Spring Data Redis](https://docs.spring.io/spring-data/redis/docs/current/reference/html/)（底层默认基于 Lettuce）进行性能测试并对比。


客户端 Jedis，基于连接池，连接数 8（即默认的 PoolConfig 配置），线程数分别为 10、50、100，JMH 性能测试结果：

``` text
Benchmark                   Mode  Cnt   Score    Error   Units
JedisPool8.testThreads10   thrpt   10  61.417 ±  7.771  ops/ms
JedisPool8.testThreads100  thrpt   10  89.274 ± 24.962  ops/ms
JedisPool8.testThreads50   thrpt   10  72.563 ± 12.983  ops/ms
```

客户端 Jedis，基于连接池，连接数 100，线程数分别为 10、50、100、150，JMH 性能测试结果：

``` text
Benchmark                     Mode  Cnt    Score    Error   Units
JedisPool100.testThreads10   thrpt   10   71.888 ±  6.167  ops/ms
JedisPool100.testThreads100  thrpt   10  626.489 ± 50.685  ops/ms
JedisPool100.testThreads150  thrpt   10  639.556 ± 50.365  ops/ms
JedisPool100.testThreads50   thrpt   10  356.273 ± 22.973  ops/ms
```

客户端 Lettuce，单连接，线程数分别为 10、50、100，JMH 性能测试结果：

``` text
Benchmark                    Mode  Cnt    Score    Error   Units
LettuceTest.testThreads10   thrpt   10   77.678 ±  5.605  ops/ms
LettuceTest.testThreads100  thrpt   10  726.404 ± 73.657  ops/ms
LettuceTest.testThreads50   thrpt   10  369.844 ± 26.029  ops/ms
```

客户端 Spring Data Redis，底层 Lettuce 驱动，单连接，线程数分别为 10、50、100，JMH 性能测试结果：

``` text
Benchmark                          Mode  Cnt    Score    Error   Units
SpringDataLettuce.testThreads10   thrpt   10   78.123 ±  6.632  ops/ms
SpringDataLettuce.testThreads100  thrpt   10  724.646 ± 53.133  ops/ms
SpringDataLettuce.testThreads50   thrpt   10  381.857 ± 35.664  ops/ms
```


分析 JMH 性能测试结果，容易得出如下**结论**：

1. 客户端 Jedis，配置的连接池的连接数越大，性能相对越高
2. 客户端 Jedis，在并发的线程数超越连接池的连接数后，吞吐量不再随线程数增加而增大
3. Lettuce 单连接，只占用一个物理连接，性能优于 Jedis 连接池
4. Lettuce 单连接，吞吐量与并发的线程数正相关
5. 使用封装的 Lettuce 的 Spring Data Redis，与直接使用 Lettuce，性能区别不大


**相关资料：**
 - Lettuce: Connection Pooling <https://github.com/lettuce-io/lettuce-core/wiki/Connection-Pooling>
 - Why is Lettuce the default Redis client used in Spring Session Redis? #789 <https://github.com/spring-projects/spring-session/issues/789>
 - Consider Lettuce instead of Jedis as default Redis driver dependency #10480 <https://github.com/spring-projects/spring-boot/issues/10480>
