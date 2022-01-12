package com.liuyang19900520.bugs.l02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
@RequestMapping("/l022")

public class TestL022 {

  private List<Integer> data = new ArrayList<>();

  //不涉及共享资源的慢方法
  private void slow() {
    try {
      TimeUnit.MILLISECONDS.sleep(10);
    } catch (InterruptedException e) {
      log.error(String.valueOf(e));
    }
  }

  //错误的加锁方法
  @GetMapping("/wrong")
  public int wrong() {
    long begin = System.currentTimeMillis();
    IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
      //加锁粒度太粗了
      synchronized (this) {
        slow();
        data.add(i);
      }
    });
    log.info("took:{}", System.currentTimeMillis() - begin);
    return data.size();
  }


  @GetMapping("/right")
  public int right() {
    long begin = System.currentTimeMillis();
    IntStream.rangeClosed(1, 1000).parallel().forEach(i -> {
      slow();
      //加锁粒度太粗了
      synchronized (data) {
        data.add(i);
      }
    });
    log.info("took:{}", System.currentTimeMillis() - begin);
    return data.size();
  }
}
