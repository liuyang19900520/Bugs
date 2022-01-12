package com.liuyang19900520.bugs.l01;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author Max Liu
 * @since 2021/12/18
 */
@Slf4j
@RestController
public class T1 {

  //循环次数
  private static int LOOP_COUNT = 10000000;
  //线程数量
  private static int THREAD_COUNT = 10;
  //元素数量
  private static int ITEM_COUNT = 10;

  public static void main(String[] args) {
    System.out.println(testFunction(2, i -> i * 2 + 1, j -> j * j));

    Function<String, String> function = Function.identity();
    String strValue = testIdentity(function);
    System.out.println(strValue);
  }

  public static int testFunction(int i, Function<Integer, Integer> function1,
      Function<Integer, Integer> function2) {
    return function1.compose(function2).apply(i);
  }

  public static String testIdentity(Function<String, String> function) {
    return function.apply("hello world");
  }

  private Map<String, Long> normaluse() throws InterruptedException {
    ConcurrentHashMap<String, Long> freqs = new ConcurrentHashMap<>(ITEM_COUNT);

    ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);

    forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {

      //获得一个随机的Key
      String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);

      synchronized (freqs) {
        if (freqs.containsKey(key)) {
          //Key存在则+1
          freqs.put(key, freqs.get(key) + 1);
        } else {
          //Key不存在则初始化为1
          freqs.put(key, 1L);
        }
      }
    }));

    forkJoinPool.shutdown();
    forkJoinPool.awaitTermination(1, TimeUnit.HOURS);

    return freqs;

  }

  private Map<String, Long> gooduse() throws InterruptedException {

    ConcurrentHashMap<String, LongAdder> freqs = new ConcurrentHashMap<>(ITEM_COUNT);

    ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);

    forkJoinPool.execute(() -> IntStream.rangeClosed(1, LOOP_COUNT).parallel().forEach(i -> {

          String key = "item" + ThreadLocalRandom.current().nextInt(ITEM_COUNT);

          //利用computeIfAbsent()方法来实例化LongAdder，然后利用LongAdder来进行线程安全计数
          freqs.computeIfAbsent(key, k -> new LongAdder()).increment();

        }

    ));

    forkJoinPool.shutdown();

    forkJoinPool.awaitTermination(1, TimeUnit.HOURS);

    //因为我们的Value是LongAdder而不是Long，所以需要做一次转换才能返回
    return freqs.entrySet().stream()
        .collect(Collectors.toMap(
            e -> e.getKey(),
            e -> e.getValue().longValue())
        );

  }

  @GetMapping("/good")
  public String good() throws InterruptedException {

    StopWatch stopWatch = new StopWatch();

    stopWatch.start("normaluse");

    Map<String, Long> normaluse = normaluse();

    stopWatch.stop();

    //校验元素数量
    Assert.isTrue(normaluse.size() == ITEM_COUNT, "normaluse size error");

    //校验累计总数
    Assert.isTrue(normaluse.entrySet().stream()
            .mapToLong(item -> item.getValue()).reduce(0, Long::sum) == LOOP_COUNT
        , "normaluse count error");

    stopWatch.start("gooduse");

    Map<String, Long> gooduse = gooduse();

    stopWatch.stop();

    Assert.isTrue(gooduse.size() == ITEM_COUNT, "gooduse size error");

    Assert.isTrue(gooduse.entrySet().stream()

            .mapToLong(item -> item.getValue())

            .reduce(0, Long::sum) == LOOP_COUNT

        , "gooduse count error");

    log.info(stopWatch.prettyPrint());

    return "OK";

  }
}
