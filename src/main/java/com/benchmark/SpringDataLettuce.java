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
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/
 *
 * @author yulewei on 2021/5/23
 */
@Fork(1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SpringDataLettuce {
    private static final int COUNT = 100;
    private LettuceConnectionFactory factory;
    private StringRedisTemplate template;

    @Setup
    public void setup() {
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        standaloneConfig.setHostName("172.16.3.95");
        standaloneConfig.setPort(6379);
        standaloneConfig.setPassword(RedisPassword.of("helloworld"));
        factory = new LettuceConnectionFactory(standaloneConfig);
        factory.afterPropertiesSet();
        template = new StringRedisTemplate(factory);
        template.afterPropertiesSet();
    }

    @TearDown
    public void close() {
        factory.destroy();
    }

    @Benchmark
    @Threads(10)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public String testThreads10() {
        return template.opsForValue().get("foo");
    }

    @Benchmark
    @Threads(50)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public String testThreads50() {
        return template.opsForValue().get("foo");
    }

    @Benchmark
    @Threads(100)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public String testThreads100() {
        return template.opsForValue().get("foo");
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SpringDataLettuce.class.getSimpleName())
                .resultFormat(ResultFormatType.TEXT)
                .result("result/result-" + System.currentTimeMillis() + ".txt")
                .build();
        new Runner(opt).run();
    }
}
