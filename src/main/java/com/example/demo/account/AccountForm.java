package com.example.demo.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.login.LoginForm.Group1;
import com.example.demo.login.LoginForm.Group2;

public class AccountForm implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/* バリデーション優先順位をグループ化 */
	public interface Group1 {
	}

	public interface Group2 {
	}

	@GroupSequence({ Group1.class, Group2.class })
	public interface All {
	}
	
	/* id */
	private Integer userid;

	/* ニックネーム */
	@NotBlank(groups = Group1.class)
	@Size(max = 10, message = "{max}文字以内で入力してください", groups = Group2.class)
	private String nickname;

	/* メールアドレス */
	private String mail;

	/* パスワード */
	private String password;
	
	/* 画像パス */
	private String accountimgpath;

	/* 自己紹介文 */
	@Size(max = 100, message = "{max}文字以内で入力してください", groups = Group2.class)
	private String introduction;

	/* フォロー数 */
	private Integer follownum;
	
	/* フォロワー数 */
	private Integer followernum;

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

	
}
