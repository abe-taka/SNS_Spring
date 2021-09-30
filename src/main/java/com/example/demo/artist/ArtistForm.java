package com.example.demo.artist;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.demo.artist_register.Artist_RegisterEntity;

public class ArtistForm {

	// アーティストid
	private int artistid;

	// アーティスト名
	private String artistname;

	// 登録数
	private int registrationnum;

	// 画像パス
	private String artistimgpath;

	@OneToMany(mappedBy = "artist")
	private List<Artist_RegisterEntity> artist_RegisterEntity;

	public int getArtistid() {
		return artistid;
	}

	public void setArtistid(int artistid) {
		this.artistid = artistid;
	}

	public String getArtistname() {
		return artistname;
	}

	public void setArtistname(String artistname) {
		this.artistname = artistname;
	}

	public int getRegistrationnum() {
		return registrationnum;
	}

	public void setRegistrationnum(int registrationnum) {
		this.registrationnum = registrationnum;
	}

	public String getArtistimgpath() {
		return artistimgpath;
	}

	public void setArtistimgpath(String artistimgpath) {
		this.artistimgpath = artistimgpath;
	}

	public List<Artist_RegisterEntity> getArtist_RegisterEntity() {
		return artist_RegisterEntity;
	}

	public void setArtist_RegisterEntity(List<Artist_RegisterEntity> artist_RegisterEntity) {
		this.artist_RegisterEntity = artist_RegisterEntity;
	}
}
