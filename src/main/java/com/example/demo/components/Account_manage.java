package com.example.demo.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.example.demo.account.AccountEntity;
import com.example.demo.account.AccountRepository;

@Component
public class Account_manage {

	@Autowired
	AccountRepository accountRepository;

	// アカウントデータ取得メソッド(引数:メールアドレス)
	public AccountEntity Get_AccountData(Model model, String mailaddress, AccountEntity accountEntity) {
		accountEntity = accountRepository.findByMail(mailaddress);
		model.addAttribute("account", accountEntity);
		return accountEntity;
	}

	// アカウントデータ取得メソッド(引数:メールアドレス)
	public AccountEntity Get_AccountData2(String mailaddress, AccountEntity accountEntity) {
		accountEntity = accountRepository.findByMail(mailaddress);
		return accountEntity;
	}
}