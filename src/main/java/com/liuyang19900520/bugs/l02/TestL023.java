package com.liuyang19900520.bugs.l02;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
@RequestMapping("/l023")

public class TestL023 {

  static HashMap<String, Item> items = Maps.newHashMap();

  static {
    init();
  }

  private static HashMap<String, Item> init() {
    IntStream.rangeClosed(1, 10).boxed().forEach(x -> {
      items.put("item" + x, new Item("item" + x));
    });
    return items;
  }

  private static List<Item> createCartTest() {
    return IntStream.rangeClosed(1, 3).mapToObj(i -> "item" + ThreadLocalRandom.current().nextInt(0,
        items.keySet().size())).map(name -> items.get(name)).collect(Collectors.toList());
  }

  public static void main(String[] args) {
    List<Item> cartTest = createCartTest();

    System.out.println(cartTest);
  }

  private List<Item> createCart() {
    return IntStream.rangeClosed(1, 3).mapToObj(i -> "item" + ThreadLocalRandom.current().nextInt(0,
        items.keySet().size())).map(name -> items.get(name)).collect(Collectors.toList());
  }

  private boolean createOrder(List<Item> order) {
    //存放所有获得的锁
    List<ReentrantLock> locks = new ArrayList<>();
    for (Item item : order) {
      try {
        //获得锁10秒超时
        if (item.lock.tryLock(10, TimeUnit.SECONDS)) {
          locks.add(item.lock);
        } else {
          locks.forEach(ReentrantLock::unlock);
          return false;
        }
      } catch (InterruptedException e) {
      }
    }
    //锁全部拿到之后执行扣减库存业务逻辑
    try {
      order.forEach(item -> item.remaining--);
    } finally {
      locks.forEach(ReentrantLock::unlock);
    }
    return true;
  }


  @GetMapping("/wrong")
  public long wrong() {
    long begin = System.currentTimeMillis(); //并发进行100次下单操作，统计成功次数
    long success = IntStream.rangeClosed(1, 100).parallel().mapToObj(i -> {
      List<Item> cart = createCart();
      return createOrder(cart);
    }).filter(result -> result).count();
    log.info("success:{} totalRemaining:{} took:{}ms items:{}", success,
        items.entrySet().stream().map(item -> item.getValue().remaining)
            .reduce(new BinaryOperator<Integer>() {
              @Override
              public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
              }
            }).get(), System.currentTimeMillis() - begin, items);
    return success;
  }


  @GetMapping("/right")
  public long right() {
    long begin = System.currentTimeMillis(); //并发进行100次下单操作，统计成功次数
    long success = IntStream.rangeClosed(1, 100).parallel().mapToObj(i -> {
      List<Item> cart = createCart().stream().sorted(Comparator.comparing(Item::getName))
          .collect(Collectors.toList());
      return createOrder(cart);
    }).filter(result -> result).count();
    log.info("success:{} totalRemaining:{} took:{}ms items:{}", success,
        items.entrySet().stream().map(item -> item.getValue().remaining)
            .reduce(new BinaryOperator<Integer>() {
              @Override
              public Integer apply(Integer integer, Integer integer2) {
                return integer + integer2;
              }
            }).get(), System.currentTimeMillis() - begin, items);
    return success;
  }


}
