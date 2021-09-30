package com.example.demo.comment;

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
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "comment_table")
public class CommentSendEntity {

	// コメントid
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private int commentid;

	// ユーザーid
	@ManyToOne
	@JsonBackReference("Unit3")
	@JoinColumn(name = "user_id")
	private AccountEntity account;

	// 文章
	private String sentence;

	// 画像
	@Column(name = "commentimg_path")
	private String commentimgpath;

	// コメント日
	@Column(name = "comment_day")
	private String commentday;

	// 投稿id
	@ManyToOne
	@JsonBackReference("Unit")
	@JoinColumn(name = "chat_send_id")
	private ChatSendEntity chat;

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

	public ChatSendEntity getChat() {
		return chat;
	}

	public void setChat(ChatSendEntity chat) {
		this.chat = chat;
	}

}
