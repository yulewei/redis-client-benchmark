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

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.benchmark.Constants.PROPERTIES_FILE;
import static com.benchmark.Constants.PROP_HOST;
import static com.benchmark.Constants.PROP_PASSWORD;
import static com.benchmark.Constants.PROP_PORT;

/**
 * https://github.com/lettuce-io/lettuce-core/wiki/Basic-usage
 *
 * @author yulewei on 2021/5/23
 */
@Fork(1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class LettuceTest {
    private static final int COUNT = 100;
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;

    @Setup
    public void setup() throws IOException {
        Properties prop = new Properties();
        prop.load(ClassLoader.getSystemClassLoader().getResourceAsStream(PROPERTIES_FILE));
        String host = prop.getProperty(PROP_HOST);
        int port = Integer.parseInt(prop.getProperty(PROP_PORT));
        String password = prop.getProperty(PROP_PASSWORD);

        redisClient = RedisClient.create(RedisURI.Builder.redis(host)
                .withPort(port).withPassword(password).build());
        connection = redisClient.connect();
    }

    @TearDown
    public void close() {
        redisClient.shutdown();
        connection.close();
    }

    @Benchmark
    @Threads(10)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public String testThreads10() {
        return connection.sync().get("foo");
    }

    @Benchmark
    @Threads(50)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public String testThreads50() {
        return connection.sync().get("foo");
    }

    @Benchmark
    @Threads(100)
    @OperationsPerInvocation(COUNT)
    @BenchmarkMode({Mode.Throughput})
    public String testThreads100() {
        return connection.sync().get("foo");
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LettuceTest.class.getSimpleName())
                .resultFormat(ResultFormatType.TEXT)
                .result("result/result-" + System.currentTimeMillis() + ".txt")
                .build();
        new Runner(opt).run();
    }

}
