package com.example.demo.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.demo.account.AccountEntity;

@Entity
@Table(name="message_table")
public class MessageEntity {

	/* メッセージid */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id")
	public int messageid;
	
	/* 送信者id */
	@ManyToOne
	@JoinColumn(name="sender_id")
	public AccountEntity senderaccount;
	
	/* 受信者id */
	@ManyToOne
	@JoinColumn(name="getter_id")
	public AccountEntity getteraccount;
	
	/* 内容 */
	public String content;
	
	/* メッセージ日時 */
	@Column(name="message_day")
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