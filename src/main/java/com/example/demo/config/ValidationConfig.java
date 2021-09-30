package com.example.demo.config;

import javax.validation.Validator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ValidationConfig extends WebMvcConfigurerAdapter {

	public org.springframework.validation.Validator getValidator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		// メッセージファイルを読込むための設定を記載します
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		// 「setBasename」を使用することで任意のファイル名に変更することも可能です
		messageSource.setBasename("classpath:ValidationMessages");
		// 「setDefaultEncoding」を使用することで任意の文字コードに変更することも可能です
		messageSource.setDefaultEncoding("UTF-8");
		validator.setValidationMessageSource(messageSource);
		return validator;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// ハンドラーでstaticコンテンツにアクセスできるようにする 動作していない
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		// to serve static .html pages...
		registry.addResourceHandler("/static/**").addResourceLocations("/resources/static/");
	}
}