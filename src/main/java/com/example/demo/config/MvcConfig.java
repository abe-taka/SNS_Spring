package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	// パス「/login」と「login.html」を紐づけ
	//ポート番号の直後の「/」は、なくても動作可能
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/account/account_Top").setViewName("/account/account_top");
	}
	
	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("static/");
	}*/
}