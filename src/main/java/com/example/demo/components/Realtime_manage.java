package com.example.demo.components;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class Realtime_manage {
	
	public String Realtime_process(String real_time) {
		Date dateObj = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateObj);
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        dateObj = calendar.getTime();
		
		// 日時情報を指定フォーマットの文字列で取得
		real_time = format.format(dateObj);
		return real_time;
		
	}
}