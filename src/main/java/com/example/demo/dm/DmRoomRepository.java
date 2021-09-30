package com.example.demo.dm;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.account.AccountEntity;

@Repository
public interface DmRoomRepository extends JpaRepository<DmRoomEntity,Integer>{

	//@Paramの変数名と引数の変数名は統一にする
	@Query(value = "SELECT dm_room_id,yuan_userid,guest_userid FROM dm_room_table WHERE (yuan_userid = :yuan OR guest_userid = :yuan) AND (yuan_userid = :guest OR guest_userid = :guest)",nativeQuery = true)
	DmRoomEntity findByYuanaccountANDGuestaccount(@Param("yuan") AccountEntity yuanaccount,@Param("guest")AccountEntity guestaccount);
	
	//DMidで検索
	public DmRoomEntity findByDmroomid(int dmrooid);
	
	//ログインユーザーが関わるルームを取得
	public List<DmRoomEntity> findByYuanaccountOrGuestaccount(AccountEntity yuanaccount,AccountEntity guestaccount);
}