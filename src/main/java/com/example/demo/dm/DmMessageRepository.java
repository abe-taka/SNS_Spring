package com.example.demo.dm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.comment.CommentSendEntity;

@Repository
public interface DmMessageRepository extends JpaRepository<DmMessageEntity,Integer>{
	//roomidの受け取り
	public List<DmMessageEntity> findByDmroom(DmRoomEntity dmroom);
}