package com.example.demo.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.account.AccountEntity;
import com.example.demo.follow.FollowEntity;
import com.example.demo.login.LoginEntity;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
	// メールアドレスを検索
	public AccountEntity findByMail(String mail);
	
	//ユーザー検索(フォロー処理用)
	public AccountEntity findByUserid(int userid);
	
	//全フォローユーザーの取得
	@Query(value="select a.user_id,mailaddress,password,nickname,accountimg_path,introduction,follow_num,follower_num FROM follow_table f,account_table a WHERE f.user_id = a.user_id AND f.follow_user_id = :followuserid",nativeQuery = true)
	public List<AccountEntity> findByFollowAccountData(@Param("followuserid") int followuserid);
}