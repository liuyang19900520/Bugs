package com.liuyang19900520.bugs.l03;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author Max Liu
 * @since 2021/12/21
 */
@Slf4j
@RestController
@RequestMapping("/l031")

public class TestL031 {

  public static void main(String[] args) {
    String[] words = new String[]{"Hello", "World"};
    List<String[]> a = Arrays.stream(words).map(word -> word.split("")).distinct()
        .collect(Collectors.toList());
    a.forEach(b -> {
      Arrays.stream(b).forEach(System.out::println);
    });

    List<String> collect = Stream.of(words).flatMap(word -> Stream.of(word.split("")))
        .collect(Collectors.toList());

    collect.forEach(System.out::println);

    LongStream stream = LongStream.of(3L, 5L, 7L, 9L, 11L);

    // Creating a Stream
    // Using LongStream mapToObj(LongFunction mapper)
    Stream<BigInteger> stream1 = stream.mapToObj(BigInteger::valueOf);

    // Displaying an object-valued Stream
    // consisting of the results of
    // applying the given function.
    stream1.forEach(num -> System.out.println(num.add(BigInteger.TEN)));

  }

  @GetMapping("/wrong")
  public void oom1() throws InterruptedException {

    ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    //打印线程池的信息，稍后我会解释这段代码
    printStats(threadPool);
    for (int i = 0; i < 100000000; i++) {
      threadPool.execute(() -> {
        String payload =
            IntStream.rangeClosed(1, 1000000).mapToObj(__ -> "a").collect(Collectors.joining(""))
                + UUID.randomUUID().toString();
        try {
          TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
        }
        log.info(payload);
      });
    }

    threadPool.shutdown();
    threadPool.awaitTermination(1, TimeUnit.HOURS);
  }

  @GetMapping("/right")
  public int right() throws InterruptedException {
    //使用一个计数器跟踪完成的任务数
    AtomicInteger atomicInteger = new AtomicInteger();
    //创建一个具有2个核心线程、5个最大线程，使用容量为10的ArrayBlockingQueue阻塞队列作为工
    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 5, 5, TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(10),
        new ThreadFactoryBuilder().setNameFormat("demo-threadpool-%d").build(),
        new ThreadPoolExecutor.AbortPolicy());

    printStats(threadPool);
//每隔1秒提交一次，一共提交20次任务
    IntStream.rangeClosed(1, 20).forEach(i -> {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      int id = atomicInteger.incrementAndGet();
      try {
        threadPool.submit(() -> {
          log.info("{} started", id); //每个任务耗时10秒
          try {
            TimeUnit.SECONDS.sleep(10);
          } catch (InterruptedException e) {
          }
          log.info("{} finished", id);

        });
      } catch (Exception ex) {
//提交出现异常的话，打印出错信息并为计数器减一
        log.error("error submitting task {}", id, ex);
        atomicInteger.decrementAndGet();
      }

    });

    TimeUnit.SECONDS.sleep(60);
    return atomicInteger.intValue();
  }

  private void printStats(ThreadPoolExecutor threadPool) {
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
      log.info("=========================");
      log.info("Pool Size: {}", threadPool.getPoolSize());
      log.info("Active Threads: {}", threadPool.getActiveCount());
      log.info("Number of Tasks Completed: {}", threadPool.getCompletedTaskCount());
      log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());
      log.info("=========================");
    }, 0, 1, TimeUnit.SECONDS);
  }

}
