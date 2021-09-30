package com.example.demo.nice;

import com.example.demo.account.AccountEntity;
import com.example.demo.chat.ChatSendEntity;

public class Chat_Form {

	/* 投稿いいねid */
	public int chatniceid;
	
	/* いいねユーザー */
	public AccountEntity account;
	
	/* 投稿id */
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