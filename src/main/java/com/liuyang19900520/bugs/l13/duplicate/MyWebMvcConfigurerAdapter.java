package com.liuyang19900520.bugs.l13.duplicate;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * <p>
 *
 * </p>
 *
 * @author Max Liu
 * @since 2022/01/17
 */
@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurationSupport {


  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/com/liuyang19900520/bugs/l13/duplicate/");
    super.addResourceHandlers(registry);
  }
}
