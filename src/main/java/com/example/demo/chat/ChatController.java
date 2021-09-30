package com.example.demo.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.account.AccountEntity;
import com.example.demo.account.AccountRepository;
import com.example.demo.artist.ArtistEntity;
import com.example.demo.artist.ArtistRepository;
import com.example.demo.artist_register.Artist_RegisterEntity;
import com.example.demo.artist_register.Artist_RegisterRepository;
import com.example.demo.comment.CommentSendEntity;
import com.example.demo.comment.CommentSendForm;
import com.example.demo.comment.CommentSendRepository;
import com.example.demo.components.Account_manage;
import com.example.demo.components.Chat_manage;
import com.example.demo.components.Image_manage;
import com.example.demo.components.Session_manage;
import com.example.demo.follow.FollowRepository;
import com.example.demo.follow.FollowUserListForm;

@Controller
public class ChatController {

	@Autowired
	Session_manage session_manage;
	@Autowired
	Account_manage account_manage;
	@Autowired
	Chat_manage chat_manage;
	@Autowired
	Image_manage img_manage;
	@Autowired
	FollowRepository followRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ChatSendRepository chatSendRepository;
	@Autowired
	CommentSendRepository commentSendRepository;
	@Autowired
	ArtistRepository artistRepository;
	@Autowired
	Artist_RegisterRepository artist_RegisterRepository;

	// セッションで保持したメールアドレス
	private String mailaddress;
	// アカウントデータ取得
	private AccountEntity accountEntity;
	// 投稿テーブルのデータ取得
	private List<ChatSendEntity> getchatsendEntity;

	private List<FollowUserListForm> f_listForm;

	// トップページ(POST)
	@RequestMapping(value = "/chat/chat", method = RequestMethod.POST)
	public String Chat_process(Model model) {
		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);
		
		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		/*-- フォルダの生成 --*/
		// コメント用
		img_manage.Create_CommentFolder(mailaddress);
		
		int flag = 0;
		
		// 自分のアカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);
		int userid = accountEntity.getUserid();

		/*-- 指定なしをinsertする　--*/
		ArtistEntity artistEntity =  artistRepository.findByArtistid(1);
		Artist_RegisterEntity artist_RegisterEntity = artist_RegisterRepository.findByUseridAndArtist(userid,artistEntity);
		if(artist_RegisterEntity == null) {
			Artist_RegisterEntity save_RegisterEntity = new Artist_RegisterEntity();
			save_RegisterEntity.setUserid(userid);
			save_RegisterEntity.setArtist(artistEntity);
			artist_RegisterRepository.save(save_RegisterEntity);
		}
			
		/*--フォローユーザーの投稿データを取得--*/
		List<ChatSendEntity> chat_list = chatSendRepository.findByFollowChatsendid(userid);
		// DBに格納しているパスを基に画像を取得(アカウント画像、投稿画像)
		for (int i = 0; i < chat_list.size(); i++) {
			flag = 1;
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

			// アーティスト画像
			if (chat_list.get(i).getArtist().getArtistimgpath() != null) {
				String dbpath5 = null;
				dbpath5 = chat_list.get(i).getArtist().getArtistimgpath();
				System.out.println("dbpath5" + dbpath5);
				String imgdata5 = null;
				imgdata5 = img_manage.Get_ChatSendImage(dbpath5);
				chat_list.get(i).getArtist().setArtistimgpath(imgdata5);
				System.out.println("imgdata5" + imgdata5);
			}
		}

		// コメントのデータを取得
		List<CommentSendEntity> comment_list = commentSendRepository.findByUserid(userid);
		model.addAttribute("comment_list", comment_list);

		// 表示
		model.addAttribute("getchatsendEntity", chat_list);

		// 詳細ページ用
		model.addAttribute("chatsendForm", new ChatSendForm());
		
		model.addAttribute("flag", flag);

		return "/chat/chat";
	}

	// トップページ(GET)
	@RequestMapping(value = "/chat/chat", method = RequestMethod.GET)
	public String Account_Top(Model model) {
		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);
		
		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// 自分のアカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);
		int userid = accountEntity.getUserid();
		
		int flag = 0;

		/*--フォローユーザーの投稿データを取得--*/
		List<ChatSendEntity> chat_list = chatSendRepository.findByFollowChatsendid(userid);
		// DBに格納しているパスを基に画像を取得(アカウント画像、投稿画像)
		for (int i = 0; i < chat_list.size(); i++) {
			flag = 1;
			
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

			// アーティスト画像
			if (chat_list.get(i).getArtist().getArtistimgpath() != null) {
				String dbpath5 = null;
				dbpath5 = chat_list.get(i).getArtist().getArtistimgpath();
				System.out.println("dbpath5" + dbpath5);
				String imgdata5 = null;
				imgdata5 = img_manage.Get_ChatSendImage(dbpath5);
				chat_list.get(i).getArtist().setArtistimgpath(imgdata5);
				System.out.println("imgdata5" + imgdata5);
			}
		}

		// 表示
		model.addAttribute("getchatsendEntity", chat_list);

		// 詳細ページ用
		model.addAttribute("chatsendForm", new ChatSendForm());
		
		model.addAttribute("flag", flag);

		return "/chat/chat";
	}

	// 投稿詳細ページ(POST)
	@RequestMapping(value = "/chat/chat_details", method = RequestMethod.POST)
	public String Post_ChatDetails(Model model, ChatSendForm chatsendForm) {
		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);
		
		// アカウント画像を取得
		accountEntity = account_manage.Get_AccountData2(mailaddress,accountEntity);
		model.addAttribute("accountEntity",accountEntity);
		
		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// 投稿idを取得
		System.out.println("chatsendForm.getChatsendid()" + chatsendForm.getChatsendid());
		int id = chatsendForm.getChatsendid();
		System.out.println("id" + id);

		// idを基に情報を取得
		ChatSendEntity chatsendEntity = chatSendRepository.findByChatsendid(id);
		System.out.println("chatsendEntity" + chatsendEntity);

		// アカウントの画像データを取得
		String imgdata = null;
		imgdata = img_manage.Response_Get_AccountImage(chatsendEntity.getAccount().getMail());
		System.out.println("imgdata" + imgdata);
		chatsendEntity.getAccount().setAccountimgpath(imgdata);

		// 投稿の画像データを取得
		// 「画像パス1」：値が入っていたら画像を取得
		if (chatsendEntity.getChatimgpath1() != null) {
			// DBに保存しているパスをString型に変換
			String dbpath1 = null;
			dbpath1 = chatsendEntity.getChatimgpath1();
			// 画像データを取得
			String imgpath = null;
			imgpath = img_manage.Get_ChatSendImage(dbpath1);
			// 取得してきた画像データをEntityにセットする
			chatsendEntity.setChatimgpath1(imgpath);
		}

		// 「画像パス2」：値が入っていたら画像を取得
		if (chatsendEntity.getChatimgpath2() != null) {
			// DBに保存しているパスをString型に変換
			String dbpath2 = null;
			dbpath2 = chatsendEntity.getChatimgpath2();
			// 画像データを取得
			String imgpath = null;
			imgpath = img_manage.Get_ChatSendImage(dbpath2);
			// 取得してきた画像データをEntityにセットする
			chatsendEntity.setChatimgpath2(imgpath);
		}

		// 「画像パス3」：値が入っていたら画像を取得
		if (chatsendEntity.getChatimgpath3() != null) {
			// DBに保存しているパスをString型に変換
			String dbpath3 = null;
			dbpath3 = chatsendEntity.getChatimgpath3();
			// 画像データを取得
			String imgpath = null;
			imgpath = img_manage.Get_ChatSendImage(dbpath3);
			// 取得してきた画像データをEntityにセットする
			chatsendEntity.setChatimgpath3(imgpath);
		}

		// 「画像パス4」：値が入っていたら画像を取得
		if (chatsendEntity.getChatimgpath4() != null) {
			// DBに保存しているパスをString型に変換
			String dbpath4 = null;
			dbpath4 = chatsendEntity.getChatimgpath1();
			// 画像データを取得
			String imgpath = null;
			imgpath = img_manage.Get_ChatSendImage(dbpath4);
			// 取得してきた画像データをEntityにセットする
			chatsendEntity.setChatimgpath4(imgpath);
		}

		// 投稿データを渡す
		model.addAttribute("chatsendEntity", chatsendEntity);

		// コメントを取得
		List<CommentSendEntity> comment_list = commentSendRepository.findByChat(chatsendEntity);
		System.out.println("comment_list" + comment_list);
		for (int i = 0; i < comment_list.size(); i++) {
			// アカウント画像データ
			if (comment_list.get(i).getAccount().getAccountimgpath() != null) {
				String img_data = null;
				img_data = img_manage.Response_Get_AccountImage(comment_list.get(i).getAccount().getMail());
				comment_list.get(i).getAccount().setAccountimgpath(img_data);
			}

			// コメント画像データ
			if (comment_list.get(i).getCommentimgpath() != null) {
				String dbimgpath = null;
				dbimgpath = comment_list.get(i).getCommentimgpath();
				String img_data = null;
				img_data = img_manage.Get_ChatSendImage(dbimgpath);
				comment_list.get(i).setCommentimgpath(img_data);
			}
		}

		// コメントをデータを渡す
		model.addAttribute("comment_list", comment_list);
		
		//コメント用
		model.addAttribute("commentForm", new CommentSendForm());

		return "/chat/chat_details";
	}

	// 投稿詳細ページ(GET)
	@RequestMapping(value = "/chat/chat_details", method = RequestMethod.GET)
	public String Get_ChatDetails(Model model) {

		return "/chat/chat_details";
	}

}