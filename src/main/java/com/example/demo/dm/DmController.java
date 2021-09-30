package com.example.demo.dm;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.account.AccountEntity;
import com.example.demo.account.AccountRepository;
import com.example.demo.components.Account_manage;
import com.example.demo.components.Image_manage;
import com.example.demo.components.Realtime_manage;
import com.example.demo.components.Session_manage;

@Controller
public class DmController {

	@Autowired
	DmRoomRepository roomRepository;
	@Autowired
	DmMessageRepository messageRepository;
	@Autowired
	Account_manage acc_manage;
	@Autowired
	Image_manage img_manage;
	@Autowired
	Realtime_manage timemanage;
	@Autowired
	Account_manage account_manage;
	@Autowired
	Session_manage session_manage;
	@Autowired
	AccountRepository accountRepository;

	// DMルームテーブルのメールアドレス受け取り用
	private String guestmail;

	// 現在時刻受け取り
	private String real_time;

	// アカウントデータ取得
	private static AccountEntity accountEntity;

	// DM画像保存パス
	private String dm_img_path;

	// 送信者のメールアドレスを取得
	private String dm_sendmailaddress = null;

	// セッションで保持したメールアドレス
	private String mailaddress;

	// DMルームページ(GET)
	@RequestMapping(value = "/dm/dmroom", method = RequestMethod.GET)
	public String GET_DM(Model model, DmRoomForm roomForm) {

		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		List<String> img_list = new ArrayList<String>();
		List<String> txt_list = new ArrayList<String>();

		// DM相手のニックネーム、アカウント画像の取得
		DmRoomEntity dmroom = new DmRoomEntity();
		dmroom = Get_GuestAccount(model, roomForm, dmroom);
		// Messageテーブルの外部キーを取得
		List<DmMessageEntity> dmlist = new ArrayList<>();
		dmlist = messageRepository.findByDmroom(dmroom);
		model.addAttribute("dmlist", dmlist);

		// 画像パスを受け取り
		for (int i = 0; i < dmlist.size(); i++) {
			// リストからひとつずつ取り出し、Stringに変換
			System.out.println("********" + dmlist.size());
			// 送信者メールアドレス
			dm_sendmailaddress = dmlist.get(i).getSendusermail();
			System.out.println("|||||||" + dm_sendmailaddress);
			// パス
			System.out.println("********" + dmlist.size());
			String dbimgpath = dmlist.get(i).getImagepath();
			System.out.println("pppppppppp" + dbimgpath);
			// 時間
			String dbtime = dmlist.get(i).getDmmessageday();

			System.out.println("ttttttttttt" + dbtime);
			String replace_dbtime = dbtime.replaceAll("-", "");
			replace_dbtime = replace_dbtime.replaceAll(":", "");
			replace_dbtime = replace_dbtime.replaceAll(" ", "");
			System.out.println("sssssssss" + replace_dbtime);
			// リストに文章、画像、時間を入れる
			String dbsentence = dmlist.get(i).getSentence();
			txt_list.add(dbsentence);

			// 画像を検索し取得する
			String imgpath = img_manage.Get_DMImage(dm_sendmailaddress, dbimgpath, replace_dbtime);
			System.out.println("iiiiiiiiiiiii" + imgpath);
			img_list.add(imgpath);

			// 時間
			txt_list.add(dbtime);

			imgpath = null;
			dbtime = null;
			replace_dbtime = null;

		}
		model.addAttribute("img_list", img_list);
		model.addAttribute("txt_list", txt_list);

		// 〇アイコン画像
		img_manage.Get_MaruImage(model);

		return "/dm/dmroom";
	}

	// Dmルームページ(POST)
	@RequestMapping(value = "/dm/dmroom", method = RequestMethod.POST)
	public String POST_DM(Model model, DmRoomForm roomForm) {

		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);

		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		List<String> img_list = new ArrayList<String>();
		List<String> txt_list = new ArrayList<String>();

		// DM相手のニックネーム、アカウント画像の取得
		DmRoomEntity dmroom = new DmRoomEntity();
		dmroom = Get_GuestAccount(model, roomForm, dmroom);
		// Messageテーブルの外部キーを取得
		List<DmMessageEntity> dmlist = new ArrayList<>();
		dmlist = messageRepository.findByDmroom(dmroom);
		model.addAttribute("dmlist", dmlist);

		System.out.println("********" + dmlist.size());
		// 画像パスを受け取り
		for (int i = 0; i < dmlist.size(); i++) {
			// リストからひとつずつ取り出し、Stringに変換

			// 送信者メールアドレス
			dm_sendmailaddress = dmlist.get(i).getSendusermail();
			System.out.println("|||||||" + dm_sendmailaddress);
			// パス
			String dbimgpath = dmlist.get(i).getImagepath();
			System.out.println("pppppppppp" + dbimgpath);
			// 時間
			String dbtime = dmlist.get(i).getDmmessageday();

			System.out.println("ttttttttttt" + dbtime);
			String replace_dbtime = dbtime.replaceAll("-", "");
			replace_dbtime = replace_dbtime.replaceAll(":", "");
			replace_dbtime = replace_dbtime.replaceAll(" ", "");
			System.out.println("sssssssss" + replace_dbtime);

			// リストに文章、画像、時間を入れる
			String dbsentence = dmlist.get(i).getSentence();
			txt_list.add(dbsentence);

			// 画像を検索し取得する
			String imgpath = img_manage.Get_DMImage(dm_sendmailaddress, dbimgpath, replace_dbtime);
			System.out.println("iiiiiiiiiiiii" + imgpath);

			img_list.add(imgpath);

			// 時間
			txt_list.add(dbtime);

			imgpath = null;
			dbtime = null;
			replace_dbtime = null;

		}
		model.addAttribute("img_list", img_list);
		model.addAttribute("txt_list", txt_list);

		StringBuffer data = new StringBuffer();
		model.addAttribute("base", data.toString());

		return "/dm/dmroom";
	}

	// DM相手のアカウントデータ取得
	private DmRoomEntity Get_GuestAccount(Model model, DmRoomForm roomForm, DmRoomEntity dmroomEntity) {
		DmRoomEntity roomEntity = new DmRoomEntity();

		// ルームがあるかをチェック
		DmRoomEntity existroomEntity = roomRepository.findByYuanaccountANDGuestaccount(roomForm.getYuanaccount(),
				roomForm.getGuestaccount());
		if (existroomEntity == null) {
			// ない場合
			// ルームを作る
			roomEntity.setYuanaccount(roomForm.getYuanaccount());
			roomEntity.setGuestaccount(roomForm.getGuestaccount());
			roomRepository.save(roomEntity);

			// ニックネームを取得
			int GuestUserid = roomForm.getGuestaccount().getUserid();
			accountEntity = accountRepository.findByUserid(GuestUserid);

			// アカウントデータをタイムリーフに渡す
			model.addAttribute("acc", accountEntity);

			// アカウント画像を取得
			String imgdata = null;
			imgdata = img_manage.Response_Get_AccountImage(accountEntity.getMail());
			accountEntity.setAccountimgpath(imgdata);

			// メールアドレスを渡す
			model.addAttribute("roomEntity", roomEntity);
			return roomEntity;
		} else {
			// ある場合
			// ニックネームを取得
			int GuestUserid = roomForm.getGuestaccount().getUserid();
			accountEntity = accountRepository.findByUserid(GuestUserid);

			// アカウントデータをタイムリーフに渡す
			model.addAttribute("acc", accountEntity);

			// アカウント画像を取得
			String imgdata = null;
			imgdata = img_manage.Response_Get_AccountImage(accountEntity.getMail());
			accountEntity.setAccountimgpath(imgdata);

			// メールアドレスを渡す
			model.addAttribute("roomEntity", roomEntity);

			// 投稿idの情報を渡す
			model.addAttribute("roomEntity", existroomEntity);

			// メッセージ用
			model.addAttribute("dmmessage", new DmMessageForm());

			return existroomEntity;
		}
	}

	// DM履歴
	@RequestMapping(value = "/dm/dm_history", method = RequestMethod.GET)
	private String DM_History(Model model) {
		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);
		model.addAttribute("accountEntity", accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);
		
		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		// ルームを作ったことのあるユーザー情報を取得
		List<DmRoomEntity> dmroom_list = roomRepository.findByYuanaccountOrGuestaccount(accountEntity, accountEntity);

		// 相手の画像データを取得
		for (int i = 0; i < dmroom_list.size(); i++) {
			String imgdata = null;
			imgdata = img_manage.Response_Get_AccountImage(dmroom_list.get(i).getGuestaccount().getMail());
			dmroom_list.get(i).getGuestaccount().setAccountimgpath(imgdata);
		}
		model.addAttribute("dmroom_list", dmroom_list);

		return "/dm/dm_history";
	}
}