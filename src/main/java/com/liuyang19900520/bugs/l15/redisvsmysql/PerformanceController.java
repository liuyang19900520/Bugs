package com.liuyang19900520.bugs.l15.redisvsmysql;

import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("redisvsmysql")
public class PerformanceController {

  @Autowired
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @GetMapping("redis")
  public void redis() {
    Assert.isTrue(stringRedisTemplate.opsForValue()
        .get("item" + (ThreadLocalRandom.current().nextInt(CommonMistakesApplication.ROWS) + 1))
        .equals(CommonMistakesApplication.PAYLOAD));
  }

  @GetMapping("redis2")
  public void redis2() {
    Assert.isTrue(stringRedisTemplate.keys("item71*").size() == 1111);
  }

  @GetMapping("mysql")
  public void mysql() {
    Assert.isTrue(jdbcTemplate.queryForObject("SELECT data FROM `r` WHERE name=?", new Object[]{
                ("item" + (ThreadLocalRandom.current().nextInt(CommonMistakesApplication.ROWS) + 1))},
            String.class)
        .equals(CommonMistakesApplication.PAYLOAD));
  }

  @GetMapping("mysql2")
  public void mysql2() {
    Assert.isTrue(
        jdbcTemplate.queryForList("SELECT name FROM `r` WHERE name LIKE 'item71%'", String.class)
            .size() == 1111);
  }
}
