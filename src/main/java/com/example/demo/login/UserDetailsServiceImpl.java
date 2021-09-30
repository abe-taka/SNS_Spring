package com.example.demo.login;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	// DBからユーザ情報を検索するメソッドを実装したクラス
	@Autowired
	private LoginRepository loginRepository;

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

		LoginEntity loginEntity = loginRepository.findByMail(mail);

		if (loginEntity == null) {
			throw new UsernameNotFoundException("User" + mail + "was not found in the database");
		}
		// 権限のリスト
		// AdminやUserなどが存在するが、今回は利用しないのでUSERのみを仮で設定
		// 権限を利用する場合は、DB上で権限テーブル、ユーザ権限テーブルを作成し管理が必要
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority("USER");
		grantList.add(authority);

		// UserDetailsはインタフェースなのでUserクラスのコンストラクタで生成したユーザオブジェクトをキャスト
		UserDetails userDetails = (UserDetails) new User(loginEntity.getMail(),loginEntity.getPassword(), grantList);

		System.out.println("user.getMail()" + loginEntity.getMail());
		System.out.println("user.getPassword()" + loginEntity.getPassword());

		return userDetails;
	}
}
