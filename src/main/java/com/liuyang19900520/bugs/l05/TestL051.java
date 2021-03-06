package com.liuyang19900520.bugs.l05;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/l051")

public class TestL051 {


  private String getResponse(String url, int connectTimeout, int readTimeout) throws IOException {
    return Request.Get("http://localhost:8080/l051" + url)
        .connectTimeout(connectTimeout).socketTimeout(readTimeout).execute().returnContent()
        .asString();
  }

  @GetMapping("/client")
  public String client() throws IOException {
    log.info("client1 called");
    //服务端5s超时，客户端读取超时2秒
    return getResponse("/server?timeout=5000", 1000, 2000);
  }


  @GetMapping("/server")
  public void server(@RequestParam("timeout") int timeout) throws InterruptedException {
    log.info("server called");
    TimeUnit.MILLISECONDS.sleep(timeout);
    log.info("Done");
  }


}
