package com.example.demo.nice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.account.AccountEntity;
import com.example.demo.chat.ChatSendEntity;

@Repository
public interface Chat_NiceRepository extends JpaRepository<Chat_NiceEntity,Integer>{

	//フォローidを検索
	public Chat_NiceEntity findByAccountAndChat(AccountEntity acc,ChatSendEntity chat);
}