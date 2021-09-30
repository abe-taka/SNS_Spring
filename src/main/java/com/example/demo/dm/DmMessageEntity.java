package com.example.demo.dm;

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

import com.example.demo.chat.ChatSendEntity;
import com.example.demo.comment.CommentSendEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "dm_message_table")
public class DmMessageEntity {

	// DMメッセージid
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dm_message_id")
	private int dmmessageid;

	//送信ユーザー
	@Column(name = "send_usermail")
	private String sendusermail;
	
	//受け取りユーザー
	@Column(name = "get_usermail")
	private String getusermail;
	
	// 文章
	private String sentence;
	
	// 画像パス
	@Column(name = "image_path")
	private String imagepath;

	// コメント日
	@Column(name = "dm_message_day")
	private String dmmessageday;

	//一対多の「多」
	@ManyToOne
	@JsonBackReference("Unit2")
	@JoinColumn(name = "dm_room_id")
	private DmRoomEntity dmroom;
	
	// ゲッター、セッター
	public int getDmmessageid() {
		return dmmessageid;
	}

	public void setDmmessageid(int dmmessageid) {
		this.dmmessageid = dmmessageid;
	}

	public String getSendusermail() {
		return sendusermail;
	}

	public void setSendusermail(String sendusermail) {
		this.sendusermail = sendusermail;
	}

	public String getGetusermail() {
		return getusermail;
	}

	public void setGetusermail(String getusermail) {
		this.getusermail = getusermail;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getImagepath() {
		return imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}

	public String getDmmessageday() {
		return dmmessageday;
	}

	public void setDmmessageday(String dmmessageday) {
		this.dmmessageday = dmmessageday;
	}

	public DmRoomEntity getDmroom() {
		return dmroom;
	}

	public void setDmroom(DmRoomEntity dmroom) {
		this.dmroom = dmroom;
	}

}