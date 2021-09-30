package com.example.demo.comment;

import com.example.demo.account.AccountEntity;

public class CommentSendForm {

	// コメントid
	private int commentid;

	// ユーザーid
	private AccountEntity account;

	// 文章
	private String sentence;

	// 画像
	private String commentimgpath;

	// コメント日
	private String commentday;

	// 投稿id
	private int chatsendid;

	/* ゲッター、セッター */
	public int getCommentid() {
		return commentid;
	}

	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}

	
	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getCommentimgpath() {
		return commentimgpath;
	}

	public void setCommentimgpath(String commentimgpath) {
		this.commentimgpath = commentimgpath;
	}

	public String getCommentday() {
		return commentday;
	}

	public void setCommentday(String commentday) {
		this.commentday = commentday;
	}

	public int getChatsendid() {
		return chatsendid;
	}

	public void setChatsendid(int chatsendid) {
		this.chatsendid = chatsendid;
	}
}
