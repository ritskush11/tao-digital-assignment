package com.tao.digital.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
	
	
	public static Date formatStringToDate(String inputDate)
	{
		try {
		  Date date = new Date();
		  SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");

		    date = df.parse(inputDate);
		    return date;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
