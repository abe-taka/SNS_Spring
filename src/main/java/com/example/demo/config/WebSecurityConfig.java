package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.login.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	// パスワードのハッシュ化に利用
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	//静的ファイルのアクセス制御をなくす
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/image/**", "/css/**", "/javascript/**","/bootstrap/**","/webjars/**");
	}

	//認証パスの設定
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//認証なしでもアクセスできるパス
		http.authorizeRequests()
				.antMatchers("/login/new_index", "/login/new_confirm", "/login/new_comp", "/login/mail_error","/login/password_again").permitAll()
				.anyRequest().authenticated()
				.and()
			//ログイン認証
			.formLogin()
				.loginPage("/") // ログインページ、MvcConfigにも設定しているならパスを統一する
				.loginProcessingUrl("/chat/chat") // フォームのSubmitURL、login.htmlの送信先と同じにする、ログイン認証された時のURLがここになる。
				.usernameParameter("mail") // パラメーター１
				.passwordParameter("password")// パラメーター２
				.successForwardUrl("/chat/chat")//ログイン認証先
				.failureUrl("/login_error")//ログイン失敗先
				.permitAll()
				.and()
			//ログアウト処理
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.deleteCookies("auth_code","JSESSIONID")
				.invalidateHttpSession(true)
                .permitAll()
				.and()
				.rememberMe();
	}

	//認証処理はuserDetailsServiceに設定
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
		System.out.println("configconfig");
	}
}