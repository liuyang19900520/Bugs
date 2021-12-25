package com.liuyang19900520.bugs.l02;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

/**
 * <p>
 *
 * </p>
 *
 * @author Max Liu
 * @since 2021/12/21
 */
@RestController
@RequestMapping("/l02")

public class TestL021 {

    @GetMapping("/wrong")
    public int wrong(@RequestParam(value = "count", defaultValue = "1000000") int count) {
        Data.reset();
        //多线程循环一定次数调用Data类不同实例的wrong方法
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new Data().wrong());
        return Data.getCounter();
    }


    @GetMapping("/right")
    public int right(@RequestParam(value = "count", defaultValue = "1000000") int count) {
        Data.reset();
        //多线程循环一定次数调用Data类不同实例的wrong方法
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new Data().right());
        return Data.getCounter();
    }
}
