package com.example.demo.account;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.demo.chat.ChatSendEntity;
import com.example.demo.comment.CommentSendEntity;
import com.example.demo.dm.DmRoomEntity;
import com.example.demo.follow.FollowEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "account_table")
public class AccountEntity {

	/* id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userid;

	/* ニックネーム */
	@Column(unique = true)
	private String nickname;

	/* メールアドレス */
	@Column(name = "mailaddress")
	private String mail;

	/* パスワード */
	private String password;

	/* 画像パス */
	@Column(name = "accountimg_path")
	private String accountimgpath;

	/* 自己紹介文 */
	private String introduction;

	/* フォロー数 */
	@Column(name = "follow_num")
	private Integer follownum;

	/* フォロワー数 */
	@Column(name = "follower_num")
	private Integer followernum;

	// ChatSendEntityとの外部キー設定
	@OneToMany(mappedBy = "account")
	@JsonBackReference("Unit2")
	private List<ChatSendEntity> chatSendEntity;

	// CommentSendEntityとの外部キー設定
	@OneToMany(mappedBy = "account")
	@JsonBackReference("Unit3")
	private List<CommentSendEntity> commentsendEntity;

	// FollowEntityとの外部キー設定
	@OneToMany(mappedBy = "followeraccount")
	@JsonBackReference("Unit4")
	private List<FollowEntity> followEntity_getfolloweruser;

	// FollowEntityとの外部キー設定
	@OneToMany(mappedBy = "followaccount")
	@JsonBackReference("Unit5")
	private List<FollowEntity> followaccount_getfollowuser;

	// FollowEntityとの外部キー設定
	@OneToMany(mappedBy = "yuanaccount")
	@JsonBackReference("Unit6")
	private List<DmRoomEntity> YuanDm;

	// FollowEntityとの外部キー設定
	@OneToMany(mappedBy = "guestaccount")
	@JsonBackReference("Unit7")
	private List<DmRoomEntity> GuestDm;

	/* ゲッター、セッター */
	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccountimgpath() {
		return accountimgpath;
	}

	public void setAccountimgpath(String accountimgpath) {
		this.accountimgpath = accountimgpath;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Integer getFollownum() {
		return follownum;
	}

	public void setFollownum(Integer follownum) {
		this.follownum = follownum;
	}

	public Integer getFollowernum() {
		return followernum;
	}

	public void setFollowernum(Integer followernum) {
		this.followernum = followernum;
	}

	public List<ChatSendEntity> getChatSendEntity() {
		return chatSendEntity;
	}

	public void setChatSendEntity(List<ChatSendEntity> chatSendEntity) {
		this.chatSendEntity = chatSendEntity;
	}

	public List<CommentSendEntity> getCommentsendEntity() {
		return commentsendEntity;
	}

	public void setCommentsendEntity(List<CommentSendEntity> commentsendEntity) {
		this.commentsendEntity = commentsendEntity;
	}

	public List<FollowEntity> getFollowEntity_getfolloweruser() {
		return followEntity_getfolloweruser;
	}

	public void setFollowEntity_getfolloweruser(List<FollowEntity> followEntity_getfolloweruser) {
		this.followEntity_getfolloweruser = followEntity_getfolloweruser;
	}

	public List<FollowEntity> getFollowaccount_getfollowuser() {
		return followaccount_getfollowuser;
	}

	public void setFollowaccount_getfollowuser(List<FollowEntity> followaccount_getfollowuser) {
		this.followaccount_getfollowuser = followaccount_getfollowuser;
	}

}
