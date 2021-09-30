package com.example.demo.artist;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.account.AccountEntity;
import com.example.demo.artist_register.Artist_RegisterEntity;
import com.example.demo.artist_register.Artist_RegisterRepository;
import com.example.demo.chat.ChatSendEntity;
import com.example.demo.chat.ChatSendRepository;
import com.example.demo.components.Account_manage;
import com.example.demo.components.Image_manage;
import com.example.demo.components.Session_manage;

@Controller
public class ArtistController {

	@Autowired
	Artist_RegisterRepository artist_RegisterRepository;
	@Autowired
	Account_manage account_manage;
	@Autowired
	Session_manage session_manage;
	@Autowired
	Image_manage img_manage;
	@Autowired
	ArtistRepository artistRepository;
	@Autowired
	ChatSendRepository chatSendRepository;

	// セッションで保持したメールアドレス
	private String mailaddress;

	// アカウントテーブルから取得してきたユーザーid
	private static int userid;

	// アカウントデータ取得
	private static AccountEntity accountEntity;

	@RequestMapping(value = "/artist_room/search_room", method = RequestMethod.GET)
	public String Get_Search_Room(Model model) {

		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);
		
		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// 登録しているアーティスト情報を取得
		userid = accountEntity.getUserid();

		// ユーザーidを基に検索
		List<Artist_RegisterEntity> List_Artist_RegisterEntity = artist_RegisterRepository.findByUserid(userid);

		// アーティスト画像を取得
		for (int i = 0; i < List_Artist_RegisterEntity.size(); i++) {

			// 指定なしを削除
			if (List_Artist_RegisterEntity.get(i).getArtist().getArtistid() == 1) {
				List_Artist_RegisterEntity.remove(i);
			}
			if (List_Artist_RegisterEntity.size() != 0) {

				if (List_Artist_RegisterEntity.get(i).getArtist().getArtistimgpath() != null) {
					// DBに保存しているパスをString型に変換
					String dbcommentpath = null;
					dbcommentpath = List_Artist_RegisterEntity.get(i).getArtist().getArtistimgpath();
					// 画像データを取得
					String imgpath = null;
					imgpath = img_manage.Get_ChatSendImage(dbcommentpath);
					// 取得してきた画像データをEntityにセットする
					List_Artist_RegisterEntity.get(i).getArtist().setArtistimgpath(imgpath);

				} else {
					System.out.println("登録していません");
				}
			}
		}

		model.addAttribute("List_Artist_RegisterEntity", List_Artist_RegisterEntity);
		model.addAttribute("artistForm", new ArtistForm());

		return "/artist_room/search_room";
	}

	@RequestMapping(value = "/artist_room/room", method = RequestMethod.POST)
	public String Post_Room(Model model, ArtistForm artistForm) {
		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);
		
		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		/*--- アーティスト情報を取得 ---*/
		ArtistEntity artistEntity = artistRepository.findByArtistid(artistForm.getArtistid());
		// DBに保存しているパスをString型に変換
		String dbcommentpath = null;
		dbcommentpath = artistEntity.getArtistimgpath();

		// 画像データを取得
		String artistimgdata = null;
		artistimgdata = img_manage.Get_ChatSendImage(dbcommentpath);
		// 取得してきた画像データをEntityにセットする
		artistEntity.setArtistimgpath(artistimgdata);

		model.addAttribute("artistEntity", artistEntity);

		/*--- 投稿情報を取得 ---*/
		List<ChatSendEntity> chat_list = chatSendRepository.findByArtist(artistEntity);

		// DBに格納しているパスを基に画像を取得(アカウント画像、投稿画像)
		for (int i = 0; i < chat_list.size(); i++) {
			// アカウント画像
			String db_accmail = null;
			String get_accimgdata = null;
			db_accmail = chat_list.get(i).getAccount().getMail();
			get_accimgdata = img_manage.Response_Get_AccountImage(db_accmail);
			chat_list.get(i).getAccount().setAccountimgpath(get_accimgdata);

			// 「画像パス1」：値が入っていたら画像を取得
			if (chat_list.get(i).getChatimgpath1() != null) {
				// DBに保存しているパスをString型に変換
				String dbpath1 = null;
				dbpath1 = chat_list.get(i).getChatimgpath1();
				// 画像データを取得
				String imgpath = null;
				imgpath = img_manage.Get_ChatSendImage(dbpath1);
				// 取得してきた画像データをEntityにセットする
				chat_list.get(i).setChatimgpath1(null);
				chat_list.get(i).setChatimgpath1(imgpath);
			}

			// 「画像パス2」：値が入っていたら画像を取得
			if (chat_list.get(i).getChatimgpath2() != null) {
				// DBに保存しているパスをString型に変換
				String dbpath2 = null;
				dbpath2 = chat_list.get(i).getChatimgpath2();
				// 画像データを取得
				String imgpath2 = null;
				imgpath2 = img_manage.Get_ChatSendImage(dbpath2);
				// 取得してきた画像データをEntityにセットする
				chat_list.get(i).setChatimgpath2(null);
				chat_list.get(i).setChatimgpath2(imgpath2);
			}

			// 「画像パス3」：値が入っていたら画像を取得
			if (chat_list.get(i).getChatimgpath3() != null) {
				// DBに保存しているパスをString型に変換
				String dbpath3 = null;
				dbpath3 = chat_list.get(i).getChatimgpath3();
				// 画像データを取得
				String imgpath3 = null;
				imgpath3 = img_manage.Get_ChatSendImage(dbpath3);
				// 取得してきた画像データをEntityにセットする
				chat_list.get(i).setChatimgpath3(null);
				chat_list.get(i).setChatimgpath3(imgpath3);
			}

			// 「画像パス4」：値が入っていたら画像を取得
			if (chat_list.get(i).getChatimgpath4() != null) {
				// DBに保存しているパスをString型に変換
				String dbpath4 = null;
				dbpath4 = chat_list.get(i).getChatimgpath4();
				// 画像データを取得
				String imgpath4 = null;
				imgpath4 = img_manage.Get_ChatSendImage(dbpath4);
				// 取得してきた画像データをEntityにセットする
				chat_list.get(i).setChatimgpath4(null);
				chat_list.get(i).setChatimgpath4(imgpath4);
			}
		}

		// 表示
		model.addAttribute("getchatsendEntity", chat_list);

		return "/artist_room/room";
	}
}