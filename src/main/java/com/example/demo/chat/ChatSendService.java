package com.example.demo.chat;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//ロールバックを定義(トランザクション処理が途中で途絶えた場合に取り消す)
//(rollbackOn = Exception.class):色んなエラー処理に対してロールバックをするための設定(@Transactionalだけだとしてくれない場合がある)
@Transactional(rollbackOn = Exception.class)
public class ChatSendService {

	@Autowired
	ChatSendRepository chatSendRepository;

	// 保存処理
	public void save(ChatSendEntity chatSendEntity) {
		chatSendRepository.save(chatSendEntity);
	}
}
