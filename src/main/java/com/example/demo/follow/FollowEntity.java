package com.example.demo.follow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

import com.example.demo.account.AccountEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name= "follow_table")
public class FollowEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "follow_id")
	private int followid;
	
	@ManyToOne
	@JsonBackReference("Unit4")
	@JoinColumn(name="user_id")
	
	private AccountEntity followeraccount;
	
	@ManyToOne
	@JsonBackReference("Unit5")
	@JoinColumn(name="follow_user_id")
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