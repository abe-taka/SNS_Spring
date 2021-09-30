package com.example.demo.artist_register;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.artist.ArtistEntity;

@Repository
public interface Artist_RegisterRepository extends JpaRepository<Artist_RegisterEntity,Integer>{
	
	//ユーザーidを基に検索
	List<Artist_RegisterEntity> findByUserid(int userid);
	
	//登録idを基に検索
	Artist_RegisterEntity findByRegisterid(int regid);
	
	//指定なしを挿入
	Artist_RegisterEntity findByUseridAndArtist(int regid,ArtistEntity artistEntity);
}