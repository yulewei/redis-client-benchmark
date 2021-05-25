# redis-client-benchmark


对 redis 的典型的 Java 客户端，[Jedis](https://github.com/redis/jedis)、[Lettuce](https://github.com/lettuce-io/lettuce-core)、
[Spring Data Redis](https://docs.spring.io/spring-data/redis/docs/current/reference/html/)（底层默认基于 Lettuce）进行性能测试。

假设 redis 服务器是 `172.16.3.95`，

``` bash
redis-cli -h 172.16.3.95 -p 6379 -a helloworld get foo
```

JMH 性能测试结果：

基于 Jedis 连接池，


``` text
Benchmark                   Mode  Cnt   Score    Error   Units
JedisPool8.testThreads10   thrpt   10  61.417 ±  7.771  ops/ms
JedisPool8.testThreads100  thrpt   10  89.274 ± 24.962  ops/ms
JedisPool8.testThreads50   thrpt   10  72.563 ± 12.983  ops/ms
```

``` text
Benchmark                     Mode  Cnt    Score    Error   Units
JedisPool100.testThreads10   thrpt   10   71.888 ±  6.167  ops/ms
JedisPool100.testThreads100  thrpt   10  626.489 ± 50.685  ops/ms
JedisPool100.testThreads150  thrpt   10  639.556 ± 50.365  ops/ms
JedisPool100.testThreads50   thrpt   10  356.273 ± 22.973  ops/ms
```

``` text
Benchmark                    Mode  Cnt    Score    Error   Units
LettuceTest.testThreads10   thrpt   10   77.678 ±  5.605  ops/ms
LettuceTest.testThreads100  thrpt   10  726.404 ± 73.657  ops/ms
LettuceTest.testThreads50   thrpt   10  369.844 ± 26.029  ops/ms
```

``` text
Benchmark                          Mode  Cnt    Score    Error   Units
SpringDataLettuce.testThreads10   thrpt   10   78.123 ±  6.632  ops/ms
SpringDataLettuce.testThreads100  thrpt   10  724.646 ± 53.133  ops/ms
SpringDataLettuce.testThreads50   thrpt   10  381.857 ± 35.664  ops/ms
```
