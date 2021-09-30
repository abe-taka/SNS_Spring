package com.example.demo.follow;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

@Component
public class FollowUserListForm {
	
	/* 投稿id*/
	private int chat_send_id;
	
	/* ユーザーid*/
	private int userid;
	
	/* 文章 */
	private String sentence;
	
	/* 日付 */
	private String chatsendday;
	
	/* アーティストid*/
	private int artist_id;
	
	/* 画像パス1*/
	private String chatimgpath1;
	
	/* 画像パス2*/
	private String chatimgpath2;
	
	/* 画像パス3*/
	private String chatimgpath3;
	
	/* 画像パス4*/
	private String chatimgpath4;
	
	/* アカウント画像*/
	private String acc_imgdata;
	
	/*  ニックネーム*/
	private String nickname;

	public int getChat_send_id() {
		return chat_send_id;
	}

	public void setChat_send_id(int chat_send_id) {
		this.chat_send_id = chat_send_id;
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
		return chatsendday;
	}

	public void setChat_send_day(String chatsendday) {
		this.chatsendday = chatsendday;
	}

	public int getArtist_id() {
		return artist_id;
	}

	public void setArtist_id(int artist_id) {
		this.artist_id = artist_id;
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

	public String getAcc_imgdata() {
		return acc_imgdata;
	}

	public void setAcc_imgdata(String acc_imgdata) {
		this.acc_imgdata = acc_imgdata;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
