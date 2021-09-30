package com.example.demo.artist;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.demo.artist_register.Artist_RegisterEntity;
import com.example.demo.chat.ChatSendEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "artist_table")
public class ArtistEntity {

	//アーティストid
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "artist_id")
	private int artistid;
	
	//アーティスト名
	@Column(name = "artist_name")
	private String artistname;
	
	//登録数
	@Column(name = "registration_num")
	private int registrationnum;
	
	//画像パス
	@Column(name = "artist_imgpath")
	private String artistimgpath;

	@OneToMany(mappedBy="artist")
	private List<Artist_RegisterEntity> artist_RegisterEntity;
	
	@OneToMany(mappedBy="artist")
	@JsonBackReference("Unit3")
	private List<ChatSendEntity> chatSendEntity;
	
	//ゲッター、セッター
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