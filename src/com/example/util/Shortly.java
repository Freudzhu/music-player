package com.example.util;

public class Shortly {
	public static boolean isVoid(String s)
	{
		return s == null || s.length() == 0;
	}
	public static boolean isVoid(Integer integer)
	{
		return integer == null || integer == 0;
	}

	public static String avoidNull(String s)
	{
		return isVoid(s) ? "" : s;
	}

}
