package com.example.demo.user_search;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.account.AccountEntity;

@Repository
public interface UserSearchRepository extends JpaRepository<AccountEntity,Integer>{

	//ユーザー検索
	public AccountEntity findByNickname(String nickname);
}