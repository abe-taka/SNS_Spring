package com.example.demo.chat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.demo.artist.ArtistEntity;

public class ChatSendForm {

	/* 投稿id*/
	private int chatsendid;
	
	/* ユーザーid*/
	private int userid;
	
	/* 文章 */
	private String sentence;
	
	/* 日付 */
	private String chat_send_day;
	
	/* 画像パス1*/
	private String chatimgpath1;
	
	/* 画像パス2*/
	private String chatimgpath2;
	
	/* 画像パス3*/
	private String chatimgpath3;
	
	/* 画像パス4*/
	private String chatimgpath4;
	
	/* アーティストid*/
	private ArtistEntity artist;

	
	/* ゲッター、セッター*/
	public int getChatsendid() {
		return chatsendid;
	}

	public void setChatsendid(int chatsendid) {
		this.chatsendid = chatsendid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getChat_send_day() {
		return chat_send_day;
	}

	public void setChat_send_day(String chat_send_day) {
		this.chat_send_day = chat_send_day;
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
