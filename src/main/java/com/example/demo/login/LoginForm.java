package com.example.demo.login;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import javax.validation.GroupSequence;

public class LoginForm implements Serializable {
	private static final long serialVersionUID = 1L;

	/* バリデーション優先順位をグループ化 */
	public interface Group1 {
	}

	public interface Group2 {
	}

	@GroupSequence({ Group1.class, Group2.class })
	public interface All {
	}

	/* ニックネーム */
	/* チェック範囲
	 * NotBlank：NULL、空白文字、空白(スペース)
	 * NotEmpty：NULL、空白(スペース)
	 * NotNull：NULL
	 * */
	@NotBlank(groups = Group1.class)
	@Size(max = 10, message = "{max}文字以内で入力してください", groups = Group2.class)
	private String nickname;

	/* メールアドレス */
	@NotBlank(groups = Group1.class)
	private String mail;

	/* パスワード */
	@NotBlank(groups = Group1.class)
	@Size(min = 6, message = "{min}文字以上で入力してください", groups = Group2.class)
	@Size(max = 100, message = "{max}文字以内で入力してください", groups = Group2.class)
	private String password;

	/* 暗証番号 */
	@NotNull(message = " 入力してください")
	private Integer code;

	/* パスワード再入力 */
	@NotBlank(groups = Group1.class)
	private String password_again;

	/* ゲッター、セッター */
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getPassword_again() {
		return password_again;
	}

	public void setPassword_again(String password_again) {
		this.password_again = password_again;
	}
}
