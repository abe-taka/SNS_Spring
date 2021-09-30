package com.example.demo.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.chat.ChatSendEntity;

@Repository
public interface CommentSendRepository extends JpaRepository<CommentSendEntity,Integer>{
	
	//送信されたユーザidを基に投稿テーブルの投稿idを取得＋コメントテーブルの外部キーにその投稿idがあれば取得する
	//「nativeQuery = true」 ： JPQLからSQL文になる
	//JPQL:エンティティを対象にしたクエリ。検索結果はテーブルのレコードではなく、エンティティやそのコレクションが取得される。
	@Query(value = "SELECT comment_id,y.user_id,y.sentence,comment_day,y.chat_send_id,y.commentimg_path FROM chat_send_table x ,comment_table y WHERE x.user_id = :userid AND x.chat_send_id = y.chat_send_id",nativeQuery = true)
	List<CommentSendEntity> findByUserid(@Param("userid") Integer userid);
	
	List<CommentSendEntity> findByChat(ChatSendEntity chatSendEntity);
}