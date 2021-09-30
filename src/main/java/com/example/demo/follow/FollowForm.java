package com.example.demo.follow;

import com.example.demo.account.AccountEntity;

public class FollowForm {

	private int followid;
	
	//フォローされたユーザー
	private AccountEntity followeraccount;
	
	//フォローしたユーザー
	private AccountEntity followaccount;

	//ゲッター、セッター
	public int getFollowid() {
		return followid;
	}

	public void setFollowid(int followid) {
		this.followid = followid;
	}

	public AccountEntity getFolloweraccount() {
		return followeraccount;
	}

	public void setFolloweraccount(AccountEntity followeraccount) {
		this.followeraccount = followeraccount;
	}

	public AccountEntity getFollowaccount() {
		return followaccount;
	}

	public void setFollowaccount(AccountEntity followaccount) {
		this.followaccount = followaccount;
	}

}