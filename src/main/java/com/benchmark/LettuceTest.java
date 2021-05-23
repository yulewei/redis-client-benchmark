package com.benchmark;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
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

import java.util.concurrent.TimeUnit;

/**
 * https://github.com/lettuce-io/lettuce-core/wiki/Basic-usage
 *
 * @author yulewei on 2021/5/23
 */
@Fork(1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class LettuceTest {
    private static final int COUNT = 100;
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;

    @Setup
    public void setup() {
        redisClient = RedisClient.create(RedisURI.Builder.redis("172.16.3.95").withPassword("helloworld").build());
        connection = redisClient.connect();
    }

    @TearDown
    public void close() {
        redisClient.shutdown();
        connection.close();
    }

    @Benchmark
    @Threads(1)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public void testThreads1() {
        connection.sync().get("foo");
    }

    @Benchmark
    @Threads(10)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public void testThreads10() {
        connection.sync().get("foo");
    }

    @Benchmark
    @Threads(50)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public void testThreads50() {
        connection.sync().get("foo");
    }

    @Benchmark
    @Threads(100)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public void testThreads100() {
        connection.sync().get("foo");
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LettuceTest.class.getSimpleName())
                .resultFormat(ResultFormatType.TEXT)
                .result("result-" + System.currentTimeMillis() + ".txt")
                .build();
        new Runner(opt).run();
    }

}
