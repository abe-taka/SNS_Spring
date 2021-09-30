package com.example.demo.dm;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class DmMessageForm {

	// DMルームid
	private int dmmessageid;

	// 送信ユーザー
	private String sendusermail;

	// 受け取りユーザー
	private String getusermail;
	
	// 文章
	private String sentence;
	
	// 画像パス
	private String imagepath;

	// コメント日
	private String dmmessageday;

	// 一対多の「多」
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
