package com.liuyang19900520.bugs.l08;

import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author Max Liu
 * @since 2022/01/05
 */
public class Test08 {
  private String a;
  private String b;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Test08)) {
      return false;
    }
    Test08 test08 = (Test08) o;
    return Objects.equals(a, test08.a) && Objects.equals(b, test08.b);
  }

  @Override
  public int hashCode() {
    return Objects.hash(a, b);
  }
}
