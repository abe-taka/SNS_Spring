package com.example.demo.dm;

import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.demo.account.AccountEntity;

public class DmRoomForm {

	// DMルームid
	private int dmroomid;

	// DM元ユーザーメール
	private AccountEntity yuanaccount;

	// DM先ユーザーメール
	private AccountEntity guestaccount;
	
	//一対多の「一」
	private List<DmMessageEntity> dmmessageEntity;

	//　ゲッター、セッター
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
