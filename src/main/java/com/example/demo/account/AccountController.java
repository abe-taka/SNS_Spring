package com.example.demo.account;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.example.demo.artist.ArtistEntity;
import com.example.demo.artist.ArtistForm;
import com.example.demo.artist.ArtistRepository;
import com.example.demo.artist_register.Artist_RegisterEntity;
import com.example.demo.artist_register.Artist_RegisterForm;
import com.example.demo.artist_register.Artist_RegisterRepository;
import com.example.demo.chat.ChatSendEntity;
import com.example.demo.chat.ChatSendForm;
import com.example.demo.chat.ChatSendRepository;
import com.example.demo.chat.ChatSendService;
import com.example.demo.comment.CommentSendEntity;
import com.example.demo.comment.CommentSendForm;
import com.example.demo.comment.CommentSendRepository;
import com.example.demo.components.Account_manage;
import com.example.demo.components.Chat_manage;
import com.example.demo.components.Image_manage;
import com.example.demo.components.Realtime_manage;
import com.example.demo.components.Session_manage;
import com.example.demo.dm.DmMessageEntity;
import com.example.demo.dm.DmMessageRepository;
import com.example.demo.dm.DmRoomEntity;
import com.example.demo.dm.DmRoomRepository;
import com.example.demo.follow.FollowEntity;
import com.example.demo.follow.FollowForm;
import com.example.demo.follow.FollowRepository;
import com.example.demo.message.MessageEntity;
import com.example.demo.message.MessageRepository;
import com.example.demo.nice.Chat_NiceEntity;
import com.example.demo.nice.Chat_NiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class AccountController {

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ChatSendRepository chatSendRepository;
	@Autowired
	ChatSendService chatSendService;
	@Autowired
	CommentSendRepository commentSendRepository;
	@Autowired
	FollowRepository followRepository;
	@Autowired
	ArtistRepository artistRepository;
	@Autowired
	Artist_RegisterRepository artist_RegisterRepository;
	@Autowired
	DmRoomRepository dmRoomRepository;
	@Autowired
	DmMessageRepository dmMessagerepository;
	@Autowired
	Chat_NiceRepository chat_NiceRepository;
	@Autowired
	MessageRepository messageRepository;

	// Components
	@Autowired
	Image_manage img_manage;
	@Autowired
	Account_manage account_manage;
	@Autowired
	Session_manage session_manage;
	@Autowired
	Chat_manage chat_manage;
	@Autowired
	Realtime_manage time_manage;

	// セッションで保持したメールアドレス
	private String mailaddress;

	// 検索したユーザーのメールアドレス
	private static String str = null;

	// アカウントデータ取得
	private static AccountEntity accountEntity;

	// アカウント画像取得
	private static String uploadPath;

	// アカウントテーブルから取得してきたユーザーid
	private static int userid;

	// 現在時刻の取得
	private String real_time;

	// コメントいているユーザーのアカウント画像取得
	List<String> comment_img = new ArrayList<String>();

	// GETかPOSTの区別フラグ(アーティスト検索)
	private int artist_flg = 0;

	// 登録アーティストid(GET用)
	private static int static_artistregid;

	// 登録アーティストがいるかをチェックするフラグ
	private int ar_flag = 0;

	private static int usersearch_userid = 0;

	// JSON文字列を返却するために、@ResponseBodyアノテーションを付与
	// 投稿データの取得
	@GetMapping("/search_chatdata")
	@ResponseBody
	public String Search_Chatdata(@RequestParam("id") String id, Model model) {
		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);
		System.out.println("aaaaa");

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);
		System.out.println("bbbbb");

		// 投稿データ受け取り
		ChatSendEntity chatsendEntity = chatSendRepository.findByChatsendid(Integer.parseInt(id));
		// 取得できなかった場合は、null値を返す
		if (chatsendEntity == null) {
			System.out.println("chatEntity_nullerror");
			return null;
		}
		System.out.println("ccccc");
		System.out.println("該当する投稿id" + chatsendEntity.getChatsendid());

		// エンコード処理用に、encode関数の引数の型(String型)に合わせる
		// 投稿id
		String str_chatid = String.valueOf(chatsendEntity.getChatsendid());
		// ユーザーid
		String str_userid = String.valueOf(chatsendEntity.getAccount().getUserid());
		// アーティストid
		// String str_artistid = String.valueOf(chatsendEntity.getArtistid());

		// encode処理を行い、セットする
		// 投稿id
		chatsendEntity.setChatsendid(Integer.parseInt(encode(str_chatid)));
		// ユーザーid
		chatsendEntity.getAccount().setUserid(Integer.parseInt(encode(str_userid)));
		// 文章
		chatsendEntity.setSentence(encode(chatsendEntity.getSentence()));
		// 投稿日
		chatsendEntity.setChatsendday(encode(chatsendEntity.getChatsendday()));
		// アーティストid
		// chatsendEntity.setArtistid(Integer.parseInt(encode(str_artistid)));
		// 画像パス１，２、３，４
		chatsendEntity.setChatimgpath1(encode(chatsendEntity.getChatimgpath1()));
		chatsendEntity.setChatimgpath2(encode(chatsendEntity.getChatimgpath2()));
		chatsendEntity.setChatimgpath3(encode(chatsendEntity.getChatimgpath3()));
		chatsendEntity.setChatimgpath4(encode(chatsendEntity.getChatimgpath4()));
		System.out.println("ddddd");

		// JSONに変換し返却
		return chat_getJson(chatsendEntity);
	}

	// アカウントデータの取得
	@GetMapping("/search_accountdata")
	@ResponseBody
	public String Search(@RequestParam("userid") String userid, Model model) {

		// アカウントデータ取得
		accountEntity = accountRepository.findByUserid(Integer.parseInt(userid));
		System.out.println("ppppp");

		// 取得できなかった場合は、null値を返す
		if (accountEntity == null) {
			System.out.println("accountEntity_nullerror");
			return null;
		}
		System.out.println("該当するアカウントid" + accountEntity.getUserid());

		// アカウント画像のデータを取得
		String accountimg_data = null;
		accountimg_data = img_manage.Response_Get_AccountImage(accountEntity.getMail());
		accountEntity.setAccountimgpath(accountimg_data);
		System.out.println("accountEntity.setAccountimgpath(accountimg_data)" + accountEntity.getAccountimgpath());

		// エンコード処理用に、encode関数の引数の型(String型)に合わせる
		// encode処理を行い、セットする
		// ニックネーム
		accountEntity.setNickname(encode(accountEntity.getNickname()));
		// アカウント画像パス
		accountEntity.setAccountimgpath(encode(accountEntity.getAccountimgpath()));

		// JSONに変換し返却
		return account_getJson(accountEntity);
	}

	// DMデータの保存、取得
	@RequestMapping(value = "/search_dmdata", method = RequestMethod.GET)
	@ResponseBody
	public String Search_DMData(@RequestParam("roomid") String roomid, @RequestParam("yuanMail") String yuanmail,
			@RequestParam("guestMail") String guestmail, @RequestParam("sentence") String sentence) {

		System.out.println("*************");
		// Entityの変数を用意
		DmMessageEntity dm_messageEntity = new DmMessageEntity();
		try {
			// 自分のメールアドレス
			dm_messageEntity.setSendusermail(yuanmail);
			// 相手のメールアドレス
			dm_messageEntity.setGetusermail(guestmail);
			// DMルームid
			DmRoomEntity dmRoomEntity = new DmRoomEntity();
			dmRoomEntity = dmRoomRepository.findByDmroomid(Integer.parseInt(roomid));
			dm_messageEntity.setDmroom(dmRoomEntity);
			// 文章
			dm_messageEntity.setSentence(sentence);
			// 時間
			real_time = time_manage.Realtime_process(real_time);
			dm_messageEntity.setDmmessageday(real_time);
			// insert
			dmMessagerepository.save(dm_messageEntity);

			// エンコード処理用に、encode関数の引数の型(String型)に合わせる
			// encode処理を行い、セットする
			// 文章
			dm_messageEntity.setSentence(sentence);
			// 時間
			dm_messageEntity.setDmmessageday(real_time);

		} catch (Exception e) {
			System.out.println("DMメッセージ保存、表示処理失敗");
		}

		// JSONに変換し返却
		return dm_getJson(dm_messageEntity);
	}

	// アカウントデータの取得
	@GetMapping("/search_commentdata")
	@ResponseBody
	public String Search_CommentData(@RequestParam("chatsendid") String chatsendid) {

		// 取得できなかった場合は、null値を返す
		if (accountEntity == null) {
			System.out.println("accountEntity_nullerror");
			return null;
		}
		System.out.println("該当するアカウントid" + accountEntity.getUserid());

		// アカウント画像のデータを取得
		String accountimg_data = null;
		accountimg_data = img_manage.Response_Get_AccountImage(accountEntity.getMail());
		accountEntity.setAccountimgpath(accountimg_data);
		System.out.println("accountEntity.setAccountimgpath(accountimg_data)" + accountEntity.getAccountimgpath());

		// エンコード処理用に、encode関数の引数の型(String型)に合わせる
		// encode処理を行い、セットする
		// ニックネーム
		accountEntity.setNickname(encode(accountEntity.getNickname()));
		// アカウント画像パス
		accountEntity.setAccountimgpath(encode(accountEntity.getAccountimgpath()));
		System.out.println("rrrrr");

		// JSONに変換し返却
		return account_getJson(accountEntity);
	}

	// 良いね処理
	@RequestMapping("/save_nicedatata/{chatsendid}/{userid}")
	@ResponseBody
	public void Save_NiceData(@PathVariable int chatsendid, @PathVariable int userid) {

		Chat_NiceEntity chat_NiceEntity = new Chat_NiceEntity();
		// useridを基にアカウント情報を取得
		AccountEntity acc = accountRepository.findByUserid(userid);
		// chatsendidを基に投稿情報を取得
		ChatSendEntity chat = chatSendRepository.findByChatsendid(chatsendid);
		// いいねしているかを確認する
		chat_NiceEntity = chat_NiceRepository.findByAccountAndChat(acc, chat);

		// していない
		chat_NiceEntity.setAccount(acc);
		chat_NiceEntity.setChat(chat);

		chat_NiceRepository.save(chat_NiceEntity);
	}

	// コメントデータ保存
	@GetMapping("/save_commentdata/")
	@ResponseBody
	public String Save_CommentData(@RequestParam("userid") String userid, @RequestParam("sentence") String sentence,
			@RequestParam("chatsendid") String chatsendid) {

		// アカウントデータを取得
		AccountEntity acc = new AccountEntity();
		acc = accountRepository.findByUserid(Integer.parseInt(userid));

		// 投稿データを取得
		ChatSendEntity chat = new ChatSendEntity();
		chat = chatSendRepository.findByChatsendid(Integer.parseInt(chatsendid));

		// コメントデータを保存
		CommentSendEntity comment = new CommentSendEntity();
		comment.setAccount(acc);
		comment.setChat(chat);
		comment.setSentence(sentence);
		String realtime = null;
		realtime = time_manage.Realtime_process(realtime);
		comment.setCommentday(realtime);
		commentSendRepository.save(comment);

		// アカウント画像のデータを取得
		String comment_accountimg_data = null;
		comment_accountimg_data = img_manage.Response_Get_AccountImage(comment.getAccount().getAccountimgpath());
		comment.getAccount().setAccountimgpath(comment_accountimg_data);

		// JSONに変換し返却
		return comment_getJson(comment);
	}

	// コメントデータ保存
	@GetMapping("/save_accountdata/")
	@ResponseBody
	public String Get_AccountData(@RequestParam("userid") String userid) {

		// アカウントデータを取得
		AccountEntity acc = new AccountEntity();
		acc = accountRepository.findByUserid(Integer.parseInt(userid));

		// アカウント画像のデータを取得
		String accountimg_data = null;
		accountimg_data = img_manage.Response_Get_AccountImage(acc.getMail());
		acc.setAccountimgpath(accountimg_data);
		System.out.println("55555555555555");

		// JSONに変換し返却
		return account_getJson(acc);
	}

	// 引数の文字列をエンコードする/search_accountdata
	private String encode(String data) {
		// 引数がnullまたは空文字の場合は、その値を返す
		if (StringUtils.isEmpty(data)) {
			System.out.println("エンコード引数:nullまたは空文字");
			return data;
		}
		String retVal = null;
		try {
			retVal = URLEncoder.encode(data, "UTF-8");
			System.out.println("エンコード正常");
		} catch (UnsupportedEncodingException e) {
			System.err.println(e);
			System.out.println("エンコード失敗");
		}
		return retVal;
	}

	// [投稿データ] 引数のオブジェクトをJSON文字列に変換する
	private String chat_getJson(ChatSendEntity chatSendEntity) {
		String retVal = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			retVal = objectMapper.writeValueAsString(chatSendEntity);
			System.out.println("[投稿データ]JSON変換正常");
		} catch (JsonProcessingException e) {
			System.err.println(e);
			System.out.println("[投稿データ]JSON変換失敗");
		}
		return retVal;
	}

	// [アカウントデータ] 引数のオブジェクトをJSON文字列に変換する
	private String account_getJson(AccountEntity accountEntity) {
		String retVal = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			retVal = objectMapper.writeValueAsString(accountEntity);
			System.out.println("[アカウントデータ]JSON変換正常");
		} catch (JsonProcessingException e) {
			System.err.println(e);
			System.out.println("[アカウントデータ]JSON変換失敗");
		}
		return retVal;
	}

	// [DMデータ] 引数のオブジェクトをJSON文字列に変換する
	private String dm_getJson(DmMessageEntity dm_messageEntity) {
		String retVal = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			retVal = objectMapper.writeValueAsString(dm_messageEntity);
			System.out.println("[DMデータ]JSON変換正常");
		} catch (JsonProcessingException e) {
			System.err.println(e);
			System.out.println("[DMデータ]JSON変換失敗");
		}
		System.out.println("retVal" + retVal);
		return retVal;
	}

	// [コメントデータ] 引数のオブジェクトをJSON文字列に変換する
	private String comment_getJson(CommentSendEntity commentSendEntity) {
		String retVal = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			retVal = objectMapper.writeValueAsString(commentSendEntity);
			System.out.println("[コメントデータ]JSON変換正常");
		} catch (JsonProcessingException e) {
			System.err.println(e);
			System.out.println("[コメントデータ]JSON変換失敗");
		}
		System.out.println("retVal" + retVal);
		return retVal;
	}

	// アカウントページ(POST)
	@RequestMapping(value = "/account/account_top", method = RequestMethod.POST)
	public String Login_Auth(Model model, AccountForm accountForm, ChatSendForm chatsendForm,
			CommentSendForm commentsendForm) throws Exception {

		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// 投稿データ受け取り
		int flag = 0;
		
		List<ChatSendEntity> chat_list = chat_manage.Return_ChatSendData(model, userid, accountEntity);

		List<Integer> chatsendid_list = new ArrayList<Integer>();
		// DBに格納しているパスを基に画像を取得
		for (int i = 0; i < chat_list.size(); i++) {
			flag = 1;
			chatsendid_list.add(chat_list.get(i).getChatsendid());
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
		model.addAttribute("flag", flag);

		/*-- コメントの投稿処理 --*/
		// 日時情報を指定フォーマットの文字列で取得
		real_time = time_manage.Realtime_process(real_time);

		// コメントのデータを取得
		List<CommentSendEntity> comment_list = commentSendRepository.findByUserid(userid);
		model.addAttribute("comment_list", comment_list);

		// コメントしたユーザーのアカウント画像を取得
		for (int i = 0; i < comment_list.size(); i++) {
			// コメントをしたユーザーの情報を取得
			AccountEntity acc = accountRepository.findByUserid(comment_list.get(i).getAccount().getUserid());
			// コメントユーザーのアカウント画像を取得
			String s = img_manage.Comment_AccountImage(model, acc.getMail());
			// リストに変換
			comment_img = StringToList(s);
			acc = null;
			s = null;
			model.addAttribute("comment_img", comment_img);
		}

		// フォローボタン用
		model.addAttribute("ma", mailaddress);
		model.addAttribute("str", str);
		model.addAttribute("followForm", new FollowForm());
		// コメント用のフォーム
		model.addAttribute("commentForm", new CommentSendForm());
		// 詳細ページ用
		model.addAttribute("chatsendForm", new ChatSendForm());

		// 円形の投稿画像アイコンを用意
		img_manage.Get_MaruImage(model);

		// ユーザーidを基に登録アーティストテーブルを取得
		List<Artist_RegisterEntity> list_ar_regEntity = artist_RegisterRepository.findByUserid(userid);

		// アーティスト画像を取得
		for (int i = 0; i < list_ar_regEntity.size(); i++) {

			// 指定なしを削除
			if (list_ar_regEntity.get(i).getArtist().getArtistid() == 1) {
				list_ar_regEntity.remove(i);
			}

			// 登録しているアーティストがいるならアーティスト画像を取得
			if (list_ar_regEntity.size() != 0) {
				ar_flag = 1;

				if (list_ar_regEntity.get(i).getArtist().getArtistimgpath() != null) {
					// DBに保存しているパスをString型に変換
					String dbcommentpath = null;
					dbcommentpath = list_ar_regEntity.get(i).getArtist().getArtistimgpath();
					// 画像データを取得
					String imgpath = null;
					imgpath = img_manage.Get_ChatSendImage(dbcommentpath);
					System.out.println("imgpath" + imgpath);
					// 取得してきた画像データをEntityにセットする
					list_ar_regEntity.get(i).getArtist().setArtistimgpath(imgpath);

					// 「画像パス1」：値が入っていたら画像を取得
					if (list_ar_regEntity.get(i).getRegartistimgpath1() != null) {
						// DBに保存しているパスをString型に変換
						String dbpath1 = null;
						dbpath1 = list_ar_regEntity.get(i).getRegartistimgpath1();
						// 画像データを取得
						String imgdata1 = null;
						imgdata1 = img_manage.Get_RegisterArtistProfileImage(dbpath1);
						// 取得してきた画像データをEntityにセットする
						list_ar_regEntity.get(i).setRegartistimgpath1(imgdata1);
					}

					// 「画像パス2」：値が入っていたら画像を取得
					if (list_ar_regEntity.get(i).getRegartistimgpath2() != null) {
						// DBに保存しているパスをString型に変換
						String dbpath2 = null;
						dbpath2 = list_ar_regEntity.get(i).getRegartistimgpath2();
						// 画像データを取得
						String imgdata2 = null;
						imgdata2 = img_manage.Get_RegisterArtistProfileImage(dbpath2);
						// 取得してきた画像データをEntityにセットする
						list_ar_regEntity.get(i).setRegartistimgpath2(imgdata2);
					}

				} else {
					System.out.println("アーティスト画像登録していません");
				}

			}
			ar_flag = 0;
		}

		// 一覧データ表示用
		model.addAttribute("list_ar_regEntity", list_ar_regEntity);

		// 登録アーティスト編集
		model.addAttribute("artist_RegisterForm", new Artist_RegisterForm());

		// フォロー一覧用
		model.addAttribute("followForm", new FollowForm());

		/*-- メッセージ受け取り --*/
		// 検索
		List<MessageEntity> message_list = messageRepository.findByGetteraccount(accountEntity);
		// 送信者の画像データを取得
		for (int i = 0; i < message_list.size(); i++) {
			message_list.get(i).getSenderaccount().setAccountimgpath(
					img_manage.Response_Get_AccountImage(message_list.get(i).getSenderaccount().getMail()));
		}
		model.addAttribute("message_list", message_list);

		return "/account/account_top";
	}

	// リストに変換
	public List<String> StringToList(String str) {
		List list = new ArrayList();
		StringTokenizer tokenizer = new StringTokenizer(str, "");
		while (tokenizer.hasMoreTokens()) {
			list.add(tokenizer.nextToken());
		}
		return list;
	}

	// アカウントページ(GET)
	@RequestMapping(value = "/account/account_top", method = RequestMethod.GET)
	public String Account_Top(Model model) {

		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// 投稿データ受け取り
		int flag = 0;
		List<ChatSendEntity> chat_list = chat_manage.Return_ChatSendData(model, userid, accountEntity);

		List<Integer> chatsendid_list = new ArrayList<Integer>();
		// DBに格納しているパスを基に画像を取得
		for (int i = 0; i < chat_list.size(); i++) {
			flag = 1;
			chatsendid_list.add(chat_list.get(i).getChatsendid());
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
		model.addAttribute("flag", flag);

		// コメントのデータを取得
		userid = accountEntity.getUserid();
		List<CommentSendEntity> comment_list = commentSendRepository.findByUserid(userid);

		// パスがあれば画像データを取得
		for (int i = 0; i < comment_list.size(); i++) {
			if (comment_list.size() != 0) {

				if (comment_list.get(i).getCommentimgpath() != null) {
					// DBに保存しているパスをString型に変換
					String dbcommentpath = null;
					dbcommentpath = comment_list.get(i).getCommentimgpath();
					// 画像データを取得
					String imgpath = null;
					imgpath = img_manage.Get_ChatSendImage(dbcommentpath);

					// 取得してきた画像データをEntityにセットする
					comment_list.get(i).setCommentimgpath(null);
					comment_list.get(i).setCommentimgpath(imgpath);
				} else {

				}
			}
		}
		model.addAttribute("comment_list", comment_list);

		// コメントしたユーザーのアカウント画像を取得
		for (int i = 0; i < comment_list.size(); i++) {
			// コメントをしたユーザーの情報を取得
			AccountEntity acc = accountRepository.findByUserid(comment_list.get(i).getAccount().getUserid());
			// コメントユーザーのアカウント画像を取得
			String s = img_manage.Comment_AccountImage(model, acc.getMail());
			// リストに変換
			comment_img = StringToList(s);
			acc = null;
			s = null;
			model.addAttribute("comment_img", comment_img);
			model.addAttribute("acc", acc);
		}

		// ユーザーidを基に登録アーティストテーブルを取得
		List<Artist_RegisterEntity> list_ar_regEntity = artist_RegisterRepository.findByUserid(userid);

		// アーティスト画像、プロフィール画像を取得
		for (int i = 0; i < list_ar_regEntity.size(); i++) {

			// 指定なしを削除
			if (list_ar_regEntity.get(i).getArtist().getArtistid() == 1) {
				list_ar_regEntity.remove(i);
			}

			// 登録しているアーティストがいるならアーティスト画像を取得
			if (list_ar_regEntity.size() != 0) {
				ar_flag = 1;
				if (list_ar_regEntity.get(i).getArtist().getArtistimgpath() != null) {
					System.out
							.println("list_ar_regEntity.get(i).getSentence()" + list_ar_regEntity.get(i).getSentence());
					// DBに保存しているパスをString型に変換
					String dbcommentpath = null;
					dbcommentpath = list_ar_regEntity.get(i).getArtist().getArtistimgpath();
					// 画像データを取得
					String artistimgdata = null;
					artistimgdata = img_manage.Get_ChatSendImage(dbcommentpath);
					// 取得してきた画像データをEntityにセットする
					list_ar_regEntity.get(i).getArtist().setArtistimgpath(artistimgdata);

					// 「画像パス1」：値が入っていたら画像を取得
					if (list_ar_regEntity.get(i).getRegartistimgpath1() != null) {
						// DBに保存しているパスをString型に変換
						String dbpath1 = null;
						dbpath1 = list_ar_regEntity.get(i).getRegartistimgpath1();
						// 画像データを取得
						String imgdata1 = null;
						imgdata1 = img_manage.Get_RegisterArtistProfileImage(dbpath1);
						// 取得してきた画像データをEntityにセットする
						list_ar_regEntity.get(i).setRegartistimgpath1(imgdata1);
					} else {
						String mall_mg = null;
						mall_mg = img_manage.Get_ReturnWhiteImage();
						list_ar_regEntity.get(i).setRegartistimgpath1(mall_mg);
					}

					// 「画像パス2」：値が入っていたら画像を取得
					if (list_ar_regEntity.get(i).getRegartistimgpath2() != null) {
						// DBに保存しているパスをString型に変換
						String dbpath2 = null;
						dbpath2 = list_ar_regEntity.get(i).getRegartistimgpath2();
						// 画像データを取得
						String imgdata2 = null;
						imgdata2 = img_manage.Get_RegisterArtistProfileImage(dbpath2);
						// 取得してきた画像データをEntityにセットする
						list_ar_regEntity.get(i).setRegartistimgpath2(imgdata2);
					} else {
						String mall_mg = null;
						mall_mg = img_manage.Get_ReturnWhiteImage();
						list_ar_regEntity.get(i).setRegartistimgpath2(mall_mg);
					}

				}
			} else {
				ar_flag = 0;
			}

		}
		// フラグを送る
		model.addAttribute("ar_flag", ar_flag);

		// 登録アーティスト
		model.addAttribute("list_ar_regEntity", list_ar_regEntity);

		// コメント用のフォーム
		model.addAttribute("commentForm", new CommentSendForm());

		// 詳細ページ用
		model.addAttribute("chatsendForm", new ChatSendForm());

		// DM
		model.addAttribute("dmroom", new DmRoomEntity());

		// 登録アーティスト編集
		model.addAttribute("artist_RegisterForm", new Artist_RegisterForm());

		// 円形の投稿画像アイコンを用意
		img_manage.Get_MaruImage(model);

		// フォロー一覧用
		model.addAttribute("followForm", new FollowForm());

		/*-- メッセージ受け取り --*/
		// 検索
		List<MessageEntity> message_list = messageRepository.findByGetteraccount(accountEntity);
		// 送信者の画像データを取得
		for (int i = 0; i < message_list.size(); i++) {
			message_list.get(i).getSenderaccount().setAccountimgpath(
					img_manage.Response_Get_AccountImage(message_list.get(i).getSenderaccount().getMail()));
		}
		model.addAttribute("message_list", message_list);

		return "/account/account_top";
	}

	// アカウント編集
	@RequestMapping(value = "/account/account_manage", method = RequestMethod.POST)
	public String POST_Account_Manage(Model model, MultipartFile multipartFile, AccountForm accountForm) {

		// セッションメールアドレス取得
		session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウントのニックネーム、自己紹介文の変更処理
		// Entity(DB内の値)とForm(入力フォームの値)が違う場合
		if (accountForm.getNickname() != accountEntity.getNickname()
				|| accountForm.getIntroduction() != accountEntity.getIntroduction()) {
			// 1回目の処理を踏まえて(必然的にFormの値がnullになる)
			if (accountForm.getNickname() != null) {

				accountEntity.setMail(mailaddress);
				accountEntity.setPassword(accountForm.getPassword());
				accountEntity.setNickname(accountForm.getNickname());
				accountEntity.setIntroduction(accountForm.getIntroduction());
				System.out.println("accountEntity.getIntroduction()" + accountEntity.getIntroduction());
				accountRepository.save(accountEntity);
			}
		} else {
			System.out.println("アカウント編集、登録するカラムがありません");
		}

		// アカウント画像の変更処理
		accountForm.setMail(accountEntity.getMail());
		accountForm.setPassword(accountEntity.getPassword());
		accountForm.setNickname(accountEntity.getNickname());
		accountForm.setIntroduction(accountEntity.getIntroduction());
		model.addAttribute("accountForm", accountForm);

		// アカウント画像を取得
		img_manage.Save_AccountImage(model, mailaddress, multipartFile);
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		return "account/account_manage";
	}

	// アカウント編集(GET)
	@RequestMapping(value = "/account/account_manage", method = RequestMethod.GET)
	public String Get_Account_Manage(Model model, MultipartFile multipartFile, AccountForm accountForm) {
		// セッションメールアドレス取得
		session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// Formに値をセットし、表示
		accountForm.setUserid(accountEntity.getUserid());
		accountForm.setMail(accountEntity.getMail());
		accountForm.setPassword(accountEntity.getPassword());
		accountForm.setNickname(accountEntity.getNickname());
		accountForm.setIntroduction(accountEntity.getIntroduction());
		accountForm.setFollownum(accountEntity.getFollownum());
		accountForm.setFollowernum(accountEntity.getFollowernum());
		model.addAttribute("accountForm", accountForm);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		return "account/account_manage";
	}

	// 投稿(GET)
	@RequestMapping(value = "/account/chat_send", method = RequestMethod.GET)
	public String Chat_send(Model model) {

		// セッションメールアドレス取得
		session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// 円形の投稿画像アイコンを用意
		img_manage.Get_MaruImage(model);

		// フラグ
		int flg = 0;

		// 登録アーティストを取得
		int userid = accountEntity.getUserid();
		List<Artist_RegisterEntity> l_RegEntity = artist_RegisterRepository.findByUserid(userid);

		for (int i = 0; i < l_RegEntity.size(); i++) {
			if (l_RegEntity.get(i).getArtist().getArtistid() != 12) {
				String db_imgpath = null;
				String img_data = null;
				db_imgpath = l_RegEntity.get(i).getArtist().getArtistimgpath();
				img_data = img_manage.Get_ChatSendImage(db_imgpath);
				l_RegEntity.get(i).getArtist().setArtistimgpath(img_data);
				flg = 1;
			}
		}

		model.addAttribute("l_RegEntity", l_RegEntity);
		model.addAttribute("flg", flg);

		// 投稿用のフォーム
		model.addAttribute("ChatSendForm", new ChatSendForm());

		return "/account/chat_send";
	}

	// アーティスト登録ページ
	@RequestMapping(value = "/account/artist_register", method = RequestMethod.GET)
	public String GET_ArtistRegister(Model model) {
		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// Form
		model.addAttribute("a_regForm", new Artist_RegisterForm());
		model.addAttribute("artistForm", new ArtistForm());

		// 登録数が多い10件とアーティストを取得
		List<ArtistEntity> list_artist = artistRepository.queryAll();

		// アーティスト画像を取得
		for (int i = 0; i < list_artist.size(); i++) {
			// 指定なしを削除
			if (list_artist.get(i).getArtistid() == 1) {
				list_artist.remove(i);
			}
			if (list_artist.size() != 0) {

				if (list_artist.get(i).getArtistimgpath() != null) {
					// DBに保存しているパスをString型に変換
					String dbcommentpath = null;
					dbcommentpath = list_artist.get(i).getArtistimgpath();
					System.out.println("ssss" + dbcommentpath);
					// 画像データを取得
					String imgpath = null;
					imgpath = img_manage.Get_ChatSendImage(dbcommentpath);

					// 取得してきた画像データをEntityにセットする
					list_artist.get(i).setArtistimgpath(imgpath);
				} else {
					System.out.println("登録していません");
				}
			}
		}
		artist_flg = 1;
		model.addAttribute("list_artist", list_artist);
		model.addAttribute("artist_flg", artist_flg);

		return "/account/artist_register";
	}

	// アーティスト登録ページ
	@RequestMapping(value = "/account/artist_register", method = RequestMethod.POST)
	public String POST_ArtistRegister(Model model, ArtistForm artistForm) {
		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// 検索したワードを取得
		String search_word = artistForm.getArtistname();

		// DBに検索
		try {
			ArtistEntity artistEntity = artistRepository.findByArtistname(search_word);
			// 画像を取得
			String dbcommentpath = null;
			String imgpath = null;
			dbcommentpath = artistEntity.getArtistimgpath();
			imgpath = img_manage.Get_ChatSendImage(dbcommentpath);
			artistEntity.setArtistimgpath(imgpath);

			// いる場合
			artist_flg = 2;
			model.addAttribute("artistEntity", artistEntity);

		} catch (Exception e) {
			// 検索したワードに一致するアーティストがいない場合
			artist_flg = 3;
			String nullmessage = "一致するアーティストは見つかりませんでした";
			model.addAttribute("nullmessage", nullmessage);
		}

		model.addAttribute("artist_flg", artist_flg);

		return "/account/artist_register";
	}

	// 登録アーティスト一覧(GET)
	@RequestMapping(value = "/account/list_myartist", method = RequestMethod.GET)
	public String List_Artist(Model model) {
		// フラグ
		int flg = 0;

		// セッションメールアドレスを取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// セッションを基にユーザーidを取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);
		int userid = accountEntity.getUserid();

		// ユーザーidを基に登録アーティストテーブルを取得
		List<Artist_RegisterEntity> list_ar_regEntity = artist_RegisterRepository.findByUserid(userid);

		// アーティスト画像を取得
		for (int i = 0; i < list_ar_regEntity.size(); i++) {
			// 指定なしを削除
			if (list_ar_regEntity.get(i).getArtist().getArtistid() == 1) {
				list_ar_regEntity.remove(i);
			}
			if (list_ar_regEntity.size() != 0) {
				flg = 1;
				if (list_ar_regEntity.get(i).getArtist().getArtistimgpath() != null) {
					// DBに保存しているパスをString型に変換
					String dbcommentpath = null;
					dbcommentpath = list_ar_regEntity.get(i).getArtist().getArtistimgpath();
					// 画像データを取得
					String imgpath = null;
					imgpath = img_manage.Get_ChatSendImage(dbcommentpath);
					// 取得してきた画像データをEntityにセットする
					list_ar_regEntity.get(i).getArtist().setArtistimgpath(imgpath);
				} else {
					System.out.println("登録していません");
				}
			}
		}

		// 一覧データ表示用
		model.addAttribute("list_ar_regEntity", list_ar_regEntity);

		model.addAttribute("flg", flg);

		// 編集ページに渡す用
		model.addAttribute("artist_RegisterForm", new Artist_RegisterForm());

		return "/account/list_myartist";
	}

	// 登録アーティスト編集(POST)
	@RequestMapping(value = "/account/edit_myartist", method = RequestMethod.POST)
	public String POST_L_Edit_Artist(Model model, Artist_RegisterForm artist_RegisterForm) {
		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// 登録idを元にデータを取得
		Artist_RegisterEntity ar_RegEntity = new Artist_RegisterEntity();
		ar_RegEntity = artist_RegisterRepository.findByRegisterid(artist_RegisterForm.getRegisterid());
		System.out.println("artist_RegisterForm.getRegisterid()" + artist_RegisterForm.getRegisterid());
		static_artistregid = ar_RegEntity.getRegisterid();

		/* DBに格納しているパスを基に画像を取得 */
		// 「画像パス1」：値が入っていたら画像を取得
		if (ar_RegEntity.getRegartistimgpath1() != null) {
			// DBに保存しているパスをString型に変換
			String dbpath1 = null;
			dbpath1 = ar_RegEntity.getRegartistimgpath1();
			// 画像データを取得
			String imgdata1 = null;
			imgdata1 = img_manage.Get_RegisterArtistProfileImage(dbpath1);
			// 取得してきた画像データをEntityにセットする
			ar_RegEntity.setRegartistimgpath1(imgdata1);
		} else {
			String mall_mg = null;
			mall_mg = img_manage.Get_ReturnMaruImage();
			ar_RegEntity.setRegartistimgpath1(mall_mg);
		}

		// 「画像パス2」：値が入っていたら画像を取得
		if (ar_RegEntity.getRegartistimgpath2() != null) {
			// DBに保存しているパスをString型に変換
			String dbpath2 = null;
			dbpath2 = ar_RegEntity.getRegartistimgpath2();
			// 画像データを取得
			String imgdata2 = null;
			imgdata2 = img_manage.Get_RegisterArtistProfileImage(dbpath2);
			// 取得してきた画像データをEntityにセットする
			ar_RegEntity.setRegartistimgpath2(imgdata2);
		} else {
			String mall_mg = null;
			mall_mg = img_manage.Get_ReturnMaruImage();
			ar_RegEntity.setRegartistimgpath2(mall_mg);
		}

		// フォームに渡す
		Artist_RegisterForm dbget_aRegForm = new Artist_RegisterForm();
		dbget_aRegForm.setRegisterid(ar_RegEntity.getRegisterid());
		dbget_aRegForm.setUserid(ar_RegEntity.getUserid());
		dbget_aRegForm.setArtist(ar_RegEntity.getArtist());
		dbget_aRegForm.setSentence(ar_RegEntity.getSentence());
		dbget_aRegForm.setRegartistimgpath1(ar_RegEntity.getRegartistimgpath1());
		dbget_aRegForm.setRegartistimgpath2(ar_RegEntity.getRegartistimgpath2());

		// アーティスト画像取得
		String dbcommentpath = null;
		dbcommentpath = dbget_aRegForm.getArtist().getArtistimgpath();
		String imgpath = null;
		imgpath = img_manage.Get_ChatSendImage(dbcommentpath);
		// 取得してきた画像データをセットする
		dbget_aRegForm.getArtist().setArtistimgpath(imgpath);

		model.addAttribute("dbget_aRegForm", dbget_aRegForm);

		return "/account/edit_myartist";
	}

	// 登録アーティスト編集(GET)
	@RequestMapping(value = "/account/edit_myartist", method = RequestMethod.GET)
	public String GET_L_Edit_Artist(Model model) {
		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		Artist_RegisterEntity ar_RegEntity = new Artist_RegisterEntity();
		ar_RegEntity = artist_RegisterRepository.findByRegisterid(static_artistregid);

		/* DBに格納しているパスを基に画像を取得 */
		// 「画像パス1」：値が入っていたら画像を取得
		if (ar_RegEntity.getRegartistimgpath1() != null) {
			// DBに保存しているパスをString型に変換
			String dbpath1 = null;
			dbpath1 = ar_RegEntity.getRegartistimgpath1();
			// 画像データを取得
			String imgdata1 = null;
			imgdata1 = img_manage.Get_RegisterArtistProfileImage(dbpath1);
			// 取得してきた画像データをEntityにセットする
			ar_RegEntity.setRegartistimgpath1(imgdata1);
		} else {
			String mall_mg = null;
			mall_mg = img_manage.Get_ReturnWhiteImage();
			ar_RegEntity.setRegartistimgpath2(mall_mg);
		}

		// 「画像パス2」：値が入っていたら画像を取得
		if (ar_RegEntity.getRegartistimgpath2() != null) {
			// DBに保存しているパスをString型に変換
			String dbpath2 = null;
			dbpath2 = ar_RegEntity.getRegartistimgpath2();
			// 画像データを取得
			String imgdata2 = null;
			imgdata2 = img_manage.Get_RegisterArtistProfileImage(dbpath2);
			// 取得してきた画像データをEntityにセットする
			ar_RegEntity.setRegartistimgpath2(imgdata2);
		} else {
			String mall_mg = null;
			mall_mg = img_manage.Get_ReturnWhiteImage();
			ar_RegEntity.setRegartistimgpath2(mall_mg);
		}

		// フォームに渡す
		Artist_RegisterForm dbget_aRegForm = new Artist_RegisterForm();
		dbget_aRegForm.setRegisterid(ar_RegEntity.getRegisterid());
		dbget_aRegForm.setUserid(ar_RegEntity.getUserid());
		dbget_aRegForm.setArtist(ar_RegEntity.getArtist());
		dbget_aRegForm.setSentence(ar_RegEntity.getSentence());
		dbget_aRegForm.setRegartistimgpath1(ar_RegEntity.getRegartistimgpath1());
		dbget_aRegForm.setRegartistimgpath2(ar_RegEntity.getRegartistimgpath2());

		// アーティスト画像取得
		String dbcommentpath = null;
		dbcommentpath = dbget_aRegForm.getArtist().getArtistimgpath();
		String imgpath = null;
		imgpath = img_manage.Get_ChatSendImage(dbcommentpath);
		// 取得してきた画像データをセットする
		dbget_aRegForm.getArtist().setArtistimgpath(imgpath);

		model.addAttribute("dbget_aRegForm", dbget_aRegForm);

		return "/account/edit_myartist";
	}

	// 検索したユーザーのマイページ
	@RequestMapping(value = "/account/usersearch", method = RequestMethod.POST)
	public String POST_UserAccount(Model model, AccountForm accountForm, ChatSendForm chatsendForm,
			CommentSendForm commentsendForm) throws Exception {

		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// 検索ユーザーのメールアドレスを取得
		str = accountForm.getMail();
		System.out.println("accountForm" + accountForm);

		// 検索ユーザーのアカウントデータ取得
		accountEntity = accountRepository.findByMail(str);
		// 検索ユーザーのアカウントデータ表示
		model.addAttribute("account", accountEntity);

		// 検索ユーザーのアカウント画像を取得
		Get_UserImage(model, str);

		// 検索ユーザーのユーザーidを取得
		userid = accountEntity.getUserid();
		System.out.println("accountEntity.getUserid()" + accountEntity.getUserid());
		System.out.println("userid" + userid);

		// 検索ユーザーの投稿データを表示
		chat_manage.Get_ChatSendData(model, userid, accountEntity);

		// 自分自身のセッションメールアドレス取得
		session_manage.Get_SessionMail(mailaddress);
		AccountEntity session_account = new AccountEntity();
		session_account = accountRepository.findByMail(mailaddress);
		model.addAttribute("session_account", session_account);

		// コメント用のフォーム
		model.addAttribute("commentForm", new CommentSendForm());

		// コメントのデータを取得
		List<CommentSendEntity> comment_list = commentSendRepository.findByUserid(userid);
		model.addAttribute("comment_list", comment_list);

		// コメントのユーザー画像を取得
		// Get_CommentImage(model);

		// フォローしているかの確認
		int followflg = 0;
		FollowEntity followEntity = new FollowEntity();
		try {
			followEntity = followRepository.findByFollowaccountAndFolloweraccount(session_account, accountEntity);

			// 値があれば、フラグを１にする
			if (followEntity.getFollowaccount().getUserid() == session_account.getUserid()
					&& followEntity.getFolloweraccount().getUserid() == accountEntity.getUserid()) {
				followflg = 1;
			}

		} catch (Exception e) {
			// 無ければフラグを２にする
			System.out.println("フォローしていません");
			followflg = 2;
		}

		// フォローボタン用
		model.addAttribute("ma", mailaddress);
		model.addAttribute("str", str);
		model.addAttribute("followflg", followflg);
		model.addAttribute("followEntity", followEntity);
		model.addAttribute("followForm", new FollowForm());

		return "/account/account_top";
	}

	@RequestMapping(value = "/account/usersearch", method = RequestMethod.GET)
	public String Get_UserAccount(Model model, AccountForm accountForm, ChatSendForm chatsendForm,
			CommentSendForm commentsendForm, FollowForm followForm) throws Exception {

		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// 検索ユーザーのメールアドレスを取得
		AccountEntity acc = new AccountEntity();

		if (usersearch_userid != 0) {
			acc = accountRepository.findByUserid(usersearch_userid);
			System.out.println("########");

		} else {
			acc = accountRepository.findByUserid(followForm.getFolloweraccount().getUserid());
			System.out.println("%%%%%%%");
		}

		int followflg = 0;
		FollowEntity followEntity = new FollowEntity();
		try {
			followEntity = followRepository.findByFollowaccountAndFolloweraccount(accountEntity, acc);

			// 値があれば、フラグを１にする
			if (followEntity.getFollowaccount().getUserid() == accountEntity.getUserid()
					&& followEntity.getFolloweraccount().getUserid() == acc.getUserid()) {
				followflg = 1;
			}

		} catch (Exception e) {
			// 無ければフラグを２にする
			System.out.println("フォローしていません");
			followflg = 2;
		}

		// 検索ユーザーのアカウントデータ取得
		str = acc.getMail();
		accountEntity = accountRepository.findByMail(str);

		// 検索ユーザーのアカウント画像を取得
		Get_UserImage(model, str);

		// 検索ユーザーのアカウントデータ表示
		model.addAttribute("account", accountEntity);

		model.addAttribute("ma", mailaddress);
		model.addAttribute("str", str);
		model.addAttribute("followflg", followflg);
		model.addAttribute("followEntity", followEntity);
		model.addAttribute("followForm", new FollowForm());

		return "/account/account_top";
	}

	/*------------------ リダイレクト関数 -----------------*/
	// 投稿処理
	@RequestMapping(value = "/account/chat_send_process", method = RequestMethod.POST)
	public String Chat_Send_Process(Model model, MultipartFile multipartFile, MultipartFile multipartFile2,
			MultipartFile multipartFile3, MultipartFile multipartFile4, ChatSendForm chatSendForm) {

		// セッションメールアドレス取得
		session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// 日時情報を指定フォーマットの文字列で取得
		real_time = time_manage.Realtime_process(real_time);

		String count = null;
		String chatimgpath = null;

		ChatSendEntity chatSendEntity = new ChatSendEntity();

		// 投稿画像を保存
		if (multipartFile.getSize() != 0) {
			// 保存用の数字を用意
			count = "1";
			chatimgpath = img_manage.Save_ChatImage(model, mailaddress, multipartFile, count);
			chatSendEntity.setChatimgpath1(chatimgpath);
			System.out.println("!!!!!");

		}
		if (multipartFile2.getSize() != 0) {
			// 画像１が送られているか判定し数字を変える
			System.out.println("!!!!!");
			chatimgpath = null;
			if (count == null) {
				count = "1";
				chatimgpath = img_manage.Save_ChatImage(model, mailaddress, multipartFile2, count);
				chatSendEntity.setChatimgpath1(chatimgpath);
			} else {
				count = "2";
				chatimgpath = img_manage.Save_ChatImage(model, mailaddress, multipartFile2, count);
				chatSendEntity.setChatimgpath2(chatimgpath);
			}
		}
		if (multipartFile3.getSize() != 0) {
			// 画像１、2が送られているか判定し数字を変える
			System.out.println("!!!!!");
			chatimgpath = null;
			if (count == null) {
				count = "1";
				chatimgpath = img_manage.Save_ChatImage(model, mailaddress, multipartFile3, count);
				chatSendEntity.setChatimgpath1(chatimgpath);
			} else if (count == "1") {
				count = "2";
				chatimgpath = img_manage.Save_ChatImage(model, mailaddress, multipartFile3, count);
				chatSendEntity.setChatimgpath2(chatimgpath);
			} else {
				count = "3";
				chatimgpath = img_manage.Save_ChatImage(model, mailaddress, multipartFile3, count);
				chatSendEntity.setChatimgpath3(chatimgpath);
			}
		}
		if (multipartFile4.getSize() != 0) {
			// 画像１、2、３が送られているか判定し数字を変える
			System.out.println("!!!!!");
			chatimgpath = null;
			if (count == null) {
				count = "1";
				chatimgpath = img_manage.Save_ChatImage(model, mailaddress, multipartFile4, count);
				chatSendEntity.setChatimgpath1(chatimgpath);
			} else if (count == "1") {
				count = "2";
				chatimgpath = img_manage.Save_ChatImage(model, mailaddress, multipartFile4, count);
				chatSendEntity.setChatimgpath2(chatimgpath);
			} else if (count == "2") {
				count = "3";
				chatimgpath = img_manage.Save_ChatImage(model, mailaddress, multipartFile4, count);
				chatSendEntity.setChatimgpath3(chatimgpath);
			} else {
				count = "4";
				chatimgpath = img_manage.Save_ChatImage(model, mailaddress, multipartFile4, count);
				chatSendEntity.setChatimgpath4(chatimgpath);
			}
		}

		// アーティスト情報を取得
		ArtistEntity arEntity = new ArtistEntity();
		System.out.println("*********" + chatSendForm.getArtist().getArtistid());
		arEntity = artistRepository.findByArtistid(chatSendForm.getArtist().getArtistid());
		chatSendEntity.setArtist(arEntity);

		// 投稿内容をDB保存
		System.out.println("$$$$$$$" + accountEntity.getUserid());
		chatSendEntity.setAccount(accountEntity);
		chatSendEntity.setSentence(chatSendForm.getSentence());
		chatSendEntity.setChatsendday(real_time);

		chatSendService.save(chatSendEntity);

		return "redirect:/account/chat_send";
	}

	// アカウント編集(POST:何かしらのアカウントデータを変更)
	@RequestMapping(value = "/account/save", method = RequestMethod.POST)
	public String AccountImage_save(@Validated(AccountForm.All.class) AccountForm accountForm, BindingResult result,
			Model model, MultipartFile multipartFile) {

		// 現在のアカウント画像のパスを取得
		String acc_imgpath = img_manage.GetPath_AccountImage(mailaddress);

		// バリデーションチェック(正常)、パスワード、再入力(正常)
		if (!result.hasErrors()) {
			// 変更した内容を保存
			AccountEntity updateaccountEntity = new AccountEntity();
			updateaccountEntity.setUserid(accountForm.getUserid());
			updateaccountEntity.setMail(accountForm.getMail());
			updateaccountEntity.setPassword(accountForm.getPassword());
			updateaccountEntity.setNickname(accountForm.getNickname());
			// 画像を送信した場合
			if (multipartFile.getSize() != 0) {
				// 画像変更
				img_manage.Save_AccountImage(model, mailaddress, multipartFile);
				// 変更した際のアカウント画像のパスを取得
				acc_imgpath = null;
				acc_imgpath = img_manage.Save_AccountImage(model, mailaddress, multipartFile);
			}
			updateaccountEntity.setAccountimgpath(acc_imgpath);
			updateaccountEntity.setIntroduction(accountForm.getIntroduction());
			updateaccountEntity.setFollownum(accountForm.getFollownum());
			updateaccountEntity.setFollowernum(accountForm.getFollowernum());
			accountRepository.save(updateaccountEntity);
		} else {
			System.out.println("バリデーションエラー");
			// セッションメールアドレス取得
			session_manage.Get_SessionMail(mailaddress);

			// アカウントデータ取得
			accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

			// Formに値をセットし、表示
			accountForm.setUserid(accountEntity.getUserid());
			accountForm.setMail(accountEntity.getMail());
			accountForm.setPassword(accountEntity.getPassword());
			accountForm.setNickname(accountEntity.getNickname());
			accountForm.setIntroduction(accountEntity.getIntroduction());
			accountForm.setFollownum(accountEntity.getFollownum());
			accountForm.setFollowernum(accountEntity.getFollowernum());
			model.addAttribute("accountForm", accountForm);

			// アカウント画像を取得
			img_manage.Get_AccountImage(model, mailaddress);

			// トップ用
			img_manage.Get_TopAccountImage(model, mailaddress);

			return "account/account_manage";
		}

		return "redirect:/account/account_manage/";
	}

	// コメントの保存処理
	@RequestMapping(value = "/account/comment", method = RequestMethod.POST)
	public String Comment_save(CommentSendForm commentsendForm, MultipartFile multipartFile) {

		String mail = null;
		mail = session_manage.Get_SessionMail(mail);
		AccountEntity accountEntity = accountRepository.findByMail(mail);

		CommentSendEntity commentSendEntity = new CommentSendEntity();
		// ユーザーid
		commentSendEntity.setAccount(accountEntity);
		// 文章
		commentSendEntity.setSentence(commentsendForm.getSentence());
		// 画像
		// ログイン時に作られているディレクトリを取得
		String comment_imgpath = img_manage.Create_CommentFolder(mailaddress);
		// 現在時刻を取得
		real_time = time_manage.Realtime_process(real_time);
		// 送られてきた画像をフォルダに保存
		String commentimg_path = img_manage.Save_CommentImage(mailaddress, multipartFile, comment_imgpath, real_time);
		// 保存したパスをEntityにセット
		commentSendEntity.setCommentimgpath(commentimg_path);
		// 日付
		commentSendEntity.setCommentday(real_time);
		// 投稿id
		ChatSendEntity c_entity = chatSendRepository.findByChatsendid(commentsendForm.getChatsendid());
		commentSendEntity.setChat(c_entity);
		// 保存
		commentSendRepository.save(commentSendEntity);

		/*-- メッセージを送信 --*/
		MessageEntity messageEntity = new MessageEntity();
		// 送信者
		messageEntity.setSenderaccount(accountEntity);
		// 受信者
		messageEntity.setGetteraccount(c_entity.getAccount());
		// 内容
		messageEntity.setContent("コメントが送られてきました");
		// 日時
		String real_time = null;
		real_time = time_manage.Realtime_process(real_time);
		messageEntity.setMessageday(real_time);
		// 保存
		messageRepository.save(messageEntity);

		return "redirect:/account/account_top";
	}

	// アーティスト検索
	@RequestMapping(value = "/account/artist_search", method = RequestMethod.POST)
	public String Artist_Search(Model model) {
		model.addAttribute("a_regForm", new Artist_RegisterForm());
		model.addAttribute("artistForm", new ArtistForm());

		return "redirect:/account/artist_register";
	}

	// フォロー処理
	@RequestMapping(value = "/follow", method = RequestMethod.POST)
	public String Follow(Model model, FollowForm followForm) {

		// フォローテーブルにデータ追加
		FollowEntity f_entity = new FollowEntity();
		f_entity.setFollowaccount(followForm.getFollowaccount());
		f_entity.setFolloweraccount(followForm.getFolloweraccount());
		followRepository.save(f_entity);

		usersearch_userid = followForm.getFolloweraccount().getUserid();

		/*--フォローしたユーザーのフォロー数カウント--*/
		// フォローしたユーザーのidを取得
		// 取得したidを元にフォロー数を取得
		AccountEntity a_entity = accountRepository.findByUserid(followForm.getFollowaccount().getUserid());
		// フォロー数を加算
		Integer new_follownum = a_entity.getFollownum() + 1;
		a_entity.setFollownum(new_follownum);
		// 更新
		accountRepository.save(a_entity);

		/*--フォローされたユーザーのフォロワー数をカウント--*/
		// フォローされたユーザーのidを取得
		// 取得したidを元にフォロワー数を取得
		AccountEntity a2_entity = new AccountEntity();
		a2_entity = accountRepository.findByUserid(followForm.getFolloweraccount().getUserid());
		// フォロワー数を加算
		Integer new_followernum = a2_entity.getFollowernum() + 1;
		a2_entity.setFollowernum(new_followernum);
		// 更新
		accountRepository.save(a2_entity);

		return "redirect:/account/usersearch";
	}

	// フォロー解除処理
	@RequestMapping(value = "/follow/delete", method = RequestMethod.POST)
	public String Follow_Delete(FollowForm followForm) {
		System.out.println("followForm.getFollowaccount()" + followForm.getFollowaccount());
		// 削除
		followRepository.deleteByFollowaccountAndFolloweraccount(followForm.getFollowaccount(),
				followForm.getFolloweraccount());

		usersearch_userid = followForm.getFolloweraccount().getUserid();

		/*-- フォロー数を減算 --*/
		// フォローユーザーのidを基に現在のフォロー数を取得してくる
		accountEntity = accountRepository.findByUserid(followForm.getFollowaccount().getUserid());
		// フォロー数を-1
		Integer new_follownum = accountEntity.getFollownum() - 1;
		accountEntity.setFollownum(new_follownum);
		// 更新
		accountRepository.save(accountEntity);

		// フォロワー数を減算
		// フォローされたユーザーのidを基に現在のフォロワー数を取得してくる
		accountEntity = accountRepository.findByUserid(followForm.getFolloweraccount().getUserid());
		// フォロワー数を-1
		Integer new_followernum = accountEntity.getFollowernum() - 1;
		accountEntity.setFollowernum(new_followernum);
		// 更新
		accountRepository.save(accountEntity);

		return "redirect:/account/usersearch";
	}

	// アーティスト登録処理
	@RequestMapping(value = "/account/artist_regprocess", method = RequestMethod.POST)
	public String Artist_Regprocess(Model model, ArtistForm artistForm) {
		// セッションメールアドレスを取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// セッションを基にユーザーidを取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);
		int userid = accountEntity.getUserid();

		// アーティストidを取得
		ArtistEntity artistEntity = new ArtistEntity();
		artistEntity = artistRepository.findByArtistid(artistForm.getArtistid());

		// 登録アーティストテーブルに追加
		Artist_RegisterEntity ar_regEntity = new Artist_RegisterEntity();
		// ユーザーid
		ar_regEntity.setUserid(userid);
		// アーティストid
		ar_regEntity.setArtist(artistEntity);
		artist_RegisterRepository.save(ar_regEntity);

		return "redirect:/account/list_myartist";
	}

	/*--- メソッド ---*/
	// アカウント画像取得メソッド
	private void Get_UserImage(Model model, String str) {
		// アカウント画像取得メソッド
		uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\account\\"
				+ str + "\\" + str + ".jpg";

		// 画像表示処理
		try (FileInputStream fis = new FileInputStream(uploadPath);) {
			StringBuffer data = new StringBuffer();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			// バイト配列に変換
			while (true) {
				int len = fis.read(buffer);
				if (len < 0) {
					break;
				}
				os.write(buffer, 0, len);
			}

			// base64にエンコード、タイムリーフにデータを渡す
			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
					"ASCII");
			data.append("data:image/jpg;base64,");
			data.append(base64);
			model.addAttribute("base64image", data.toString());
		}
		// まだアカウント用の画像を登録してない場合
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("画像を登録していない");

			// デフォルトの画像データを持ってくる
			String default_uploadPath = "C:\\Users\\takaf\\Documents\\workspace-spring-tool-suite-4-4.6.2.RELEASE\\SNSproject\\src\\main\\resources\\static\\image\\default\\default.jpg";

			// 画像表示処理
			try (FileInputStream fis = new FileInputStream(default_uploadPath);) {
				StringBuffer data = new StringBuffer();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				// バイト配列に変換
				while (true) {
					int len = fis.read(buffer);
					if (len < 0) {
						break;
					}
					os.write(buffer, 0, len);
				}
				// base64にエンコード、タイムリーフにデータを渡す
				String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
						"ASCII");
				data.append("data:image/jpg;base64,");
				data.append(base64);
				model.addAttribute("base64image", data.toString());
			} catch (Exception e2) {
				e.printStackTrace();
				System.out.println("デフォルト画像処理失敗");
			}
		}
	}

	// 登録アーティスト削除(POST)
	@RequestMapping(value = "/account/delete_myartist", method = RequestMethod.POST)
	public String POST_Delete_MyArtist(Artist_RegisterForm artist_RegisterForm) {
		artist_RegisterRepository.deleteById(artist_RegisterForm.getRegisterid());
		return "redirect:/account/list_myartist";
	}

	// 登録アーティスト編集処理(POST)
	@RequestMapping(value = "/account/edit_myartist_process", method = RequestMethod.POST)
	public String POST_Edit_MyArtist(Model model, Artist_RegisterForm artist_RegisterForm, MultipartFile multipartFile,
			MultipartFile multipartFile2) {

		Artist_RegisterEntity ar_RegEntity = new Artist_RegisterEntity();
		ar_RegEntity = artist_RegisterRepository.findByRegisterid(artist_RegisterForm.getRegisterid());

		// FormからEntityへの受け渡し
		// 登録id、ユーザーid、文章、外部キー
		ar_RegEntity.setRegisterid(artist_RegisterForm.getRegisterid());
		ar_RegEntity.setUserid(artist_RegisterForm.getUserid());
		ar_RegEntity.setSentence(artist_RegisterForm.getSentence());
		ar_RegEntity.setArtist(artist_RegisterForm.getArtist());

		// 画像1,2
		// カウント用
		String count = null;
		// 登録id(フォルダ生成用)
		String id = null;
		// 画像パス取得用
		String a_registerimgpath = null;
		// メールアドレスの受け取り
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// 画像を登録しなおしたか
		id = String.valueOf(ar_RegEntity.getRegisterid());
		if (multipartFile.getSize() != 0) {
			// 保存用の数字を用意
			count = "1";
			a_registerimgpath = img_manage.Save_RegisterArtistProfileImage(model, mailaddress, multipartFile, count,
					id);
			ar_RegEntity.setRegartistimgpath1(a_registerimgpath);
			System.out.println("!!!!!");

		}

		if (multipartFile2.getSize() != 0) {
			a_registerimgpath = null;
			// 画像１が送られていないかつ、DBの画像パス1がnullの場合はmulti2の画像をパス1にセットする
			if (count == null && ar_RegEntity.getRegartistimgpath1() == null) {
				count = "1";
				a_registerimgpath = img_manage.Save_RegisterArtistProfileImage(model, mailaddress, multipartFile2,
						count, id);
				ar_RegEntity.setRegartistimgpath1(a_registerimgpath);
			} else {
				count = "2";
				a_registerimgpath = img_manage.Save_RegisterArtistProfileImage(model, mailaddress, multipartFile2,
						count, id);
				ar_RegEntity.setRegartistimgpath2(a_registerimgpath);
			}
		}

		// 更新
		artist_RegisterRepository.save(ar_RegEntity);

		return "redirect:/account/edit_myartist";
	}

	// フォロー一覧
	@RequestMapping(value = "/account/follow_list", method = RequestMethod.POST)
	public String Follow_List(Model model, FollowForm followForm) {

		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);
		model.addAttribute("accountEntity", accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		List<FollowEntity> followEntity = followRepository.findByFollowaccount(followForm.getFollowaccount());

		for (int i = 0; i < followEntity.size(); i++) {
			String img_data = null;
			img_data = img_manage.Response_Get_AccountImage(followEntity.get(i).getFolloweraccount().getMail());
			followEntity.get(i).getFolloweraccount().setAccountimgpath(img_data);

		}
		model.addAttribute("followEntity", followEntity);

		// フォロー解除用
		model.addAttribute("followForm", new FollowForm());
		// アカウントページ用
		model.addAttribute("accountForm", new AccountForm());

		return "/account/follow_list";
	}

	// フォロー解除処理
	@RequestMapping(value = "/followlist/delete", method = RequestMethod.POST)
	public String FollowList_Delete(Model model, FollowForm followForm) {
		// 削除
		System.out.println("１１１１１");
		followRepository.deleteByFollowaccountAndFolloweraccount(followForm.getFollowaccount(),
				followForm.getFolloweraccount());
		System.out.println("１１１１１");

		usersearch_userid = followForm.getFolloweraccount().getUserid();

		/*-- フォロー数を減算 --*/
		// フォローユーザーのidを基に現在のフォロー数を取得してくる
		accountEntity = accountRepository.findByUserid(followForm.getFollowaccount().getUserid());
		// フォロー数を-1
		Integer new_follownum = accountEntity.getFollownum() - 1;
		accountEntity.setFollownum(new_follownum);
		// 更新
		accountRepository.save(accountEntity);

		// フォロワー数を減算
		// フォローされたユーザーのidを基に現在のフォロワー数を取得してくる
		accountEntity = accountRepository.findByUserid(followForm.getFolloweraccount().getUserid());
		// フォロワー数を-1
		Integer new_followernum = accountEntity.getFollowernum() - 1;
		accountEntity.setFollowernum(new_followernum);
		// 更新
		accountRepository.save(accountEntity);

		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);
		model.addAttribute("accountEntity", accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		List<FollowEntity> followEntity = followRepository.findByFollowaccount(followForm.getFollowaccount());

		for (int i = 0; i < followEntity.size(); i++) {
			String img_data = null;
			img_data = img_manage.Response_Get_AccountImage(followEntity.get(i).getFolloweraccount().getMail());
			followEntity.get(i).getFolloweraccount().setAccountimgpath(img_data);

		}
		model.addAttribute("followEntity", followEntity);

		// フォロー解除用
		model.addAttribute("followForm", new FollowForm());
		// アカウントページ用
		model.addAttribute("accountForm", new AccountForm());

		return "/account/follow_list";
	}

	// フォロワー一覧
	@RequestMapping(value = "/account/follower_list", method = RequestMethod.POST)
	public String Follower_List(Model model, FollowForm followForm) {

		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);
		model.addAttribute("accountEntity", accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// フォロワーのデータを取得
		List<FollowEntity> followerEntity = followRepository.findByFolloweraccount(followForm.getFolloweraccount());

		// フォロワーの画像データを取得
		for (int i = 0; i < followerEntity.size(); i++) {
			String img_data = null;
			img_data = img_manage.Response_Get_AccountImage(followerEntity.get(i).getFollowaccount().getMail());
			followerEntity.get(i).getFollowaccount().setAccountimgpath(img_data);
		}
		model.addAttribute("followerEntity", followerEntity);

		// フォロー解除用
		model.addAttribute("followForm", new FollowForm());
		// アカウントページ用
		model.addAttribute("accountForm", new AccountForm());

		return "/account/follower_list";
	}
}