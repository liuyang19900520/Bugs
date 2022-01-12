package com.liuyang19900520.bugs.l09;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 *
 * </p>
 *
 * @author Max Liu
 * @since 2022/01/05
 */
public class L09Test {

  public static void main(String[] args) {

    Set<BigDecimal> set = new HashSet<>();
    set.add(new BigDecimal("1.0"));
    System.out.println(set.contains(new BigDecimal("1.000")));

    Set<BigDecimal> set2 = new HashSet<>();
    set2.add(new BigDecimal("1.0").stripTrailingZeros());
    System.out.println(set2.contains(new BigDecimal("1.000").stripTrailingZeros()));


    // Initialize two variables and
    // both are of "String" type
    String val1 = "100.24";
    String val2 = "-2.1456";

    // Initialize two BigDecimal objects and
    // one MathContext
    BigDecimal b_dec1 = new BigDecimal(val1);
    BigDecimal b_dec2 = new BigDecimal(val2);


    // the scale of this BigDecimal b_dec1
    // i.e. scale is the number of digits
    // represented after decimal point here
    // val1 = 100.24 (i.e. 24 two digits after
    // decimal)
    int scale = b_dec1.scale();
    System.out.println("b_dec1.scale(): " + scale);

    // the scale of this BigDecimal b_dec2
    // i.e. scale is the number of digits
    // represented after decimal point here
    // val2 = -2.1456 (i.e. 1456 four digits after
    // decimal)
    scale = b_dec2.scale();
    System.out.println("b_dec2.scale(): " + scale);

    Arrays.asList(1);

  }
}
