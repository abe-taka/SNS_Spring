package com.example.demo.artist_register;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.example.demo.artist.ArtistEntity;

@Entity
@Table(name = "registration_artist_table")
public class Artist_RegisterEntity {

	// アーティスト登録id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "register_id")
	private int registerid;
	
	// ユーザーid
	@Column(name = "user_id")
	private int userid;

	// アーティストid
	@ManyToOne
	@JoinColumn(name = "artist_id")
	private ArtistEntity artist;

	// 文章
	private String sentence;

	// 画像パス1
	@Column(name = "register_artist_imgpath1")
	private String regartistimgpath1;
	
	// 画像パス2
	@Column(name = "register_artist_imgpath2")
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

	public ArtistEntity getArtist() {
		return artist;
	}

	public void setArtist(ArtistEntity artist) {
		this.artist = artist;
	}

}