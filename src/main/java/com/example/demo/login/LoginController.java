package com.example.demo.login;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*パッケージ外のクラスオブジェクトを生成するにはインポートが必要*/
import com.example.demo.account.AccountEntity;
import com.example.demo.account.AccountRepository;

@Controller
public class LoginController {

	//メール送信のクラス
	@Autowired
	private MailSender sender; 
	@Autowired
	private LoginRepository loginRepository;
	@Autowired
	private AccountRepository accountRepository;

	// 乱数で生成された暗証番号を入れる変数
	private int randomValue;

	// ログイン
	@GetMapping("/")
	public String Get_Login(Model model) {
		
		// セッションが存在するかをチェックする
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String mailaddress = auth.getName();
		
		// ある場合→トップページ
		if (mailaddress != "anonymousUser") {
			AccountEntity acc = accountRepository.findByMail(mailaddress);
			model.addAttribute("mailaddress", mailaddress);
			model.addAttribute("account", acc);
			return "redirect:/chat/chat";
		}
		// ない場合→ログインページ
		else {
			model.addAttribute("loginForm", new LoginForm());
			return "login/index";
		}
	}
	
	// ログイン認証エラー(WebsecirityConfigのログイン失敗先)
	@RequestMapping(value = "/login_error", method = RequestMethod.GET)
	public String Login_Error(@Validated(LoginForm.All.class) LoginForm loginForm, BindingResult result, Model model) {
		if (result.hasErrors()) {
			// エラー(バリデーションチェック)
			System.out.println("バリデーションエラー");
			//model.addAttribute("loginForm", new LoginForm());
		}else {
			System.out.println("その他エラー");
		}
		return "redirect:/login/index";
	}

	// 新規登録ページへ
	@RequestMapping("/login/new_index")
	public String New_Index(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "login/new_index";
	}

	// バリデーションチェック＆メール送信
	@RequestMapping(value = "/login/new_confirm", method = RequestMethod.POST)
	public String SendMail(@Validated(LoginForm.All.class) LoginForm loginForm, BindingResult result, Model model, RedirectAttributes redirectAttr) {

		// バリデーションログ
		System.out.println(result);
		// パスワード入力値ログ
		System.out.println(loginForm.getPassword());
		System.out.println(loginForm.getPassword_again());

		// バリデーションチェック(正常)、パスワード、再入力(正常)
		if (!result.hasErrors() && loginForm.getPassword().equals(loginForm.getPassword_again())) {

			/*-- メールアドレスが既に使われていないか --*/
			// アカウントテーブルにアクセス
			LoginEntity exist_mailloginEntity = loginRepository.findByMail(loginForm.getMail());

			/* 「isEmpty()」：(nullと""の場合、trueを返す)
			 * 「.equals("")」：(""の場合、trueを返す。nullは判別できない)
			 * 機能としては「isEmpty()」の方がnullも含め判別ができる分良いが、逆に考えればnullか""の判別ができず、値が参照されたのかが分からない。
			 * */
			// 処理が走って値の参照もでき、中身が空白の場合
			if (exist_mailloginEntity == null) {

				// 暗証番号(乱数生成)
				Random random = new Random();
				// 100000~1000000の範囲で生成する
				randomValue = 100000 + random.nextInt(900000);
				String str = String.valueOf(randomValue);

				SimpleMailMessage msg = new SimpleMailMessage();
				// メール送信内容
				// 宛先メール
				msg.setTo(loginForm.getMail());
				// タイトル
				msg.setSubject("暗証番号送信");
				// 本文
				msg.setText("暗証番号:"+str);
				// 送信処理
				sender.send(msg);

				return "login/new_confirm";
			}
			// エラー(メールアドレスが既にある)
			System.out.println("メール既に使われている");
			// DBで取得したメールアドレス
			System.out.println(exist_mailloginEntity.getMail());
			return "login/mail_error";
		}
		// エラー(バリデーションチェック、パスワード、再入力)
		System.out.println("バリデーションエラー");
		redirectAttr.addFlashAttribute("err", "IDとパスワードを確認してください。");
		return "redirect:/login/new_index";
	}

	// 暗証入力チェック
	// codeには優先順位を設けてないため、「@Validated」だけで良い
	@RequestMapping(value = "/login/new_comp", method = RequestMethod.POST)
	public String CheckCode(@Validated LoginForm loginForm, BindingResult result, Model model) {

		// 暗証番号の入力値が一致していたら、データベース登録
		System.out.println(randomValue);
		System.out.println(loginForm.getCode());

		if (!result.hasErrors()) {
			if (randomValue == loginForm.getCode()) {

				// Entityクラスのオブジェクトを@Autowiredで生成するとパッケージ階層関連のエラーが出る、Appliction.javaクラスと同じ階層に置くと解消はされる
				// パスワードのハッシュ化
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

				// ログインテーブル
				LoginEntity loginEntity = new LoginEntity();
				loginEntity.setMail(loginForm.getMail());
				loginEntity.setPassword(encoder.encode(loginForm.getPassword()));

				// アカウントテーブル
				AccountEntity accountEntity = new AccountEntity();
				accountEntity.setNickname(loginForm.getNickname());
				accountEntity.setMail(loginForm.getMail());
				accountEntity.setPassword(encoder.encode(loginForm.getPassword()));

				// 新規登録処理
				loginRepository.save(loginEntity);
				accountRepository.save(accountEntity);

				System.out.println("登録完了");
				return "login/new_comp";

			} else {
				// 暗証番号の入力値が一致していない
				System.out.println("暗証番号不一致");
				return "login/new_confirm";
			}
		} else {
			// エラー(バリデーションチェック)
			System.out.println("バリデーションエラー");
			return "login/new_confirm";
		}
	}
	
	//パスワード再登録
	@RequestMapping(value = "/login/password_again", method = RequestMethod.GET)
	public String GetPassword_Again() {

		return "password_again";
	}
}
