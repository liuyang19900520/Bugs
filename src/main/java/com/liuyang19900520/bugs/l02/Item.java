package com.liuyang19900520.bugs.l02;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>
 *
 * </p>
 *
 * @author Max Liu
 * @since 2021/12/23
 */


@RequiredArgsConstructor
@Data
public class Item {
    final String name; //商品名
    int remaining = 1000; //库存剩余
    @ToString.Exclude //ToString不包含这个字段
    ReentrantLock lock = new ReentrantLock();





}
