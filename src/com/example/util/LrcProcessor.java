package com.example.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class LrcProcessor {	
	public ArrayList<ArrayList> process(InputStream in) throws IOException{
		ArrayList<Long> timeMills =new ArrayList<Long>();
		ArrayList<String> messages =new ArrayList<String>();
		ArrayList<ArrayList> time_message = new ArrayList<ArrayList>();		
		BufferedReader br =new BufferedReader(new InputStreamReader(in));
		String content = null;
		Pattern p = Pattern.compile("\\[([^\\]]+)\\]");
		while((content = br.readLine())!=null){
			Matcher m = p.matcher(content);
			if(m.find()){
				String timeStr = m.group();
				long timeLong = StringToLong(timeStr);
				String msg=content.substring(10);
				timeMills.add(timeLong);
				messages.add(msg);
			}
		}
		time_message.add(timeMills);
		time_message.add(messages);
		int i = 0;
		while(i <= time_message.size() -1){
			System.out.println(time_message.get(i));
			i++;
		}
		return time_message;	
	}

	private long StringToLong(String timeStr) {
		// TODO Auto-generated method stub
		String preTimeStr = timeStr.substring(1, 9);
		String[] s = preTimeStr.split(":");
		int min = Integer.parseInt(s[0]);
		String[] ss = s[1].split("\\.");
		int sec = Integer.parseInt(ss[0]);
		int mill = Integer.parseInt(ss[1]);
		return min*60*1000+sec*1000+mill*10L;
		
	}
}
