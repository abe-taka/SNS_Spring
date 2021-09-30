package com.example.demo.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.account.AccountEntity;
import com.example.demo.artist.ArtistEntity;


@Repository
public interface ChatSendRepository extends JpaRepository<ChatSendEntity, Integer> {

	// 特定のユーザーidの投稿データを取得
	public List<ChatSendEntity> findByAccount(AccountEntity accountEntity);
	//public ChatSendEntity findByUserid(String userid);
	
	// コメントの外部キー用
	public ChatSendEntity findByChatsendid(Integer chatsendid);
	
	//アーティストidを基に検索
	public List<ChatSendEntity> findByArtist(ArtistEntity ar);

	// フォローユーザーのみの投稿データを取得
	@Query(value = "SELECT chat_send_id,c.user_id,sentence,chat_send_day,chatimg_path1,chatimg_path2,chatimg_path3,chatimg_path4,artist_id FROM chat_send_table c ,follow_table f WHERE f.follow_user_id = :userid AND c.user_id = f.user_id", nativeQuery = true)
	List<ChatSendEntity> findByFollowChatsendid(@Param("userid") Integer userid);
}