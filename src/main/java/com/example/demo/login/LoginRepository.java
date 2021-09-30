package com.example.demo.login;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<LoginEntity, Integer> {
	// メールアドレス検索
	public LoginEntity findByMail(String mail);

	// ログイン検索(メールアドレス、パスワード)
	public List<LoginEntity> findByMailAndPassword(String mail, String password);
}