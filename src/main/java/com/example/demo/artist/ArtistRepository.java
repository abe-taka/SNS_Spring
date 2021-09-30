package com.example.demo.artist;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity,Integer>{
	//登録数が多い10件を取得
	@Query(value="select * from artist_table order by registration_num desc, artist_id asc LIMIT 10",nativeQuery=true)
	List<ArtistEntity> queryAll();
	
	//アーティスト名検索
	ArtistEntity findByArtistname(String artistname);
	
	//アーティストidを基に検索
	ArtistEntity findByArtistid(int artistid);
}