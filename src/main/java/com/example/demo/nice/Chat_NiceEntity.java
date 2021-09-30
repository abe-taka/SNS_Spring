package com.example.demo.nice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.demo.account.AccountEntity;
import com.example.demo.chat.ChatSendEntity;

@Entity
@Table(name="chat_nice_table")
public class Chat_NiceEntity {

	/* 投稿いいねid */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_nice_id")
	public int chatniceid;
	
	/* いいねユーザー */
	@ManyToOne
	@JoinColumn(name = "user_id")
	public AccountEntity account;
	
	/* 投稿id */
	@ManyToOne
	@JoinColumn(name = "chat_send_id")
	public ChatSendEntity chat;

	/* ゲッター、セッター */
	public int getChatniceid() {
		return chatniceid;
	}

	public void setChatniceid(int chatniceid) {
		this.chatniceid = chatniceid;
	}

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	public ChatSendEntity getChat() {
		return chat;
	}

	public void setChat(ChatSendEntity chat) {
		this.chat = chat;
	}
}