package com.example.demo.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.example.demo.account.AccountEntity;
import com.example.demo.chat.ChatSendEntity;
import com.example.demo.chat.ChatSendRepository;
import com.example.demo.comment.CommentSendEntity;

@Component
public class Chat_manage {

	@Autowired
	ChatSendRepository chatSendRepository;
	@Autowired
	Image_manage img_manage;
	// 投稿テーブルのデータ取得
	private List<ChatSendEntity> getchatsendEntity;

	// 投稿データ取得(アカウントページ)
	public void Get_ChatSendData(Model model, int userid, AccountEntity accountEntity) {
		getchatsendEntity = chatSendRepository.findByAccount(accountEntity);
		
		model.addAttribute("getchatsendEntity", getchatsendEntity);
		
	}

	// 新投稿データ取得(アカウントページ)
	public List<ChatSendEntity> Return_ChatSendData(Model model, int userid, AccountEntity accountEntity) {
		userid = accountEntity.getUserid();
		getchatsendEntity = chatSendRepository.findByAccount(accountEntity);
		// model.addAttribute("getchatsendEntity", getchatsendEntity);
		return getchatsendEntity;
	}

	// 投稿データ取得(チャットページ:FF内)
	public List<ChatSendEntity> Get_FollowChatData(List<ChatSendEntity> getchatsendEntity, int userid) {
		getchatsendEntity = chatSendRepository.findByFollowChatsendid(userid);

		// DBに格納しているパスを基に画像を取得
		for (int i = 0; i < getchatsendEntity.size(); i++) {
			//chatsendid_list.add(chat_list.get(i).getChatsendid());
			// 「画像パス1」：値が入っていたら画像を取得
			if (getchatsendEntity.get(i).getChatimgpath1() != null) {
				// DBに保存しているパスをString型に変換
				String dbpath1 = null;
				dbpath1 = getchatsendEntity.get(i).getChatimgpath1();
				// 画像データを取得
				String imgpath = null;
				imgpath = img_manage.Get_ChatSendImage(dbpath1);
				// 取得してきた画像データをEntityにセットする
				getchatsendEntity.get(i).setChatimgpath1(null);
				getchatsendEntity.get(i).setChatimgpath1(imgpath);
			}

			// 「画像パス2」：値が入っていたら画像を取得
			if (getchatsendEntity.get(i).getChatimgpath2() != null) {
				// DBに保存しているパスをString型に変換
				String dbpath2 = null;
				dbpath2 = getchatsendEntity.get(i).getChatimgpath2();
				// 画像データを取得
				String imgpath2 = null;
				imgpath2 = img_manage.Get_ChatSendImage(dbpath2);
				// 取得してきた画像データをEntityにセットする
				getchatsendEntity.get(i).setChatimgpath2(null);
				getchatsendEntity.get(i).setChatimgpath2(imgpath2);
			}

			// 「画像パス3」：値が入っていたら画像を取得
			if (getchatsendEntity.get(i).getChatimgpath3() != null) {
				// DBに保存しているパスをString型に変換
				String dbpath3 = null;
				dbpath3 = getchatsendEntity.get(i).getChatimgpath3();
				// 画像データを取得
				String imgpath3 = null;
				imgpath3 = img_manage.Get_ChatSendImage(dbpath3);
				// 取得してきた画像データをEntityにセットする
				getchatsendEntity.get(i).setChatimgpath3(null);
				getchatsendEntity.get(i).setChatimgpath3(imgpath3);
			}

			// 「画像パス4」：値が入っていたら画像を取得
			if (getchatsendEntity.get(i).getChatimgpath4() != null) {
				// DBに保存しているパスをString型に変換
				String dbpath4 = null;
				dbpath4 = getchatsendEntity.get(i).getChatimgpath4();
				// 画像データを取得
				String imgpath4 = null;
				imgpath4 = img_manage.Get_ChatSendImage(dbpath4);
				// 取得してきた画像データをEntityにセットする
				getchatsendEntity.get(i).setChatimgpath4(null);
				getchatsendEntity.get(i).setChatimgpath4(imgpath4);
			}

		}

		return getchatsendEntity;
		//model.addAttribute("getchatsendEntity", getchatsendEntity);
	}

}
