package com.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.TimeUnit;

/**
 * https://github.com/redis/jedis/wiki/Getting-started
 *
 * @author yulewei on 2021/5/23
 */
@Fork(1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JedisPool8 {
    private static final int COUNT = 100;

    private JedisPool jedisPool = null;

    @Setup
    public void setup() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        jedisPool = new JedisPool(poolConfig, "localhost", 6379, 10000);
        jedisPool = new JedisPool(poolConfig, "172.16.3.95", 6379, 10000, "helloworld");
    }

    @TearDown
    public void close() {
        jedisPool.close();
    }

    @Benchmark
    @Threads(10)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public void testThreads10() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.get("foo");
        }
    }

    @Benchmark
    @Threads(50)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public void testThreads50() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.get("foo");
        }
    }

    @Benchmark
    @Threads(100)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public void testThreads100() {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.get("foo");
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JedisPool8.class.getSimpleName())
                .resultFormat(ResultFormatType.TEXT)
                .result("result-" + System.currentTimeMillis() + ".txt")
                .build();
        new Runner(opt).run();
    }
}
