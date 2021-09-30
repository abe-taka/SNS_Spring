package com.example.demo.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.account.AccountEntity;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer>{

	//受信者idを基に検索
	public List<MessageEntity> findByGetteraccount(AccountEntity account); 
}