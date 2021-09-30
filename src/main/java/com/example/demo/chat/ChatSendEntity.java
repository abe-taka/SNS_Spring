package com.example.demo.chat;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.demo.account.AccountEntity;
import com.example.demo.artist.ArtistEntity;
import com.example.demo.comment.CommentSendEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "chat_send_table")
public class ChatSendEntity {

	/* 投稿id*/
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_send_id")
	private int chatsendid;
	
	/* ユーザーid */
	@ManyToOne
	@JsonBackReference("Unit2")
	@JoinColumn(name = "user_id")
	private AccountEntity account;
	
	/* 文章 */
	private String sentence;
	
	/* 日付 */
	@Column(name = "chat_send_day")
	private String chatsendday;
	
	/* 画像パス1*/
	@Column(name = "chatimg_path1")
	private String chatimgpath1;
	
	/* 画像パス2*/
	@Column(name = "chatimg_path2")
	private String chatimgpath2;
	
	/* 画像パス3*/
	@Column(name = "chatimg_path3")
	private String chatimgpath3;
	
	/* 画像パス4*/
	@Column(name = "chatimg_path4")
	private String chatimgpath4;
	
	/* アーティストid*/
	@ManyToOne
	@JsonBackReference("Unit3")
	@JoinColumn(name = "artist_id")
	private ArtistEntity artist;
	
	//コメントidとの外部キー設定
	@OneToMany(mappedBy="chat")
	@JsonManagedReference("Unit")
	private List<CommentSendEntity> commentSendEntity;
	
	/* ゲッター、セッター*/
	public int getChatsendid() {
		return chatsendid;
	}

	public void setChatsendid(int chatsendid) {
		this.chatsendid = chatsendid;
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

	public String getChatsendday() {
		return chatsendday;
	}

	public void setChatsendday(String chatsendday) {
		this.chatsendday = chatsendday;
	}

	public List<CommentSendEntity> getCommentSendEntity() {
		return commentSendEntity;
	}

	public void setCommentSendEntity(List<CommentSendEntity> commentSendEntity) {
		this.commentSendEntity = commentSendEntity;
	}

	public String getChatimgpath1() {
		return chatimgpath1;
	}

	public void setChatimgpath1(String chatimgpath1) {
		this.chatimgpath1 = chatimgpath1;
	}

	public String getChatimgpath2() {
		return chatimgpath2;
	}

	public void setChatimgpath2(String chatimgpath2) {
		this.chatimgpath2 = chatimgpath2;
	}

	public String getChatimgpath3() {
		return chatimgpath3;
	}

	public void setChatimgpath3(String chatimgpath3) {
		this.chatimgpath3 = chatimgpath3;
	}

	public String getChatimgpath4() {
		return chatimgpath4;
	}

	public void setChatimgpath4(String chatimgpath4) {
		this.chatimgpath4 = chatimgpath4;
	}

	public ArtistEntity getArtist() {
		return artist;
	}

	public void setArtist(ArtistEntity artist) {
		this.artist = artist;
	}
	
}