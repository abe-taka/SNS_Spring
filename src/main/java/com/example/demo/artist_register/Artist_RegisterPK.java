//package com.example.demo.artist_register;
//
//import java.io.Serializable;
//
//import javax.persistence.Column;
//import javax.persistence.Embeddable;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//
//import com.example.demo.artist.ArtistEntity;
//
//@Embeddable
//public class Artist_RegisterPK implements Serializable {
//	// ユーザーid
//	@Id
//	@Column(name = "user_id")
//	private int userid;
//
//	// アーティストid
//	@Id
//	@ManyToOne
//	@JoinColumn(name = "artist_id")
//	private ArtistEntity artist;
//
//}