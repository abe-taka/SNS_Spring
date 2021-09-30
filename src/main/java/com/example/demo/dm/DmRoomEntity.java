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

import com.example.demo.account.AccountEntity;
import com.example.demo.comment.CommentSendEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "dm_room_table")
public class DmRoomEntity {

	// DMルームid
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dm_room_id")
	private int dmroomid;

	// DM元ユーザーメール
	@ManyToOne
	@JoinColumn(name = "yuan_userid")
	@JsonBackReference("Unit6")
	private AccountEntity yuanaccount;

	// DM先ユーザーメール
	@ManyToOne
	@JoinColumn(name = "guest_userid")
	@JsonBackReference("Unit7")
	private AccountEntity guestaccount;

	// 一対多の「一」
	@OneToMany(mappedBy = "dmroom")
	@JsonManagedReference("Unit2")
	private List<DmMessageEntity> dmmessageEntity;

	// ゲッター、セッター
	public int getDmroomid() {
		return dmroomid;
	}

	public void setDmroomid(int dmroomid) {
		this.dmroomid = dmroomid;
	}

	public AccountEntity getYuanaccount() {
		return yuanaccount;
	}

	public void setYuanaccount(AccountEntity yuanaccount) {
		this.yuanaccount = yuanaccount;
	}

	public AccountEntity getGuestaccount() {
		return guestaccount;
	}

	public void setGuestaccount(AccountEntity guestaccount) {
		this.guestaccount = guestaccount;
	}

	public List<DmMessageEntity> getDmmessageEntity() {
		return dmmessageEntity;
	}

	public void setDmmessageEntity(List<DmMessageEntity> dmmessageEntity) {
		this.dmmessageEntity = dmmessageEntity;
	}

}