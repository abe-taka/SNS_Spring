package com.example.demo.user_search;

import java.io.FileInputStream;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.account.AccountEntity;
import com.example.demo.account.AccountForm;
import com.example.demo.account.AccountRepository;
import com.example.demo.components.Account_manage;
import com.example.demo.components.Image_manage;
import com.example.demo.components.Session_manage;
import com.example.demo.follow.FollowEntity;
import com.example.demo.follow.FollowForm;
import com.example.demo.follow.FollowRepository;

@Controller
public class UserSearchController {

	@Autowired
	UserSearchRepository urepo;
	@Autowired
	Image_manage img_manage;
	@Autowired
	Account_manage account_manage;
	@Autowired
	Session_manage session_manage;
	@Autowired
	FollowRepository followRepository;
	@Autowired
	AccountRepository accountRepository;

	// アカウント画像取得用グローバル変数
	private static String uploadPath;

	// セッションで保持したメールアドレス
	private String mailaddress;

	// アカウントデータ取得
	private static AccountEntity accountEntity;

	// ユーザー検索(GET)
	@RequestMapping(value = "user_search/user_search", method = RequestMethod.GET)
	public String GetUserSearch(Model model, AccountForm accountForm) {

		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);
		
		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		model.addAttribute("accountForm", new AccountForm());
		
		// フォローボタン用
		model.addAttribute("followForm", new FollowForm());
		
		//オススメユーザーを取得

		return "user_search/user_search";
	}

	// ユーザー検索(POST)
	@RequestMapping(value = "user_search/user_search", method = RequestMethod.POST)
	public String PostUserSearch(Model model, AccountForm accountForm) {
		// セッションメールアドレス取得
		mailaddress = session_manage.Get_SessionMail(mailaddress);

		// アカウントデータ取得
		accountEntity = account_manage.Get_AccountData(model, mailaddress, accountEntity);
		model.addAttribute("accountEntity", accountEntity);

		// アカウント画像を取得
		img_manage.Get_AccountImage(model, mailaddress);
		
		// トップ用
		img_manage.Get_TopAccountImage(model, mailaddress);

		/*--- 検索ユーザー処理 ---*/
		// 検索したニックネームを取得
		String str = accountForm.getNickname();
		// ニックネームを基にアカウントデータを取得
		AccountEntity AccEn = urepo.findByNickname(str);
		int search_flg = 0;
		
		// アカウントページ用
		model.addAttribute("accountForm", new AccountForm());

		// フォローボタン用
		model.addAttribute("followForm", new FollowForm());
		
		//検索した名前が該当しなかった場合
		if(AccEn == null) {
			search_flg = 1;
			model.addAttribute("search_flg",search_flg);
			return "user_search/user_search";
		}

		// 検索ユーザーの画像データを持ってくる
		String mailaddress = AccEn.getMail();
		String imgdata = null;
		imgdata = img_manage.Response_Get_AccountImage(mailaddress);
		AccEn.setAccountimgpath(imgdata);
		model.addAttribute("acc", AccEn);

		FollowEntity followEntity = followRepository.findByFollowaccountAndFolloweraccount(accountEntity,AccEn);
		int follwflg = 0;
		System.out.println("followEntity" + followEntity);
		if (followEntity != null) {
			follwflg = 1;
			System.out.println("follwflg" + follwflg);
		}
		model.addAttribute("follwflg", follwflg);

		
		return "user_search/user_search";
	}

	// フォロー処理
	@RequestMapping(value = "/user_search/follow", method = RequestMethod.POST)
	public String FollowCancel_User(FollowForm followForm) {

		System.out.println("followForm.getFollowaccount();" + followForm.getFollowaccount());
		System.out.println("followForm.getFolloweraccount();" + followForm.getFolloweraccount());

		// フォローテーブルにデータ追加
		FollowEntity f_entity = new FollowEntity();
		f_entity.setFollowaccount(followForm.getFollowaccount());
		f_entity.setFolloweraccount(followForm.getFolloweraccount());
		followRepository.save(f_entity);

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
	@RequestMapping(value = "/user_search/followcancel", method = RequestMethod.POST)
	public String Follow_User(FollowForm followForm) {

		// フォローテーブルにデータ削除
		FollowEntity f_entity = new FollowEntity();

		f_entity.setFollowaccount(followForm.getFollowaccount());
		f_entity.setFolloweraccount(followForm.getFolloweraccount());
		FollowEntity followEntity = followRepository
				.findByFollowaccountAndFolloweraccount(followForm.getFollowaccount(), followForm.getFolloweraccount());
		followRepository.delete(followEntity);

		/*--フォローしたユーザーのフォロー数マイナス--*/
		// フォローしたユーザーのidを取得
		// 取得したidを元にフォロー数を取得
		AccountEntity a_entity = accountRepository.findByUserid(followForm.getFollowaccount().getUserid());
		// フォロー数を減算
		Integer new_follownum = a_entity.getFollownum() - 1;
		a_entity.setFollownum(new_follownum);
		// 更新
		accountRepository.save(a_entity);

		/*--フォローされたユーザーのフォロワー数をマイナス--*/
		// フォローされたユーザーのidを取得
		// 取得したidを元にフォロワー数を取得
		AccountEntity a2_entity = new AccountEntity();
		a2_entity = accountRepository.findByUserid(followForm.getFolloweraccount().getUserid());
		// フォロワー数を減算
		Integer new_followernum = a2_entity.getFollowernum() - 1;
		a2_entity.setFollowernum(new_followernum);
		// 更新
		accountRepository.save(a2_entity);

		return "redirect:/user_search/user_search";
	}
}