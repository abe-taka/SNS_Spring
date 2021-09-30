package com.example.demo.artist_register;

import com.example.demo.artist.ArtistEntity;

public class Artist_RegisterForm {

	// アーティスト登録id
	private int registerid;
	
	// ユーザーid
	private int userid;

	// アーティストid
	private ArtistEntity artist;

	// 文章
	private String sentence;

	// 画像パス1
	private String regartistimgpath1;
	
	// 画像パス2
	private String regartistimgpath2;

	// ゲッター、セッター
	public int getRegisterid() {
		return registerid;
	}

	public void setRegisterid(int registerid) {
		this.registerid = registerid;
	}
	
	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public ArtistEntity getArtist() {
		return artist;
	}

	public void setArtist(ArtistEntity artist) {
		this.artist = artist;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getRegartistimgpath1() {
		return regartistimgpath1;
	}

	public void setRegartistimgpath1(String regartistimgpath1) {
		this.regartistimgpath1 = regartistimgpath1;
	}

	public String getRegartistimgpath2() {
		return regartistimgpath2;
	}

	public void setRegartistimgpath2(String regartistimgpath2) {
		this.regartistimgpath2 = regartistimgpath2;
	}
}
