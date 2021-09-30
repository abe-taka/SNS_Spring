package com.example.demo.follow;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.account.AccountEntity;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Integer>{

	//フォローしているかを確認する
	public FollowEntity findByFollowaccountAndFolloweraccount(AccountEntity accountEntity, AccountEntity accountEntity2);
	
	@Transactional
	//削除
	public void deleteByFollowaccountAndFolloweraccount(AccountEntity accountEntity,AccountEntity accountEntity2);
	
	//フォロー一覧出力
	public List<FollowEntity> findByFollowaccount(AccountEntity accountEntity);
	
	//フォロワー一覧出力
	public List<FollowEntity> findByFolloweraccount(AccountEntity accountEntity);
}