package com.example.demo.message;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.demo.account.AccountEntity;

public class MessageForm {
	
	/* メッセージid */
	public int messageid;
	
	/* 送信者id */
	public AccountEntity senderaccount;
	
	/* 受信者id */
	public AccountEntity getteraccount;
	
	/* 内容 */
	public String content;
	
	/* メッセージ日時 */
	public String messageday;

	
	
	/* ゲッター、セッター */
	public int getMessageid() {
		return messageid;
	}

	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}

	public AccountEntity getSenderaccount() {
		return senderaccount;
	}

	public void setSenderaccount(AccountEntity senderaccount) {
		this.senderaccount = senderaccount;
	}

	public AccountEntity getGetteraccount() {
		return getteraccount;
	}

	public void setGetteraccount(AccountEntity getteraccount) {
		this.getteraccount = getteraccount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMessageday() {
		return messageday;
	}

	public void setMessageday(String messageday) {
		this.messageday = messageday;
	}
}
