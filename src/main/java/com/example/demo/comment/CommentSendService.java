package com.example.demo.comment;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
public class CommentSendService {

	@Autowired
	CommentSendRepository csRepository;

	public void save(CommentSendEntity commentSendEntity) {
		csRepository.save(commentSendEntity);
	}
}
